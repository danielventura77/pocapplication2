package br.com.ventura.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.ventura.service.CustomUserDetailsService;

/**
 * Classe de configuração do Spring Security <br/>
 * É invocada na inicialização da API e tem as seguintes responsabilidades: <br/>
 * 	Habilitar o SpringSecurity com a annotation @EnableWebSecurity <br/>
 * 	Habilitar a segurança no nível dos métodos da API com a annotation @EnableGlobalMethodSecurity(prePostEnabled = true)
 *
 * @author Daniel Ventura
 *
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	public SecurityConfig(CustomUserDetailsService customUserDetailsService){
		this.customUserDetailsService = customUserDetailsService;
	}
	
	/**
	 * Método de configuração de quais Roles serão exigidas do usuário para acessar cada path da API. <br/>
	 * Também são configurados aqui os Filtros de Autenticação e Autorização para o token JWT
	*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable().authorizeRequests()
			.antMatchers(HttpMethod.POST,"/sign_up").permitAll()
			.antMatchers("/*/protected/**").hasRole("USER")
			.antMatchers("/*/admin/**").hasRole("ADMIN")
			.and()
			.addFilter(new JWTAuthenticationFilter(authenticationManager()))
			.addFilter(new JWTAuthorizationFilter(authenticationManager(), customUserDetailsService));
	}
	
	
	/**
	 * Define o Encoder/Decoder da senha do usuário da aplicação armazenada no banco de dados
	 */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

}
