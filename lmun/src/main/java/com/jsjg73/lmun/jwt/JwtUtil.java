package com.jsjg73.lmun.jwt;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jsjg73.lmun.repositories.UserRepository;
import com.jsjg73.lmun.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

@Service
public class JwtUtil {
	@Autowired
	JwtConfig jwtConfig;
	@Autowired
	SecretKey secretKey;
	@Autowired
	UserService userService;
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	public List<String> extractAuthorities(String token){
		final Claims claims = extractAllClaims(token);
		List<Map<String,String>> authorities = (List<Map<String,String>>) claims.get("authorities");
		return authorities.stream().map(map->map.get("authority")).collect(Collectors.toList());
	}
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public String generateToken(Authentication authentication) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, authentication);
	}
	public String reGenerateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		UserDetails userDetails = userService.loadUserByUsername(username);
		return createToken(claims, userDetails);
	}
	private String createToken(Map<String, Object> claims, Authentication authentication) {
		return Jwts.builder().setClaims(claims)
				.setSubject(authentication.getName())
				.claim("authorities", authentication.getAuthorities())
				.setIssuedAt(new Date())
				.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
				.signWith(secretKey).compact();
	}
	private String createToken(Map<String, Object> claims, UserDetails userDetails) {
		return Jwts.builder().setClaims(claims)
				.setSubject(userDetails.getUsername())
				.claim("authorities", userDetails.getAuthorities())
				.setIssuedAt(new Date())
				.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
				.signWith(secretKey).compact();
	}
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	public String eliminatePrefix(String token){
		return token.substring(7);
	}
}
