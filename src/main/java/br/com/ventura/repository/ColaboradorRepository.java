package br.com.ventura.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.ventura.model.Colaborador;

/**
 * 
 * Interface utilizada para o acesso ao banco de dados
 * @author Daniel Ventura
 *
 */
public interface ColaboradorRepository  extends PagingAndSortingRepository<Colaborador, Long>{
	
	List<Colaborador> findByNomeIgnoreCaseContaining(String nome);
	Colaborador findByCpf(String cnpj);
	
	

}
