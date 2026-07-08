package ec.edu.ups.icc.fundamentos01.users.services;

import java.util.List;

import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entity.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;
import ec.edu.ups.icc.fundamentos01.users.repository.UserRepository;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;

/*
 * Implementación del servicio de usuarios.
 * * En esta clase se reemplaza la lista en memoria por UserRepository.
 * El repositorio se encarga de comunicarse con PostgreSQL mediante JPA.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * Retorna todos los usuarios almacenados en PostgreSQL.
     */
    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .filter(entity -> !entity.isDeleted()) // Regla de negocio: filtrar eliminados
                .map(UserMapper::toModelFromEntity)
                .map(UserMapper::toResponse)
                .toList();
    }

    /*
     * Busca un usuario por id.
     * Si no existe o está eliminado, lanza NotFoundException.
     */
    @Override
    public UserResponseDto findOne(Long id) {
        UserEntity entity = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found"));
            
        if (entity.isDeleted()) {
            throw new NotFoundException("User not found");
        }
        
        UserModel model = UserMapper.toModelFromEntity(entity);
        return UserMapper.toResponse(model);
    }

    /*
     * Crea un nuevo usuario en la base de datos.
     * Si el email ya está registrado, lanza ConflictException.
     */
    @Override
    public UserResponseDto create(CreateUserDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("Email already registered");
        }
        
        UserModel model = UserMapper.toModelFormDTO(dto);
        UserEntity entity = UserMapper.toEntityFromModel(model);
        
        // ¡Usamos nuestra función para hashear!
        entity.setPasswordHash(hashPasswordNative(dto.getPassword()));
        
        UserEntity savedEntity = userRepository.save(entity);
        UserModel savedModel = UserMapper.toModelFromEntity(savedEntity);
        return UserMapper.toResponse(savedModel);
    }

    /*
     * Actualiza completamente un usuario existente.
     * Si no existe o está eliminado, lanza NotFoundException.
     */
    @Override
    public UserResponseDto update(Long id, UpdateUserDto dto) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());

        UserEntity savedEntity = userRepository.save(entity);
        UserModel model = UserMapper.toModelFromEntity(savedEntity);
        
        return UserMapper.toResponse(model);
    }

    /*
     * Actualiza parcialmente un usuario existente.
     * Si no existe o está eliminado, lanza NotFoundException.
     */
    @Override
    public UserResponseDto partialUpdate(Long id, PartialUpdateUserDto dto) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            entity.setPasswordHash(hashPasswordNative(dto.getPassword()));
        }

        UserEntity savedEntity = userRepository.save(entity);
        UserModel model = UserMapper.toModelFromEntity(savedEntity);
        
        return UserMapper.toResponse(model);
    }

    /*
     * Elimina lógicamente un usuario por id.
     * Si no existe o está eliminado, lanza NotFoundException.
     */
    @Override
    public void delete(Long id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        entity.setDeleted(true);
        userRepository.save(entity);
    }

    /*
     * Método auxiliar para encriptar contraseñas usando SHA-256 nativo de Java.
     * Evita tener que instalar Spring Security.
     */
    private String hashPasswordNative(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }
}
