package com.bytestore.security.jwt;

import com.bytestore.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt;
        String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            username = jwtService.extractUsername(jwt);
        } catch (ExpiredJwtException ex) {
            handleJwtException(response, request, ex, HttpStatus.UNAUTHORIZED, "Token expirado",
                    "O token de autenticação expirou. Por favor, faça login novamente.", "JWT_EXPIRED");
            return;
        } catch (JwtException ex) {
            handleJwtException(response, request, ex, HttpStatus.UNAUTHORIZED, "Token inválido",
                    "Erro ao processar o token de autenticação. Por favor, faça login novamente.", "JWT_ERROR");
            return;
        }

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            try {
                if (jwtService.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (ExpiredJwtException ex) {
                handleJwtException(response, request, ex, HttpStatus.UNAUTHORIZED, "Token expirado",
                        "O token de autenticação expirou. Por favor, faça login novamente.", "JWT_EXPIRED");
                return;
            } catch (JwtException ex) {
                handleJwtException(response, request, ex, HttpStatus.UNAUTHORIZED, "Token inválido",
                        "Erro ao processar o token de autenticação. Por favor, faça login novamente.", "JWT_ERROR");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleJwtException(HttpServletResponse response, HttpServletRequest request,
                                    Exception ex, HttpStatus status, String error, String message, String errorCode)
            throws IOException {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                status.value(),
                error,
                message,
                request.getRequestURI(),
                errorCode
        );

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }
}
