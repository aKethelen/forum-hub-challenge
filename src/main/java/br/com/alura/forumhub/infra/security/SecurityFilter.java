package br.com.alura.forumhub.infra.security;

import br.com.alura.forumhub.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();

        return (HttpMethod.POST.matches(method) && "/usuarios".equals(path))
                || (HttpMethod.POST.matches(method) && "/login".equals(path))
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || (HttpMethod.GET.matches(method) && "/".equals(path))
                || (HttpMethod.GET.matches(method) && "/status".equals(path))
                || (HttpMethod.GET.matches(method) && "/docs".equals(path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            try {
                var subject = tokenService.getSubject(tokenJWT);
                var usuario = repository.findByEmail(subject);

                if (usuario != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            usuario.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (RuntimeException e) {
                enviarErroJson(response, e.getMessage(), request);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.replace("Bearer ", "").trim();
    }

    private void enviarErroJson(HttpServletResponse response, String mensagem, HttpServletRequest request) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        String json = String.format(
                "{\"timestamp\": \"%s\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"%s\", \"path\": \"%s\"}",
                java.time.LocalDateTime.now(), mensagem, request.getRequestURI()
        );

        response.getWriter().write(json);
    }
}