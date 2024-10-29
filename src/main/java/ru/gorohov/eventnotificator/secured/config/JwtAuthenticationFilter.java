package ru.gorohov.eventnotificator.secured.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.gorohov.eventnotificator.secured.jwt.JwtManager;
import ru.gorohov.eventnotificator.secured.user.UserSecurity;
import ru.gorohov.eventnotificator.secured.user.domain.UserService;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;
    private final UserService userService;
    private static final String HEADER_NAME = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("DoFilterInternal");

        var header = request.getHeader(HEADER_NAME);
        logger.info("Authorization header: " + header);

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        var jwtToken = header.substring(7);

        if (!jwtManager.isTokenValid(jwtToken)) {
            logger.info("Token is invalid");
            filterChain.doFilter(request, response);
            return;
        }
        logger.info("Token is valid");

        var username = jwtManager.getLoginFromToken(jwtToken);
        var role = jwtManager.getRoleFromToken(jwtToken);
        var user = userService.getUserByLogin(username);
        var userSecured = UserSecurity.builder()
                .login(user.getLogin())
                .id(user.getId())
                .age(user.getAge())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole())

                .build();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userSecured, null,
                        List.of(new SimpleGrantedAuthority(role)));

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);

    }
}