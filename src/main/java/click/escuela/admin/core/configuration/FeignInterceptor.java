package click.escuela.admin.core.configuration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

@Configuration
public class FeignInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String accessToken = request == null ? StringUtils.EMPTY : request.getHeader(HttpHeaders.AUTHORIZATION);
		System.out.println("=================Feign Interceptor AccessToken: " + accessToken);
		requestTemplate.header(HttpHeaders.AUTHORIZATION, accessToken);
	}
	@Bean
	public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
	      return new SpringFormEncoder(new SpringEncoder(messageConverters));
	 }
}
