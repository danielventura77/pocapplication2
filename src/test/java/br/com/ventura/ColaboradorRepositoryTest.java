package br.com.ventura;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ventura.model.Colaborador;
import br.com.ventura.model.Setor;
import br.com.ventura.repository.ColaboradorRepository;
import br.com.ventura.repository.SetorRepository;

/**
 * Testes Unitários do Repositório (DAO) Colaborador
 * @author Daniel Ventura.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ColaboradorRepositoryTest {
    @Autowired
    private ColaboradorRepository colaboradorDao;
    
    @Autowired
    private SetorRepository setorDao;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    

    @Test
    public void createShouldPersistData() {
    	
    	Setor setor = persisteSetor("Setor A");
    	
    	Colaborador colaborador = 
    			new Colaborador("20706037090","João Paulo", "(21) 99999-8888", "joao@gmail.com", setor);
        
        this.colaboradorDao.save(colaborador);
        assertThat(colaborador.getId()).isNotNull();
        assertThat(colaborador.getCpf()).isEqualTo("20706037090");
        assertThat(colaborador.getNome()).isEqualTo("João Paulo");
        assertThat(colaborador.getTelefone()).isEqualTo("(21) 99999-8888");
        assertThat(colaborador.getEmail()).isEqualTo("joao@gmail.com");
        assertThat(colaborador.getSetor().getNome()).isEqualTo("Setor A");
    }

    
    @Test
    public void deleteShouldRemoveData() {
    	
    	Setor setor = persisteSetor("Setor A");
    	
    	Colaborador colaborador = 
    			new Colaborador("64338925078","Maria José", "(21) 99777-8228", "maria@gmail.com", setor);
    	
        this.colaboradorDao.save(colaborador);
        this.colaboradorDao.deleteById(colaborador.getId());
        assertThat(!colaboradorDao.findById(colaborador.getId()).isPresent());
    }
    
    
    @Test
    public void updateShouldChangeAndPersistData() {
    	
    	Setor setor = persisteSetor("Setor A");
    	
    	Colaborador colaborador = 
    			new Colaborador("64338925078","Maria José", "(21) 99777-8228", "maria@gmail.com", setor);
        this.colaboradorDao.save(colaborador);
        
        colaborador.setNome("Maria Silva");
        colaborador.setEmail("silva@gmail.com");
        
        this.colaboradorDao.save(colaborador);
        
        colaborador = this.colaboradorDao.findById(colaborador.getId()).get();
        assertThat(colaborador.getNome()).isEqualTo("Maria Silva");
        assertThat(colaborador.getEmail()).isEqualTo("silva@gmail.com");
    }
    
    @Test
    public void findAllShouldFindAllData() {
    	Setor setor = persisteSetor("Setor A");
    	
    	Colaborador colaborador1 = 
    			new Colaborador("64338925078","Maria José", "(21) 99777-8228", "maria@gmail.com", setor);
    	Colaborador colaborador2 = 
    			new Colaborador("36930552038","Paulo Monteiro", "(21) 93337-8828", "paulo@gmail.com", setor);
    	
        this.colaboradorDao.save(colaborador1);
        this.colaboradorDao.save(colaborador2);

        Iterable<Colaborador> colaboradores = colaboradorDao.findAll();
        assertThat(((Collection<?>)colaboradores).size()).isEqualTo(2);
    }
    
    @Test
    public void findByIdIShouldFindByIdData() {
    	Setor setor = persisteSetor("Setor A");
    	
    	Colaborador colaborador = 
    			new Colaborador("64338925078","Maria José", "(21) 99777-8228", "maria@gmail.com", setor);
    	
        this.colaboradorDao.save(colaborador);

        colaborador = colaboradorDao.findById(colaborador.getId()).get();
        assertThat(colaborador.getId()).isNotNull();
        assertThat(colaborador.getCpf()).isEqualTo("64338925078");
        assertThat(colaborador.getNome()).isEqualTo("Maria José");
        assertThat(colaborador.getTelefone()).isEqualTo("(21) 99777-8228");
        assertThat(colaborador.getEmail()).isEqualTo("maria@gmail.com");
        assertThat(colaborador.getSetor().getNome()).isEqualTo("Setor A");
    }
    
    
    @Test
    public void createSameCpfShouldThrowConstraintViolationException() {
    	
        thrown.expect(DataIntegrityViolationException.class);
        
        Setor setor = persisteSetor("Setor A");
        
    	Colaborador colaborador1 = 
    			new Colaborador("64338925078","Maria José", "(21) 99777-8228", "maria@gmail.com", setor);
    	Colaborador colaborador2 = 
    			new Colaborador("64338925078","Maria José", "(21) 99777-8228", "maria@gmail.com", setor);
    	
        this.colaboradorDao.save(colaborador1);
        this.colaboradorDao.save(colaborador2);
    }
    
    @Test
    public void createWhenNomeIsNullShouldThrowConstraintViolationException() {
    	
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Nome obrigatório");
        
        Setor setor = persisteSetor("Setor A");
        
    	Colaborador colaborador = 
    			new Colaborador("64338925078", null, "(21) 99777-8228", "maria@gmail.com", setor);
        
        this.colaboradorDao.save(colaborador);
    }
    
    @Test
    public void createWhenEmailIsNullShouldThrowConstraintViolationException() {
    	
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Email obrigatório");
        
        Setor setor = persisteSetor("Setor A");
        
    	Colaborador colaborador = 
    			new Colaborador("64338925078", "Maria José", "(21) 99777-8228", null, setor);
        
        this.colaboradorDao.save(colaborador);
    }
    
    @Test
    public void createWhenTelefoneIsNullShouldThrowConstraintViolationException() {
    	
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Telefone obrigatório");
        
        Setor setor = persisteSetor("Setor A");
        
    	Colaborador colaborador = 
    			new Colaborador("64338925078", "Maria José", null, "maria@gmail.com", setor);
        
        this.colaboradorDao.save(colaborador);
    }
    
    @Test
    public void createWhenCPFIsNullShouldThrowConstraintViolationException() {
    	
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("CPF obrigatório");
        
        Setor setor = persisteSetor("Setor A");
        
    	Colaborador colaborador = 
    			new Colaborador(null, "Maria José", "(21) 99777-8228", "maria@gmail.com", setor);
        
        this.colaboradorDao.save(colaborador);
    }
    
    @Test
    public void createColaboradorWhenSetorIsNullShouldThrowDataIntegrityViolationException() {
    	
        thrown.expect(DataIntegrityViolationException.class);
                
    	Colaborador colaborador = 
    			new Colaborador("64338925078", "Maria José", "(21) 99777-8228", "maria@gmail.com", null);
        
        this.colaboradorDao.save(colaborador);
    }
    
    @Test
    public void createWhenEmailIsNotValidShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Email inválido");
        
        Setor setor = persisteSetor("Setor A");
        
    	Colaborador colaborador = 
    			new Colaborador("64338925078", "Maria José", "(21) 99777-8228", "mariagmail.com", setor);
    	
        this.colaboradorDao.save(colaborador);
    }
   
    @Test
    public void createWhenCpfIsNotValidShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("CPF inválido");
        
        Setor setor = persisteSetor("Setor A");
        
    	Colaborador colaborador = 
    			new Colaborador("00000000000", "Maria José", "(21) 99777-8228", "maria@gmail.com", setor);
    	
        this.colaboradorDao.save(colaborador);
    }
    
    @Test
    public void createWhenTelefoneIsNotValidShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("O telefone deve ter o formato (DD) DDDDD-DDDD.");
        
        Setor setor = persisteSetor("Setor A");
        
    	Colaborador colaborador = 
    			new Colaborador("64338925078", "Maria José", "(21) 99228", "maria@gmail.com", setor);
    	
        this.colaboradorDao.save(colaborador);
    }
    
    @Test
    public void createWhenNomeIsNotValidShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("O nome deve ter entre 3 e 60 caracteres");
        
        Setor setor = persisteSetor("Setor A");
        
    	Colaborador colaborador = 
    			new Colaborador("64338925078", "Ma", "(21) 99777-8228", "maria@gmail.com", setor);
    	
        this.colaboradorDao.save(colaborador);
    }


	private Setor persisteSetor(String nomeSetor) {
		Setor setor = new Setor();
    	setor.setId(null);
    	setor.setNome(nomeSetor);
    	this.setorDao.save(setor);
		return setor;
	}


}
