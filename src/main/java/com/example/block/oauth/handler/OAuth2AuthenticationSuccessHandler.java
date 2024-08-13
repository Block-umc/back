package com.example.block.oauth.handler;

import com.example.block.domain.User;
import com.example.block.dto.JwtTokenDto;
import com.example.block.global.constants.Constants;
import com.example.block.oauth.info.KakaoUserInfo;
import com.example.block.repository.UserRepository;
import com.example.block.security.enums.ERole;
import com.example.block.security.service.CustomUserDetailService;
import com.example.block.utillity.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String REDIRECT_URL = "http://13.209.114.87:8080/api/v1/auth/oauth2/kakao?accessToken=%s&refreshToken=%s&providerId=%d";

    private final JwtUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        System.out.println("OAuth2AuthenticationSuccessHandler: Authentication 들어옴");

        Long serialId = ((Number) kakaoUserInfo.getAttributes().get(Constants.KAKAO_ID)).longValue();

        User user = userRepository.findBySerialId(serialId).orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다"));


        //토큰 발급
        JwtTokenDto tokens = jwtUtil.generateTokens(user.getId(), ERole.USER);

        if (!user.getIsNewUser() || !tokens.refreshToken().equals(user.getRefreshToken())) {
            userRepository.updateRefreshTokenAndLoginStatus(user.getId(), tokens.refreshToken(), true);
        }

        String redirectUrI = String.format(REDIRECT_URL, tokens.accessToken(), tokens.refreshToken(), serialId);
        getRedirectStrategy().sendRedirect(request, response, redirectUrI);



        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"accessToken\":\"" + tokens.accessToken() + "\", \"refreshToken\":\"" + tokens.refreshToken() + "\"}");

    }
}
