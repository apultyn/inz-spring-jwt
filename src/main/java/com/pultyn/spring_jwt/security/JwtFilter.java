package com.pultyn.spring_jwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader( "Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        try {
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Claims claims = jwtService.extractAllClaims(token);
                UserDetails userDetails = context.getBean(CustomUserDetailsService.class).loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken
                            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(claims);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("JWT Expired");
        }
        filterChain.doFilter(request, response);
    }
}
