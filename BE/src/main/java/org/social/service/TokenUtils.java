package org.social.service;

import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.social.model.user.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;
import java.time.Duration;
import java.time.Instant;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ApplicationScoped
public class TokenUtils {
    private static final String INCORRECT_INFORMATION = "Incorrect information.";
    private static final String ACCESS_TOKEN_INVALID = "AccessToken invalid.";
    private static final String REFRESH_TOKEN_INVALID = "RefreshToken invalid.";
    @Inject
    UserInterface userInterface;

    public String generateRefreshToken(String userId) {
        JwtClaimsBuilder claimsBuilder = builder(userId, "refresh");
        claimsBuilder.expiresIn(Duration.ofDays(7));
        return claimsBuilder.jws().sign();
    }

    public String generateToken(String userId) {
        JwtClaimsBuilder claimsBuilder = builder(userId, "user");
        claimsBuilder.expiresIn(Duration.ofSeconds(30));
        return claimsBuilder.jws().sign();
    }

    public String getTokenFromCtx(SecurityContext ctx) {
        JsonWebToken jsonWebToken = (JsonWebToken) ctx.getUserPrincipal();
        return jsonWebToken.getRawToken();
    }

    private JwtClaimsBuilder builder(String userId, String role) {
        return Jwt.claims().issuer("qcsocial").subject(userId).issuedAt(Instant.now()).groups(role);
    }

    public String refreshToken(SecurityContext ctx, BiConsumer<User, String> biConsumer) {
        var user = getInfoUser(ctx);
        var refreshToken = getTokenFromCtx(ctx);
        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new UnauthorizedException(REFRESH_TOKEN_INVALID);
        }
        var newToken = generateToken(user.id.toString());
        biConsumer.accept(user, newToken);
        return newToken;
    }

    public User verify(SecurityContext ctx) {
        var user = getInfoUser(ctx);
        var accessToken = getTokenFromCtx(ctx);
        if (!accessToken.equals(user.getAccessToken())) {
            throw new UnauthorizedException(ACCESS_TOKEN_INVALID);
        }
        return user;
    }

    public void clearToken(SecurityContext ctx, Consumer<User> consumer) {
        var user = verify(ctx);
        consumer.accept(user);
    }

    private User getInfoUser(SecurityContext ctx) {
        if (ctx.getUserPrincipal() == null || ctx.getUserPrincipal().getName() == null) {
            throw new UnauthorizedException(INCORRECT_INFORMATION);
        }
        var userName = ctx.getUserPrincipal().getName();
        return userInterface.findUserById(userName);
    }
}
