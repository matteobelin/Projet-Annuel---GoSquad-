package com.gosquad.infrastructure.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class JWTInterceptorTest {

    private JWTInterceptor jwtInterceptor;
    private Key key;
    private String secret = "12345678901234567890123456789012"; // 32 chars secret

    @BeforeEach
    void setUp() {
        jwtInterceptor = new JWTInterceptor(secret);
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    @Test
    void preHandle_shouldReturnFalse_whenNoAuthorizationHeader() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter responseWriter = new StringWriter();

        when(request.getHeader("Authorization")).thenReturn(null);
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(responseWriter.toString().contains("Missing or invalid Authorization header"));
    }

    @Test
    void preHandle_shouldReturnFalse_whenAuthorizationHeaderInvalid() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter responseWriter = new StringWriter();

        when(request.getHeader("Authorization")).thenReturn("InvalidToken");
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(responseWriter.toString().contains("Missing or invalid Authorization header"));
    }

    @Test
    void preHandle_shouldReturnFalse_whenTokenExpiredOrInvalid() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter responseWriter = new StringWriter();

        // Create a token signed with a different key (invalid)
        String badToken = Jwts.builder()
                .setSubject("1")
                .signWith(Keys.hmacShaKeyFor("badsecretbadsecretbadsecretbad12".getBytes()))
                .compact();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + badToken);
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(responseWriter.toString().contains("Invalid or expired token"));
    }

    @Test
    void preHandle_shouldReturnTrue_whenTokenValid() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Generate a valid token with claims
        String token = Jwts.builder()
                .setSubject("42")
                .claim("role", "ROLE_ADVISOR")
                .claim("companyCode", "COMP123")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 mins
                .signWith(key)
                .compact();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertTrue(result);

        verify(request).setAttribute("advisorId", "42");
        verify(request).setAttribute("role", "ROLE_ADVISOR");
        verify(request).setAttribute("companyCode", "COMP123");
    }
}
