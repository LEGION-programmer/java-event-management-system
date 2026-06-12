package pl.eventmanager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.eventmanager.dto.AuthResponse;
import pl.eventmanager.dto.LoginRequest;
import pl.eventmanager.dto.RegisterRequest;
import pl.eventmanager.entity.User;
import pl.eventmanager.security.JwtService;
import pl.eventmanager.security.JwtToken;
import pl.eventmanager.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock private UserService userService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;
    @InjectMocks private AuthController authController;

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userService.existsByEmail(any())).thenReturn(false);
        when(userService.existsByUsername(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userService.save(any())).thenReturn(new User());

        RegisterRequest request = new RegisterRequest();
        request.setUsername("jan");
        request.setEmail("jan@example.com");
        request.setPassword("haslo123");

        ResponseEntity<String> result = authController.register(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenEmailExists() {
        when(userService.existsByEmail(any())).thenReturn(true);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("jan");
        request.setEmail("jan@example.com");
        request.setPassword("haslo123");

        ResponseEntity<String> result = authController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenUsernameExists() {
        when(userService.existsByEmail(any())).thenReturn(false);
        when(userService.existsByUsername(any())).thenReturn(true);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("jan");
        request.setEmail("jan@example.com");
        request.setPassword("haslo123");

        ResponseEntity<String> result = authController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void shouldLoginSuccessfully() {
        User user = mock(User.class);
        when(userService.findByUsername("jan")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(new JwtToken("test-token", 3600L));

        LoginRequest request = new LoginRequest();
        request.setUsername("jan");
        request.setPassword("haslo123");

        ResponseEntity<AuthResponse> result = authController.login(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("test-token", result.getBody().getToken());
    }
}