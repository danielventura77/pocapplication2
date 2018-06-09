package br.com.ventura.security;

import static br.com.ventura.security.SecurityConstants.*;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ventura.model.ApplicationUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
/**
 * Implementação do Filtro de Autenticação.
 * Esse filtro é chamado no serviço de login (/login) e tem a responsabilidade de autenticar o usuário mediante 
 * suas credenciais (username e password) e gerar um Token JWT de acesso
 * 
 * @author daniel ventura
 *
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	/**
	 * Tem por objetivo realizar a tentativa de autenticação.
	 * É criado um objeto ApplicationUser a partir do conteúdo informado no corpo da requisição POST
	 * do serviço de login (/login). O body da requisição é preenchido no formato 
	 * {"username": "username", "password": "password"}
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			ApplicationUser applicationUser = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(applicationUser.getUsername(), applicationUser.getPassword()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Esse método é chamado caso a autenticação no serviço de login for bem sucedida.
	 * Tem o objetivo de criar um Token JWT (Json Web Token), com um tempo de expiração definido via configuração.
	 * O Token JWT é codificado com algoritmo HS256 utilizando uma chave (SECRET) conhecida apenas pela API.
	 * O token gerado é retornado no header da resposta de login. É utilizado o tipo de autenticação Bearer.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		ZonedDateTime expirationTimeUTC = ZonedDateTime.now(ZoneOffset.UTC).plus(EXPIRATION_TIME,ChronoUnit.MILLIS);
		String token = Jwts.builder().setSubject(((User)authResult.getPrincipal()).getUsername())
				.setExpiration(Date.from(expirationTimeUTC.toInstant()))
				.signWith(SignatureAlgorithm.HS256, SECRET)
				.compact();
		
		response.getWriter().write(token);
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
	}
	
}
