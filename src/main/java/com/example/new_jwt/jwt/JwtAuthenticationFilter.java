package com.example.new_jwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.new_jwt.auth.PrincipalDetails;
import com.example.new_jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

/**
 * 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음
 * /login으로 요청해서 username, password 전송하면 (post)
 * UsernamePasswordAuthenticationFilter가 동작을 함
 * form 로그인을 disabled 했기 때문에 얘는 동작을 안하므로 우리가 직접
 * security config 파일에 직접 필터로 등록을 해줘야 함
 *
 *
 * 갔다와서 24강 11분11초 들어야 함
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


//        // 1.username, password 받아서
        try {
//            BufferedReader br = request.getReader();
//
//            String input = null;
//            while ((input = br.readLine()) != null) {
//                System.out.println(input);
//            }

            // ObjectMapper 는 JSON을 파싱해줌
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            // 2. 정상인지 로그인 시도를 해봄 authenticationManager로 로그인 시도를 하면 !!

            // Principal DetailsService가 호출이 됨 (loadUserByUsername) 실행
            // 우리가 만든 JWT 토큰
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetailsService의 loadUserByUsername 함수가 실행된 후 정상이면 authentication이 리턴됨
            // DB에 있는 username과 password가 일치한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 3. authentication 객체가 세션영역에 저장이 됨 => 출력이 되면 로그인이 됐다는 것.
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료됨 : " + principalDetails.getUser().getUsername());

            // return 하면 세션에 저장이 됨
            // 리턴의 이유는 권한 관리를 security가 대신 해주기 때문임
            // 굳이 JWT쓰면서 세션 만들 이유가 없지만, 권한관리에서 편리함의 이점을 이용하기 위해서 세션에 저장하는 것임
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 실행됨
    // JWT를 여기서 request 요청한 사용자에게 response 해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // Hash암호방식
        // Hash방식은 무조건 sign값을 알아야 함
        String jwtToken = JWT.create()
                .withSubject("cos토큰") // 토큰이름
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10))) // 토큰 만료시간
                .withClaim("id", principalDetails.getUser().getId()) // withClaim => 비공개 클레임
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos")); // 서버만 아는 고유의 값

        response.addHeader("Authorization", "Bearer " + jwtToken); // 사용자에게 응답할 response header
    }
}
