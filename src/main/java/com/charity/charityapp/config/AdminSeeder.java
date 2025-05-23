package com.charity.charityapp.config;

import com.charity.charityapp.enums.Role;
import com.charity.charityapp.model.User;
import com.charity.charityapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public AdminSeeder(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder  = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@charityapp.com";
        if (!userRepo.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setFirstName("Super");
            admin.setLastName("Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(encoder.encode("ChangeMe123!"));
            admin.setRole(Role.ADMIN);
            userRepo.save(admin);
            System.out.println("[AdminSeeder] Seeded default admin user: " + adminEmail);
        }
    }
}
