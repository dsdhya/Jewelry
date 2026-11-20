package ru.university.jewelry.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import ru.university.jewelry.dto.UserDto;
import ru.university.jewelry.dto.UserLoggedDto;
import ru.university.jewelry.model.Permission;
import ru.university.jewelry.model.Role;
import ru.university.jewelry.model.User;

public class UserMapper {
    public static UserDto userToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                getUserRoleNames(user),
                getUserPermissions(user));
    }

    public static User userDtoToUser(UserDto dto) {
        User user = new User();
        user.setUsername(dto.username());
        return user;
    }

    public static UserLoggedDto userToUserLoggedDto(User user) {
        return new UserLoggedDto(
                user.getUsername(),
                getUserRoleNames(user),
                getUserPermissions(user));
    }

    private static Set<String> getUserRoleNames(User user) {
        if (user.getRoles() == null)
            return Collections.emptySet();
        return user.getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet());
    }

    private static Set<String> getUserPermissions(User user) {
        if (user.getRoles() == null)
            return Collections.emptySet();
        return user.getRoles().stream()
                .map(Role::getPermissions)
                .filter(permissions -> permissions != null)
                .flatMap(Collection::stream)
                .map(Permission::getAuthority)
                .collect(Collectors.toSet());
    }
}
