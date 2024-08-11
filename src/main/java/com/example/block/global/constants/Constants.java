package com.example.block.global.constants;

import java.util.List;

public class Constants {

    public static final String API_PREFIX = "/api/v1";
    public static String USER_ID_CLAIM_NAME = "uid";
    public static String BEARER_PREFIX = "Bearer ";
    public static String AUTHORIZATION_HEADER = "Authorization";
    public static String USER_ROLE = "ROLE_USER";
    public static final String REGION_TYPE = "H";
    public static final String USER_NICKNAME_PREFIX = "USER_";

    public static final String PLATFORM_GENERAL = "general";


    public static final String KAKAO_ACCOUNT = "kakao_account";
    public static final String KAKAO_EMAIL = "email";
    public static final String KAKAO_ID = "id"; // ID를 위한 상수
    public static final String KAKAO_PREFIX = "낯선 ";
    public static final String PLATFORM_KAKAO = "kakao";

    public static List<String> NO_NEED_AUTH_URLS = List.of(
            "/v3/api-docs.html/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/api/v1/auth/login",
            "/api/v1/auth/sign-up",
            "/api/v1/auth/sign-in",
            "/api/v1/auth/oauth2/kakao",
            "/oauth2/**",
            "/login/oauth2/**",
            "/favicon.ico",
            "/health"
    );
}
