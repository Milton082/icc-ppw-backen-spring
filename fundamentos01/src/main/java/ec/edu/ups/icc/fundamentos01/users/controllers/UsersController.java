package ec.edu.ups.icc.fundamentos01.users.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final List<UserModel> users = new ArrayList<>();
    private Long currentId = 1L;

    @GetMapping
    public List<UserResponseDto> findAll() {
        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public Object findOne(@PathVariable("id") Long id) {

        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(user -> (Object) UserMapper.toResponse(user))
                .orElseGet(() -> Map.of(
                        "error", "User not found"
                ));
    }

    @PostMapping
    public UserResponseDto create(@RequestBody CreateUserDto dto) {

        UserModel user = UserMapper.toModel(dto);

        user.setId(currentId++);

        users.add(user);

        return UserMapper.toResponse(user);
    }

    @PutMapping("/{id}")
    public Object update(
            @PathVariable("id") Long id,
            @RequestBody UpdateUserDto dto) {

        UserModel user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return Map.of(
                    "error", "User not found"
            );
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        return UserMapper.toResponse(user);
    }

    @PatchMapping("/{id}")
    public Object partialUpdate(
            @PathVariable("id") Long id,
            @RequestBody PartialUpdateUserDto dto) {

        UserModel user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return Map.of(
                    "error", "User not found"
            );
        }

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        return UserMapper.toResponse(user);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") Long id) {

        boolean deleted = users.removeIf(
                user -> user.getId().equals(id)
        );

        if (!deleted) {
            return Map.of(
                    "error", "User not found"
            );
        }

        return Map.of(
                "message", "Deleted successfully"
        );
    }
}