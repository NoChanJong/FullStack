package com.lec.todo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.lec.todo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {
	
	private static final String SECRET_KEY = "rkGU45258GGhiolLO2465TFY5345kGU45258GGhiolLO2465TFY5345";
	
	public String create(UserEntity userEntity) {
		// token의 유효기간설절
	    Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
	    
	    // jwt token을 생성
	    return Jwts.builder()
	    		.signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY))) // header에 저장될 내용 및 서명을 위한 SECRET_KEY
	    		.setSubject(userEntity.getId()) // payload에 들어갈 내용
	    		.setIssuer("Todo Application")  // token 발급자
	    		.setIssuedAt(new Date())        // token 발급일
	    		.setExpiration(expiryDate)      // token 유효기간
	    		.compact();
	}
	
	public String validateAndGetUserId(String token) {
		// 헤더와 페이로드를 setSigningKey로 넘어온 시크릿키를 이용해 서명한 후에
		// token의 서명과 비교, 위변조가 되지 않아다면 페이로드(claims)를 리턴
		// 아니라면 예외를 발생시켜, 사용자정보중 userId가 필요하기 때문에 getBody()를 호출
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(Decoders.BASE64.decode(SECRET_KEY))
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}
}