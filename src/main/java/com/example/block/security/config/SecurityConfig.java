package com.example.block.security.config;

import com.example.block.global.constants.Constants;
import com.example.block.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.example.block.oauth.handler.OAuth2FailureHandler;
import com.example.block.oauth.service.KakaoMemberDetailService;
import com.example.block.security.filter.GlobalLoggerFilter;
import com.example.block.security.filter.JwtAuthenticationFilter;
import com.example.block.security.filter.JwtExceptionFilter;
import com.example.block.security.handler.jwt.JwtAuthEntryPoint;
import com.example.block.security.handler.signout.CustomLogoutResultHandler;
import com.example.block.security.handler.signout.CustomSignOutProcessHandler;
import com.example.block.security.service.CustomUserDetailService;
import com.example.block.utillity.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder customPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private final CustomSignOutProcessHandler customSignOutProcessHandler;
    private final CustomLogoutResultHandler customSignOutResultHandler;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;
    private final KakaoMemberDetailService KakaoMemberDetailService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Bean
    protected SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(registry ->
                        registry
                                .requestMatchers(Constants.NO_NEED_AUTH_URLS.toArray(String[]::new)).permitAll()
                                //현재는 permitAll로 설정해놓았지만 추후에는 권한을 부여해야함
                                .anyRequest().authenticated()

                )
                .logout(configurer ->
                        configurer
                                .logoutUrl("/api/v1/auth/sign-out")
                                .addLogoutHandler(customSignOutProcessHandler)
                                .logoutSuccessHandler(customSignOutResultHandler)
                )
                .exceptionHandling(configurer ->
                        configurer
                                .authenticationEntryPoint(jwtAuthEntryPoint)
                )
                .oauth2Login(oAuth2Login ->{
                    oAuth2Login.userInfoEndpoint(userInfoEndpointConfig ->
                            userInfoEndpointConfig.userService(KakaoMemberDetailService)
                    );
                    oAuth2Login
                            .successHandler(oAuth2AuthenticationSuccessHandler)
                            .failureHandler(oAuth2FailureHandler);
                })

                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtUtil, customUserDetailService),
                        LogoutFilter.class)
                .addFilterBefore(new JwtExceptionFilter(),
                        JwtAuthenticationFilter.class)
                .addFilterBefore(new GlobalLoggerFilter(),
                        JwtExceptionFilter.class)
                .getOrBuild();
    }


}
