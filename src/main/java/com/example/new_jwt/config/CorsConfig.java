package com.example.new_jwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // 필터에 꼭 등록을 해줘야만 한다.
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        // 만약 false이면 js에서 응답이 오지 않음
        config.addAllowedOrigin("*"); // 모든 ip에 응답을 허용하겠다
        config.addAllowedHeader("*"); // 모든 header에 응답을 허용하겠다
        config.addAllowedMethod("*"); // 모든 메서드에 응답을 허용하겠다
        source.registerCorsConfiguration("/api/**", config); // /api로 들어오는 모든 것은 이 config 파일을 따라와야만 한다
        return new CorsFilter(source);
    }
}
