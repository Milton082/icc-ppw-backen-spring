package ec.edu.ups.icc.fundamentos01.users.mappers;

import java.time.LocalDateTime;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entity.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;

/*
 * Clase encargada de convertir objetos entre DTOs, modelos y entidades.
 *
 * En esta práctica se agrega la conversión hacia UserEntity
 * porque ya se trabaja con persistencia real en PostgreSQL.
 */
public class UserMapper {

    /*
     * Convierte un CreateUserDto en UserModel.
     *
     * El DTO contiene los datos recibidos desde la API.
     * El modelo representa el usuario dentro de la lógica de la aplicación.
     */
    public static UserModel toModelFormDTO(CreateUserDto dto) {
        UserModel model = new UserModel();
        model.setName(dto.getName());
        model.setEmail(dto.getEmail());
        model.setPassword(dto.getPassword());
        model.setPasswordHash(dto.getPassword());
        model.setCreatedAt(LocalDateTime.now());
        model.setDeleted(false);
        
        return model;
    }

    /*
     * Convierte una entidad JPA en UserModel.
     *
     * Se usa cuando el repositorio devuelve datos desde PostgreSQL.
     */
    public static UserModel toModelFromEntity(UserEntity entity) {
        UserModel model = new UserModel();

        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setEmail(entity.getEmail());
        model.setPasswordHash(entity.getPasswordHash());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setDeleted(entity.isDeleted());

        return model;
    }

    /*
     * Convierte un UserModel en UserEntity.
     *
     * Se usa antes de guardar datos en la base de datos.
     */
    public static UserEntity toEntityFromModel(UserModel model) {
        UserEntity entity = new UserEntity();

        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setEmail(model.getEmail());
        entity.setPasswordHash(model.getPasswordHash());

        return entity;
    }

    /*
     * Convierte un UserModel en UserResponseDto.
     *
     * No se expone password ni passwordHash.
     */
    public static UserResponseDto toResponse(UserModel model) {
        UserResponseDto response = new UserResponseDto();
        
        response.setId(model.getId());
        response.setName(model.getName());
        response.setEmail(model.getEmail());
        response.setCreatedAt(model.getCreatedAt());
        response.setUpdatedAt(model.getUpdatedAt());
        
        return response;
    }
}
