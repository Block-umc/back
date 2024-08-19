package com.example.block.utillity;

import com.example.block.dto.JwtTokenDto;
import com.example.block.global.constants.Constants;
import com.example.block.security.enums.ERole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil implements InitializingBean {
    @Value("asdfasdfasdfasdrt123a1241qwetqpknapilugipk1j23k4lhj1adfkj9312kl3nasdkflj9Nvlnyasdfakljf891023jhlkasdf91nkhldf91kjlas90dfjl1k29azalikdjf912kjl3lasd9f0j1kl239asdjlkf")
    private String secretKey;
    @Value("${jwt.access-token-expire-period}")
    private Integer accessTokenExpirePeriod;
    @Value("${jwt.refresh-token-expire-period}")
    @Getter
    private Integer refreshTokenExpirePeriod;

    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtTokenDto generateTokens(Integer id, ERole role) {
        return new JwtTokenDto(
                generateToken(id, role, accessTokenExpirePeriod * 1000),
                generateToken(id, null, refreshTokenExpirePeriod * 1000));
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String generateToken(Integer id, ERole role, Integer expirePeriod) {
        Claims claims = Jwts.claims();
        claims.put(Constants.USER_ID_CLAIM_NAME, id);
        if (role != null) {
            claims.put(Constants.USER_ROLE, role);
        }

        return Jwts.builder()
                .setHeaderParam(Header.JWT_TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirePeriod))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}
