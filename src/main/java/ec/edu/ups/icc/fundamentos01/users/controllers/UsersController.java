package ec.edu.ups.icc.fundamentos01.users.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.services.UserService;
import jakarta.validation.Valid;

/*
 * Controlador REST encargado de exponer los endpoints HTTP
 * para la gestión de usuarios.
 *
 * En esta práctica el controlador ya no contiene la lógica del CRUD.
 * Solo recibe la petición y delega la operación al servicio.
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService service;

    /*
     * Inyección de dependencias por constructor.
     */
    public UsersController(UserService service) {
        this.service = service;
    }

    /*
     * Endpoint para listar todos los usuarios.
     * GET /users
     */
    @GetMapping
    public List<UserResponseDto> findAll() {
        return service.findAll();
    }

    /*
     * Endpoint para buscar un usuario por id.
     * GET /users/{id}
     */
    @GetMapping("/{id}")
    public UserResponseDto findOne(@PathVariable Long id) { // Cambiado de Object a UserResponseDto
        return service.findOne(id);
    }

    /*
     * Endpoint para crear un nuevo usuario.
     * POST /users
     */
    @PostMapping
    public UserResponseDto create(@Valid @RequestBody CreateUserDto dto) {
        return service.create(dto);
    }

    /*
     * Endpoint para actualizar completamente un usuario.
     * PUT /users/{id}
     */
    @PutMapping("/{id}")
    public UserResponseDto update( // Cambiado de Object a UserResponseDto
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDto dto) {
        return service.update(id, dto);
    }

    /*
     * Endpoint para actualizar parcialmente un usuario.
     * PATCH /users/{id}
     */
    @PatchMapping("/{id}")
    public UserResponseDto partialUpdate( // Cambiado de Object a UserResponseDto
            @PathVariable Long id,
            @Valid @RequestBody PartialUpdateUserDto dto) {
        return service.partialUpdate(id, dto);
    }

    /*
     * Endpoint para eliminar un usuario.
     * DELETE /users/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { // Cambiado de Object a void
        service.delete(id);
    }
}
