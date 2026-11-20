package ru.university.jewelry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.model.Permission;
import ru.university.jewelry.model.Role;
import ru.university.jewelry.model.User;
import ru.university.jewelry.repository.PermissionRepository;
import ru.university.jewelry.repository.RoleRepository;
import ru.university.jewelry.repository.UserRepository;

@SpringBootApplication
@EnableCaching
@RequiredArgsConstructor
public class JewelryApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(JewelryApplication.class, args);
    }

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createPermissionsIfNotExists();
        createRolesIfNotExists();
        createUsersIfNotExists();
    }

    public void createUsersIfNotExists() {
        if (userRepository.findByUsername("admin").isPresent())
            return;

        Role roleAdmin = roleRepository.findByTitle("ADMIN").orElseThrow();
        Role roleUser = roleRepository.findByTitle("USER").orElseThrow();
        Role roleManager = roleRepository.findByTitle("MANAGER").orElseThrow();

        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles(Set.of(roleAdmin))
                .build();
        userRepository.save(admin);

        User user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .roles(Set.of(roleUser))
                .build();
        userRepository.save(user);

        User manager = User.builder()
                .username("manager")
                .password(passwordEncoder.encode("manager"))
                .roles(Set.of(roleManager))
                .build();
        userRepository.save(manager);
    }

    private void createRolesIfNotExists() {
        if (roleRepository.count() == 0) {
            Role userRole = Role.builder().title("USER").build();
            Role moderatorRole = Role.builder().title("MODERATOR").build();
            Role managerRole = Role.builder().title("MANAGER").build();
            Role adminRole = Role.builder().title("ADMIN").build();

            roleRepository.save(userRole);
            roleRepository.save(managerRole);
            roleRepository.save(moderatorRole);
            roleRepository.save(adminRole);

            Permission jewelryRead = permissionRepository.findByPermissionAndOperation("jewelry", "read").orElseThrow();
            Permission jewelryWrite = permissionRepository.findByPermissionAndOperation("jewelry", "write").orElseThrow();
            Permission jewelryDelete = permissionRepository.findByPermissionAndOperation("jewelry", "delete").orElseThrow();

            userRole.setPermissions(new HashSet<>(Set.of(jewelryRead)));
            managerRole.setPermissions(new HashSet<>(Set.of(jewelryRead, jewelryWrite)));
            adminRole.setPermissions(new HashSet<>(Set.of(jewelryRead, jewelryWrite, jewelryDelete)));

            roleRepository.save(userRole);
            roleRepository.save(managerRole);
            roleRepository.save(adminRole);
        }
    }

    private void createPermissionsIfNotExists() {
        if (permissionRepository.count() == 0) {
            permissionRepository.saveAll(List.of(
                    Permission.builder().permission("jewelry").operation("read").build(),
                    Permission.builder().permission("jewelry").operation("write").build(),
                    Permission.builder().permission("jewelry").operation("delete").build()
            ));
        }
    }
}
