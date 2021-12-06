package com.sikhye.chabak.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final JwtMemberInterceptor jwtMemberInterceptor;
	private final JwtAdminInterceptor jwtAdminInterceptor;

	public WebConfig(JwtMemberInterceptor jwtMemberInterceptor, JwtAdminInterceptor jwtAdminInterceptor) {
		this.jwtMemberInterceptor = jwtMemberInterceptor;
		this.jwtAdminInterceptor = jwtAdminInterceptor;
	}

	// 인터셉터 등록
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtMemberInterceptor)            // 인터셉터 등록
			.order(1)                                            // 인터셉터 호출 우선순위
			.addPathPatterns("/**")                              // 인터셉터 적용할 URI 패턴
			.excludePathPatterns("/error", "/members/**", "/auth/**"); // 인터셉터에서 제외할 URI 패턴

		registry.addInterceptor(jwtAdminInterceptor)
			.order(2)
			.addPathPatterns("/places/**")
			.excludePathPatterns("/places/comments/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:3000")
			.allowedMethods("GET", "POST", "OPTIONS", "PUT", "PATCH");
	}

}
