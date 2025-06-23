package com.korea.todo.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//SimpleUrlAuthenticationSuccessHandler
//스프링 시큐리티에서 인증 성공 후 처리를 담당하는 클래스

@Component
@Slf4j
@AllArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	//OAuth2 로그인 성공 후 JWT토큰을 발급하고 응답으로 반환하는 로직을
	//수행하는 클래스
	
	
	//스프링 시큐리티가 OAuth2 로그인에 성공한 직후 호출하는 메서드
	//Authentication객체에 로그인된 사용자 정보가 포함되어 있다.
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		//TokenProvider 객체를 생성
		TokenProvider tokenProvider = new TokenProvider();
		//JWT토큰 발급
		String token = tokenProvider.create(authentication);
		
		log.info("token {}",token);
		
		// 프론트엔드로 리다이렉트 할 수는 있겠으나 토큰을 전달할 순 없다.
		// 프론트엔드는 백엔드가 리다이렉트 하면서 전달하는 토큰을 받아주는 기능이 필요하다.
		response.sendRedirect("http://localhost:3000/sociallogin?token="+token);
	}
}






