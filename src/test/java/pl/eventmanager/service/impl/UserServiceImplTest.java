package pl.eventmanager.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.eventmanager.entity.User;
import pl.eventmanager.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldSaveUser() {
        User user = new User();

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertSame(user, result);
        verify(userRepository).save(user);
    }

    @Test
    void shouldFindUserByEmailIgnoringCase() {
        String email = "User@Example.com";
        User user = new User();

        when(userRepository.findByEmailIgnoreCase(email))
                .thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail(email);

        assertTrue(result.isPresent());
        assertSame(user, result.get());
        verify(userRepository).findByEmailIgnoreCase(email);
    }

    @Test
    void shouldFindUserByUsernameIgnoringCase() {
        String username = "ExampleUser";
        User user = new User();

        when(userRepository.findByUsernameIgnoreCase(username))
                .thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isPresent());
        assertSame(user, result.get());
        verify(userRepository).findByUsernameIgnoreCase(username);
    }

    @Test
    void shouldCheckEmailExistenceIgnoringCase() {
        String email = "User@Example.com";

        when(userRepository.existsByEmailIgnoreCase(email))
                .thenReturn(true);

        boolean result = userService.existsByEmail(email);

        assertTrue(result);
        verify(userRepository).existsByEmailIgnoreCase(email);
    }

    @Test
    void shouldCheckUsernameExistenceIgnoringCase() {
        String username = "ExampleUser";

        when(userRepository.existsByUsernameIgnoreCase(username))
                .thenReturn(false);

        boolean result = userService.existsByUsername(username);

        assertFalse(result);
        verify(userRepository).existsByUsernameIgnoreCase(username);
    }

    @Test
    void shouldFindUserById() {
        Long id = 1L;
        User user = new User();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(id);

        assertTrue(result.isPresent());
        assertSame(user, result.get());
        verify(userRepository).findById(id);
    }
}