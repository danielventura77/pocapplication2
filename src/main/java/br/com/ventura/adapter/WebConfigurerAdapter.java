package br.com.ventura.adapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Esta classe tem o objetivo de configurar o tamanho e o número da página default,
 * ou seja, quando os parâmetros de paginação (tamanho da página e número da página) 
 * não são informados numa consulta paginada, que valores serão usados.
 * 
 * @author Daniel Ventura
 */
@Configuration
public class WebConfigurerAdapter implements WebMvcConfigurer{
	
	@Value("${page.size}")
	private int pageSize;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        PageableHandlerMethodArgumentResolver phmar = new PageableHandlerMethodArgumentResolver();
        phmar.setFallbackPageable(PageRequest.of(0, pageSize));
        resolvers.add(phmar);
	}
}
