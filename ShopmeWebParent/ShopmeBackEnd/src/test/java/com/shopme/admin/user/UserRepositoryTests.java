package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;

    @BeforeEach
    public void setUp() {
        String email = "test.user@example.com";
        testUser = createUserIfNotExists(email, "Password./124", "Test", "User", entityManager.find(Role.class, 1));
    }

    private User createUserIfNotExists(String email, String password, String firstName, String lastName, Role... roles) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            User user = new User(email, password , firstName, lastName);
            for (Role role : roles) {
                user.addRole(role);
            }
            return userRepository.save(user);
        }
        return existingUser.get();
    }

    @Test
    public void shouldCreateUserIfNotExists() {
        User savedUser = createUserIfNotExists("n.oumellahni@gmail.com", "Password./123",  "Nabil", "Oumellahni", entityManager.find(Role.class, 1));
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    public void shouldCreateUserWithTwoRoles() {
        Role roleEditor = entityManager.find(Role.class, 3);
        Role roleAssistant = entityManager.find(Role.class, 5);
        User savedUser = createUserIfNotExists("h.oumellahni01@gmail.com","Password./123", "Hamid", "Oumellahni", roleEditor, roleAssistant);
        assertThat(savedUser.getRoles()).hasSize(2).contains(roleEditor, roleAssistant);
    }

    @Test
    public void shouldListAllUsers() {
        Iterable<User> users = userRepository.findAll();
        assertThat(users).hasSizeGreaterThan(0);
    }

    @Test
    public void shouldGetUserById() {
        Optional<User> user = userRepository.findById(testUser.getId());
        assertThat(user).isPresent();
    }

    @Test
    public void shouldUpdateUserDetails() {
        User user = userRepository.findById(testUser.getId()).get();
        boolean initialStatus = user.isEnabled();
        user.setEnabled(!initialStatus);
        User updatedUser = userRepository.save(user);
        assertThat(updatedUser.isEnabled()).isNotEqualTo(initialStatus);
    }

    @Test
    public void shouldDeleteUser() {
        userRepository.deleteById(testUser.getId());
        assertThat(userRepository.existsById(testUser.getId())).isFalse();
    }
}
