package pl.eventmanager.security;

public record JwtToken(
        String value,
        long expiresIn
) {
}