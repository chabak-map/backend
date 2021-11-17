package com.sikhye.chabak;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// !!!!!!!!!! /login 페이지가 계속 뜨는 경우 무시 ( https://sncap.tistory.com/849 )
// TODO: 설정파일 지정했음에도 불구하고 다른 패키지에 있으면 인식을 못하는 이유 ?
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
			.antMatchers("/**")
			.anyRequest();

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/**")
			.permitAll();

		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().disable();
	}

}
