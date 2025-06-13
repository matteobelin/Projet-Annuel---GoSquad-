package com.gosquad.infrastructure.jwt;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTInterceptor implements HandlerInterceptor {
    private final Key key;

    public JWTInterceptor() {
        String secret = Dotenv.load().get("JWT_SECRET");
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public JWTInterceptor(String jwtSecret) {this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());}
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        final String authHeader  = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return false;
        }

        String token = authHeader.substring(7);

        try{
            Claims claims = Jwts.parserBuilder()
                                                .setSigningKey(key)
                                                .build()
                                                .parseClaimsJws(token)
                                                .getBody();
            request.setAttribute("advisorId", claims.getSubject());
            request.setAttribute("role", claims.get("role"));
            request.setAttribute("companyCode", claims.get("companyCode"));

            return true;
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return false;
        }

    }

    public Map<String, Object> extractTokenInfo(String token) throws Exception {
        Map<String, Object> tokenInfo = new HashMap<>();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        tokenInfo.put("id", claims.getSubject());
        tokenInfo.put("role", claims.get("role"));
        tokenInfo.put("companyCode", claims.get("companyCode"));

        return tokenInfo;
    }

}
