package com.datapirate.springsecurity.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Akash on 21-Jun-20
 */
public class JwrTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwrTokenVerifier(JwtConfig jwtConfig,
                            SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // String authorizationHeader = request.getHeader("Authorization");
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

//        String token = authorizationHeader.replace("Bearer ", "");
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

        try {
//            String key = "khatrisecuresecuresecuresecuresecuresecure";

            Jws<Claims> claimsJws = Jwts.parser()
                    //.setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            var authorities = (List<Map<String, String>>) body.get("authorities");

            var grantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());


            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    grantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }

        filterChain.doFilter(request, response);

    }
}
