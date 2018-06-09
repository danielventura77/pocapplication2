package br.com.ventura.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ventura.model.ApplicationUser;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser,Long>{
	
	ApplicationUser findByUsername(String username);

}
