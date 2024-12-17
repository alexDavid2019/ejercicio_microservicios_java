package ar.example.registration.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import ar.example.registration.exception.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component

public class JwtUtils {

    @Value("${security.jwt-secret}")
	private String jwtSecret;
	
	@Value("${security.jwt-expiration-milliseconds}")
	private int jwtExpirationInMs;

    @Value("${security.jwt.key}")
	private String jwtKey;
	
	@Value("${security.jwt.user.generator}")
	private String userGenerator;

	public String generarToken(Authentication authentication) {
		String username = authentication.getName();
		Date fechaActual = new Date();
		Date fechaExpiracion = new Date(fechaActual.getTime() + jwtExpirationInMs);
		
		String token = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(fechaExpiracion)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
		
		return token;
	}
	
	public String getSubjectFromJWT(String token) {
		Claims claims = Jwts.parser()
						.setSigningKey(jwtSecret)
						.parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
	public boolean checkToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		}catch (SignatureException ex) {
			throw new ExampleAppException(HttpStatus.BAD_REQUEST,"Firma JWT no valida");
		}
		catch (MalformedJwtException ex) {
			throw new ExampleAppException(HttpStatus.BAD_REQUEST,"Token JWT no valida");
		}
		catch (ExpiredJwtException ex) {
			throw new ExampleAppException(HttpStatus.BAD_REQUEST,"Token JWT caducado");
		}
		catch (UnsupportedJwtException ex) {
			throw new ExampleAppException(HttpStatus.BAD_REQUEST,"Token JWT no compatible");
		}
		catch (IllegalArgumentException ex) {
			throw new ExampleAppException(HttpStatus.BAD_REQUEST,"La cadena claims JWT esta vacia");
		}
	}
	
	
    public String createToken(Authentication authentication) {
    	
        Algorithm algorithm = Algorithm.HMAC256(this.jwtKey);

        String username = authentication.getPrincipal().toString();
        
        String authorities = "";
        List<String> authoritiesList = new ArrayList<String>();
        for (final GrantedAuthority ga : authentication.getAuthorities()) {
        	authoritiesList.add(ga.getAuthority());
        }
        authorities = String.join(",", authoritiesList);

        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
        return jwtToken;
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.jwtKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token invalid, not Authorized");
        }
    }

    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }
    
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }
}

