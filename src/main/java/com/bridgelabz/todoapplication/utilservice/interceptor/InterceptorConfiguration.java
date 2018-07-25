package com.bridgelabz.todoapplication.utilservice.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bridgelabz.todoapplication.utilservice.interceptor.LoggerInterceptor;

/**
 * @author yuga
 * @since 17/07/2018
 * <p><b>Configure interceptor class to add the interceptors in the todo application.
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer{
	 
	 @Autowired
		LoggerInterceptor logInterceptor;

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(logInterceptor);
		}
}
