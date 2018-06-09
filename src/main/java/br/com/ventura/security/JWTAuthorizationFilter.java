package br.com.ventura.security;

import static br.com.ventura.security.SecurityConstants.HEADER_STRING;
import static br.com.ventura.security.SecurityConstants.SECRET;
import static br.com.ventura.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import br.com.ventura.service.CustomUserDetailsService;
import io.jsonwebtoken.Jwts;

/**
 * Implementação do Filtro de Autorização.
 * Esse filtro é invocado no acesso aos serviços da API e tem a responsabilidade de validar 
 * o token informado na requisição.
 *  
 * @author Daniel Ventura
 *
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter{
	
	private final CustomUserDetailsService customUserDetailsService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
			CustomUserDetailsService customUserDetailsService) {
		super(authenticationManager);
		this.customUserDetailsService = customUserDetailsService;
	} 
	
	/**
	 * Método responsável por setar o Token JWT no Contexto de Segurança do Spring 
	 * e dar prosseguimento a requisição. O acesso ainda poderá ser negado adiante, caso falte alguma Role
	 * necessária anotada por @PreAuthorize no serviço requisitado.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//String header = request.getHeader(HEADER_STRING);
		//if(header==null || !header.startsWith(TOKEN_PREFIX)){
		//	chain.doFilter(request, response);
		//	return;
		//}
		
		UsernamePasswordAuthenticationToken usernamePasswordAuth = getAuthenticationToken(request);
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuth);
		chain.doFilter(request, response);
	}
	
	/**
	 * Método responsável por decodificar o token que veio no header da requisição, 
	 * validando se não está expirado e se armazena a informação 
	 * correta do username do usuário
	 * @param request
	 * @return
	 */
	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request){
		String token = request.getHeader(HEADER_STRING);
		if(token==null) return null;
		
		String username = Jwts.parser().setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX,""))
					.getBody()
					.getSubject();

		
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		return username!=null? 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()) : null;
		
	}

}
