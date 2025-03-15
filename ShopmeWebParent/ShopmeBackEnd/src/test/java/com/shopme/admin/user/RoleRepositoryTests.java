package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole() {
        saveRoleIfNotExists("ADMIN", "Manage everything in the system");
    }

    @Test
    public void testCreateAdditionalRoles() {
        saveRoleIfNotExists("SALES_PERSON", "Manage product pricing, customers, and sales");
        saveRoleIfNotExists("EDITOR", "Manage categories, brands, products, and articles");
        saveRoleIfNotExists("SHIPPER", "View products, manage shipping orders, and track deliveries");
        saveRoleIfNotExists("ASSISTANT", "Manage customer questions and reviews");
    }

    private void saveRoleIfNotExists(String name, String description) {
        Optional<Role> existingRole = roleRepository.findByName(name);
        if (existingRole.isEmpty()) {
            Role role = new Role(name, description);
            Role savedRole = roleRepository.save(role);
            assertThat(savedRole.getId()).isGreaterThan(0);
        } else {
            System.out.println("Role already exists: " + name);
        }
    }
}
