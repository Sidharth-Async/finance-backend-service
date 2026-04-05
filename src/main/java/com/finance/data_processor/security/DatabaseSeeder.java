package com.finance.data_processor.security;

import com.finance.data_processor.model.Role;
import com.finance.data_processor.model.RoleName;
import com.finance.data_processor.model.User;
import com.finance.data_processor.model.UserStatus;
import com.finance.data_processor.repository.RoleRepository;
import com.finance.data_processor.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // this for basic viewer role
            if (roleRepository.count() == 0) {
                Role viewerRole = new Role();
                viewerRole.setName(RoleName.VIEWER);
                roleRepository.save(viewerRole);

                Role adminRole = new Role();
                adminRole.setName(RoleName.ADMIN);
                roleRepository.save(adminRole);
                System.out.println("Roles seeded into database.");
            }

            // just for testing we have created admin role
            if (userRepository.count() == 0) {
                Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                        .orElseThrow(() -> new RuntimeException("Admin role not found"));

                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPasswordHash(passwordEncoder.encode("admin123"));
                adminUser.setStatus(UserStatus.ACTIVE);
                adminUser.setRoles(Set.of(adminRole));

                userRepository.save(adminUser);
                System.out.println("Admin User created. Username: admin | Password: admin123");
            }
        };
    }
}
