package ec.edu.ups.icc.fundamentos01.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.fundamentos01.users.entity.UserEntity;

/**
 * Repositorio encargado de gestionar la persistencia
 * de usuarios usando Spring Data JPA.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Spring Data JPA generará automáticamente la consulta SQL basada en el nombre
    // del método

    Optional<UserEntity> findByIdAndDeletedFalse(Long id);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByIdAndDeletedFalse(Long id);

    Optional<UserEntity> findById(Long id);

    // ============== NUEVOS MÉTODOS PARA SEGURIDAD ==============

    // Buscar usuario por email (usado en login)
    Optional<UserEntity> findByEmailAndDeletedFalse(String email);

    // Verificar si email ya está registrado (usado en registro)
    boolean existsByEmail(String email);
}