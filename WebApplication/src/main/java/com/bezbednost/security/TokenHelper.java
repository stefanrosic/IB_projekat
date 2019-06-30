package com.bezbednost.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bezbednost.provider.TimeProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenHelper {

	@Value("InformacionaBezbednost")
	private String APP_NAME;
	
	@Value("3000")
	private int EXPIRES_IN;
	
	@Value("Authorization")
	private String AUTH_HEADER;
	
	static final String AUDIENCE = "ibuser";
	
	@Value("DOCTORINBOXMASSAGESAPPLICATIONSECRETSECRETSECRETSECRETSECR#@#@!")
	public String SECRET;
	
	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
	
	@Autowired
	TimeProvider timeProvider;
	
	
	//METHOD WHICH GET USER_NAME FROM CLAIMS(TOKEN BODY)
	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}
	
	//METHOD WHICH RETURN ALL CLAIMS FROM TOKEN BODY
	private Claims getAllClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}
	
	public Date getIssuedAtDateFromToken(String token) {
		Date issueAt;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			issueAt = claims.getIssuedAt();
		} catch (Exception e) {
			issueAt = null;
		}
		return issueAt;
	}

	public String getAudienceFromToken(String token) {
		String audience;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			audience = claims.getAudience();
		} catch (Exception e) {
			audience = null;
		}
		return audience;
	}
	
	//METHOD WHICH GENERATE TOKEN, SET ISSUER(OUR APP), SER SUBJECT(USERNAME OF USER), SET DATE OF ISSUE(NOW), SET EXPIRE DATE
	//AND SIGNATURE IT WITH SIGNATURE_ALGORITHM
	public String generateToken(String username) {
		return Jwts.builder().setIssuer(APP_NAME).setSubject(username).setAudience(AUDIENCE)
				.setIssuedAt(timeProvider.now()).setExpiration(generateExpirationDate())
				.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
	}
	
	
	//GET EXPIRATION DATE FROM PASSED TOKEN
	public long getExiprationDate(String token) {
		return getAllClaimsFromToken(token).getExpiration().getTime();
	}

	//GENERATE EXPIRATION DATE FOR NEW TOKEN
	private Date generateExpirationDate() {
		return new Date(timeProvider.now().getTime() + EXPIRES_IN * 1000);
	}
	
	
	//METHOD WHO CHECKS IF THE TOKEN IS VALID IN RELATION TO THE USER WHO PASSED IT TO THE SERVER
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username != null && username.equals(userDetails.getUsername()));
	}

	
	//GET TOKEN FROM AUTH HEADER
	public String getToken(HttpServletRequest request) {
		String authHeader = getAuthHeaderFromHeader(request);
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}

		return null;
	}
	
	//GET AUTH FIELD HEADER FROM HEADER
	public String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(AUTH_HEADER);
	}
	
}
