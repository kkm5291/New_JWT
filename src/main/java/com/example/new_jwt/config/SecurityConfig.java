package com.example.new_jwt.config;

import com.example.new_jwt.filter.MyFilter1;
import com.example.new_jwt.filter.MyFilter3;
import com.example.new_jwt.jwt.JwtAuthenticationFilter;
import com.example.new_jwt.jwt.JwtAuthorizationFilter;
import com.example.new_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


// https://github.com/spring-projects/spring-security/issues/10822 참고
@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final UserRepository userRepository;

    /**
     * 시큐리티 필터체인임 여기가
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
//                .addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class)
                //.addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class) // securityFilterChain에 어떤것이 있는지
                // 알아야 사용이 가능하기 때문에 공부할 것.
                // 그러나 필터를 이렇게 쓸일은 거의 없음.
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다
                .and()
                .formLogin().disable() // jwt 서버니까 필요 없음
                .httpBasic().disable() // header에 Authorization : ID, PW를 담아서 보내주는 것

                // 이 방법은 ID 와 PW가 암호화가 되지 않는다는 단점이 있음
                // 이를 암호화 하기 위해선 https를 사용해야 한다
                // 우리는 Authorization 헤더에 JWT를 넣을 것이다!!!!!
                // 노출이 되더라도 ID와 PW가 아니기 때문에 위험부담이 적다
                // 토큰을 달고 요청하는 방식이 Bearer라는 방식이다. (Not Basic)
                // Bearer Token엔 유효시간도 있음
                .apply(new MyCustomDsl()) // CORS 허용하지 않음
                .and()
                .authorizeRequests(authorize -> authorize.antMatchers("/api/v1/user/**")
                        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                        .antMatchers("/api/v1/manager/**")
                        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                        .antMatchers("/api/v1/admin/**")
                        .access("hasRole('ROLE_ADMIN')")
                        .anyRequest().permitAll())


                .build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }
}