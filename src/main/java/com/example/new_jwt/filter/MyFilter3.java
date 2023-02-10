package com.example.new_jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰 : cos
        // 토큰을 만들어서 동작하기 위해서는 시큐리티 필터체인보다
        // 빨리 동작해야만 한다.

        /**
         * 우리가 이제 만들어야 할 것은 토큰! 여기서의 "cos"를 만들어야 함
         * id,pw 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답해준다
         * 요청할 때 마다 header에 Authorization에 value값으로 토큰을 가지고옴
         * 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지 검증을 해야한다 (cos인지를 보는 것이 아니라)
         * RSA, HS256을 통해서 내 토큰이 맞는지 확인해야 한다.
         */
        if (req.getMethod().equals("POST")) {
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);
            if (headerAuth.equals("cos")) {
                chain.doFilter(req, res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }
        }
        
        chain.doFilter(request, response); // 필터체인에 다시 넣어야 함
    }
}
