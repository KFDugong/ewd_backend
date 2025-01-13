package dev.ewd.mediashelf_spring.mediashelf_spring.service;

import dev.ewd.mediashelf_spring.mediashelf_spring.model.Users;
import dev.ewd.mediashelf_spring.mediashelf_spring.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.accessTokenSecret}")
    private String accessTokenSecret;
    @Value("${jwt.refreshTokenSecret}")
    private String refreshTokenSecret;

    @Autowired
    UserRepository userRepository;

    private String tokenBuilder(Users user, Date expirationDuration, String secretKey) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("userRole", user.getUserRole());
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationDuration)
                .and()
                .signWith(getKey())
                .compact();
    }

    public String generateAccessToken(Users user) {
        return tokenBuilder(user, new Date(System.currentTimeMillis() + 1000 * 60 * 15), accessTokenSecret);
    }

    public String generateRefreshToken(Users user){
        return tokenBuilder(user, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 14), refreshTokenSecret);
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(accessTokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token){
        Claims claims = extractAllClaims(token);
        return claims.get("username", String.class);
    }

    public Long extractUserId(String token){
        String subject = extractClaim(token, Claims::getSubject);
        try {
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid token subject for userId");
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (!isTokenExpired(token) && username.equals(userDetails.getUsername()));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
}
