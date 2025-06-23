package com.korea.todo.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

//authentication에는 UserEntity의 id가 없다
//이를 해결하기 위해 OAuth2User를 구현하는 새 클래스를 작성해야한다.
//이 클래스는 OAuth2User와 UserEntity사이의 다리같은 역할을 한다.

//OAuth2User
//OAuth 2.0 로그인 과정을 처리할 때 사용하는 인터페이스
//어플리케이션이 외부 인증 제공자를 통해 사용자 인증을 처리할 때, 해당 사용자 정보를 나타내는 역할
//OAuth2User인터페이스는 OAuth2.0을 통해 로그인한 사용자의 프로필 정보와 권한을 제공하는데 사용된다.
//사용자가 인증 제공자에게서 로그인한 후, 스프링 시큐리티는 OAuth2User객체를 생성하여
//어플리케이션에 전달하고, 이를 통해 로그인된 사용자의 정보를 다룬다.
public class ApplicationOAuth2User implements OAuth2User {
   
   private String id; //사용자 식별자
   private Collection<? extends GrantedAuthority> authorities; //시큐리티에서 인식할 수 있는 권한 정보
   private Map<String, Object> attributes; //OAuth2 제공자로부터 받아온 사용자 정보 전체 Map
   
   public ApplicationOAuth2User(String id, Map<String,Object> attributes) {
         this.id = id;
         this.attributes = attributes;
         //Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
         //스프링 시큐리티의 권한 부여를 위한 컬렉션을 생성하는 코드
         //new SimpleGrantedAuthority("ROLE_USER") : 사용자의 권한을 표현하는 클래스
         //"ROLE_USER" : 기본적인 사용자 권한 이름
         this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
   }

   @Override
   public Map<String, Object> getAttributes() {
      // TODO Auto-generated method stub
      return this.attributes;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      // TODO Auto-generated method stub
      return this.authorities;
   }

   @Override
   public String getName() {
      // TODO Auto-generated method stub
      return this.id; //name 대신 id를 반환한다.
   }
   
   
   
   
   
   
   
   
   
   

}






