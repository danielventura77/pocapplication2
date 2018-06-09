package br.com.ventura.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.ventura.model.ApplicationUser;
import br.com.ventura.repository.ApplicationUserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService{
	
	
	private final ApplicationUserRepository applicationUserDAO;
	
	@Autowired
	public CustomUserDetailsService(ApplicationUserRepository applicationUserDAO) {
		this.applicationUserDAO = applicationUserDAO;
	}

	/**
	 * Este método tem o objetivo de retornar o objeto User do SpringSecurity setando-o com o username, password e roles do usuário da aplicação 
	 * recuperado do banco de dados. Caso o usuário retornado do banco não case as suas credenciais (username e password) com as informadas
	 * no login, será retornado "status code 401 - Unauthorized" como resposta ao login e nenhum token será gerado.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		ApplicationUser applicationUser = loadApplicationUserByUsername(username);
		
		if(applicationUser==null){
			new UsernameNotFoundException("User not found");
		}
		
        List<GrantedAuthority> authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
        List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");
		
		return new User(applicationUser.getUsername(), applicationUser.getPassword(), applicationUser.isAdmin() ? authorityListAdmin : authorityListUser);
		
	}
	
	/**
	 * Este método tem o objetivo de consultar no banco de dados da API o usuário, 
	 * senha e o seu perfil de acesso
	 * @param username
	 * @return
	 */
	public ApplicationUser loadApplicationUserByUsername(String username){
		return applicationUserDAO.findByUsername(username);
	}

}
