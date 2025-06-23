package com.korea.todo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korea.todo.model.UserEntity;
import com.korea.todo.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
//스프링시큐리티의 OAuth2 인증과정을 커스터마이징 하기 위해 사용하는
//OAuth2 사용자 정보 처리 서비스 클래스

//DefaultOAuth2UserService
//사용자 정보를 처리하는 기본 구현 클래스이다.
//github,구글 등 외부 인증 제공자를 통해 로그인하면, 이 클래스가
//access token을 사용해 사용자 정보를 요청하고, OAuth2User 객체로 반환한다.
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {

	@Autowired
	private UserRepository userRepository;
	
	public OAuthUserServiceImpl() {
		super();//부모의 생성자를 호출
	}
	
	//OAuth2 로그인 후 액세스 토큰을 받은 다음, 사용자 정보를 가져오는 핵심 메서드
	//userRequest의 내용
	//accessToken : OAuth2 인증 서버가 발급한 토큰
	//clientRegistration : application.yml에 등록한 github 등 설정 정보
	//additionalParameters : 인증 응답에 포함된 기타 파라미터들
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		//github로부터 받은 액세스 토큰을 이용해 사용자 정보를 가져온다.
		final OAuth2User oAuth2User = super.loadUser(userRequest);
		try {
			//oAuth2User.getAttributes()
			//사용자 정보가 map에 포함되어있다.
			log.info("OAuth2User attributes {} ", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//github에서는 login필드가 사용자의 ID에 해당한다.
		//사용자 정보에서 유저명을 추출
		final String username = (String)
				oAuth2User.getAttributes().get("login");
		
		//어떤 인증 제공자로 로그인 했는지 구분하는 문자열
		//우리의 경우 github라는 문자열이 저장된다.
		final String authProvider = userRequest.getClientRegistration().getClientName();
		
		UserEntity userEntity = null;
		
		//사용자 이름이 db에 없다면 신규 등록
		if(userRepository.existsByUsername(username) == false) {
			//유저가 존재하지 않는다면 새로 만들어줘
			userEntity = UserEntity.builder() // 객체 생성
							.username(username)
							.authProvider(authProvider)
							.build();
			
			//데이테베이스에 저장
			userEntity = userRepository.save(userEntity);
		} else {
			userEntity = userRepository.findByUsername(username);
		}
		
		 log.info("Successfully pulled user info username {} authProvider {}", username, authProvider);
		 // 저장한 유저 정보로 부터 id와 유저의 정보를 ApplicationOAuth2User
		 return new ApplicationOAuth2User(userEntity.getId(), oAuth2User.getAttributes());
	}
}







