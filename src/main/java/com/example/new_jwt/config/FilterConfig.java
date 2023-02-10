package com.example.new_jwt.config;

import com.example.new_jwt.filter.MyFilter1;
import com.example.new_jwt.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    /**
     * 필터 컨피그에서 FilterResgistrationBean을 새로 만들어서 MyFilter를 등록해준다
     * return bean을 통해서 MyFilter1의 내용이 필터체인에 걸리게 된다.
     * 여기서 주의해야할 점은 여기서 만든 Filter는 시큐리티 필터체인에 직접 거는것은 아니다!!!!!!!!!!!!!!!
     * 게다가 실행순서도 시큐리티 필터체인이 실행 되고 나서 내 CustomFilter가 실행되게 된다
     *
     * 만약 시큐리티 필터체인보다 가장 먼저 실행되게 하고 싶다!!!!면
     * SecurityConfig에서 Filter에
     * .addFilterBefore(new MyFilter1(), SecurityContextPersistenceFilter.class)
     * 처럼 Before로 걸면 된다.
     * @return
     */
    @Bean
    public FilterRegistrationBean<MyFilter1> filter1() {
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());

        bean.addUrlPatterns("/*"); // 전체주소에 관해서 필터를 걸어버리기
        bean.setOrder(1); // 낮은 번호일 수록 가장 먼저 실행 됨
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());

        bean.addUrlPatterns("/*"); // 전체주소에 관해서 필터를 걸어버리기
        bean.setOrder(0); // 낮은 번호일 수록 가장 먼저 실행 됨
        return bean;
    }
}
