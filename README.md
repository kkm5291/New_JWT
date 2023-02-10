# 세션 로그인
1. 유저네임, 패스워드 로그인 정상
2. 서버쪽 세션 ID 생성, 클라이언트 쿠키 세션ID를 응답
3. 요청할 때 마다 쿠키값 세션ID를 항상 들고 서버쪽으로 요청하기 때문에 <br>
서버는 세션 ID가 유효한지 판단해서 유효하면 인증이 필요한 페이지로 접근하게 된다.


# JWT 로그인
1. 유저네임, 패스워드 로그인 정상
2. JWT토큰을 생성
3. 클라이언트쪽으로 JWT토큰을 응답
4. 요청할 때 마다 JWT토큰을 가지고 요청
5. 서버는 JWT토큰이 유효한지를 판단함 (필터를 통해서 해결해야 함)

## 내가 만든 JWT토큰
    - Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.
    eyJzdWIiOiJjb3PthqDtgbAiLCJpZCI6MSwiZXhwIjoxNjc1OTI4MjU4LCJ1c2VybmFtZSI6ImEifQ.
    8FZiDZBBND5EFH7LMNRSuztUegoW_6Z6bKwEcjfdhEcrw4NFGoBS_5zKSQwjbnEaDxNEFZLrZ00U5JCy2MTMEw