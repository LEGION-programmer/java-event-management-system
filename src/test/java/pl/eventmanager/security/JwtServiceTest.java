package pl.eventmanager.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import pl.eventmanager.entity.User;
import pl.eventmanager.enums.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtService jwtService;

    @Test
    void shouldGenerateTokenForSavedUser() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getUsername()).thenReturn("jan");
        when(user.getEmail()).thenReturn("jan@example.com");
        when(user.getRole()).thenReturn(Role.ROLE_USER);

        when(jwtProperties.issuer()).thenReturn("test-issuer");
        when(jwtProperties.expirationSeconds()).thenReturn(3600L);

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn("mocked-token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        JwtToken token = jwtService.generateToken(user);

        assertEquals("mocked-token", token.value());
        assertEquals(3600L, token.expiresIn());
    }

    @Test
    void shouldThrowWhenUserNotYetSaved() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> jwtService.generateToken(user));
    }
}