package pl.eventmanager.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.eventmanager.entity.User;
import pl.eventmanager.enums.Role;
import pl.eventmanager.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DatabaseUserDetailsService service;

    private User buildUser() {
        User user = new User();
        user.setUsername("jan");
        user.setEmail("jan@example.com");
        user.setPassword("hashed");
        user.setRole(Role.ROLE_USER);
        return user;
    }

    @Test
    void shouldLoadUserByEmail() {
        User user = buildUser();

        when(userRepository.findByEmailIgnoreCase("jan@example.com"))
                .thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("jan@example.com");

        assertEquals("jan@example.com", details.getUsername());
    }

    @Test
    void shouldLoadUserByUsernameWhenEmailNotFound() {
        User user = buildUser();

        when(userRepository.findByEmailIgnoreCase("jan"))
                .thenReturn(Optional.empty());
        when(userRepository.findByUsernameIgnoreCase("jan"))
                .thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("jan");

        assertEquals("jan@example.com", details.getUsername());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findByEmailIgnoreCase("unknown"))
                .thenReturn(Optional.empty());
        when(userRepository.findByUsernameIgnoreCase("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("unknown"));
    }
}