package br.com.ventura.endpoint;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ventura.error.ConstraintViolationException;
import br.com.ventura.error.ResourceNotFoundException;
import br.com.ventura.model.Colaborador;
import br.com.ventura.model.Setor;
import br.com.ventura.repository.ColaboradorRepository;
import br.com.ventura.repository.SetorRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Classe endpoint da RestAPI
 * @author Daniel Ventura
 */
@Api(value="API REST Colaboradores")
@RestController
@RequestMapping("v1")
public class ColaboradorEndpoint {

    private final ColaboradorRepository colaboradorDAO;
    private final SetorRepository setorDAO;
    
    @Autowired
    public ColaboradorEndpoint(ColaboradorRepository colaboradorDAO, SetorRepository setorDAO) {
        this.colaboradorDAO = colaboradorDAO;
        this.setorDAO = setorDAO;
    }

    @GetMapping(path = "protected/colaboradores")
    @ApiOperation(value = "Retorna a lista de colaboradores", response = Colaborador[].class)
    public ResponseEntity<?> listAll() {
        return new ResponseEntity<>(colaboradorDAO.findAll(), HttpStatus.OK);
    }
    
    @GetMapping(path = "protected/colaboradores/paginacao")
    @ApiOperation(value = "Lista de colaboradores com paginação e ordenação no banco. "
    		+ "Exemplo: /paginacao?page=0&size=10&sort=setor.nome,asc "
    		+ "(também lista colaboradores agrupados por setor, quando a ordenação é realizada pelo nome do setor, como no exemplo.)", response = Colaborador[].class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "O número da página, iniciando em 0", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "size", value = "A quantidade de resgistros por página", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "sort", value = "O nome do campo e a direção da ordenação, por exemplo: nome,asc", required = false, dataType = "string", paramType = "query")
      })
    public ResponseEntity<?> listAll(Pageable pageable) {
        return new ResponseEntity<>(colaboradorDAO.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "protected/colaboradores/{id}")
    @ApiOperation(value = "Consulta um Colaborador por Id", response = Colaborador.class)
    public ResponseEntity<?> getColaboradorById(@PathVariable("id") Long id, Authentication authentication) {
        Colaborador colaborador = colaboradorDAO.findById(id).orElseThrow(()-> new ResourceNotFoundException("Colaborador not found for ID: "+id));
        return new ResponseEntity<>(colaborador, HttpStatus.OK);
    }
    
    @GetMapping(path = "protected/colaboradores/findByNome/{nome}")
    @ApiOperation(value = "Consulta um ou mais Colaboradores por um fragmento de seu nome ignorando o case", response = Colaborador[].class)
    public ResponseEntity<?> findColaboradorByNome(@PathVariable String nome){
        return new ResponseEntity<>(colaboradorDAO.findByNomeIgnoreCaseContaining(nome), HttpStatus.OK);
    }

    @PostMapping(path = "admin/colaboradores")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "Insere um Colaborador", response = Colaborador.class)
    public ResponseEntity<?> save(@Valid @RequestBody Colaborador colaborador) {
    	
    	if(colaboradorDAO.findByCpf(colaborador.getCpf())!=null) {
    		throw new ConstraintViolationException("Constraint Violation Exception: cpf "+colaborador.getCpf()+" já cadastrado");
    	}
    	Setor setor;
    	if(colaborador.getSetor()!=null) {
	    	setor = setorDAO.findById(colaborador.getSetor().getId())
	    				.orElseThrow(()-> new ConstraintViolationException("Constraint Violation Exception: Setor com id="
	    				+colaborador.getSetor().getId()+" não existe."));
    	}else {
    		throw new ConstraintViolationException("Informe um id de Setor válido");
    	}
    	
    	colaborador.setSetor(setor);
    	
        return new ResponseEntity<>(colaboradorDAO.save(colaborador),HttpStatus.CREATED);
    }

    @DeleteMapping(path = "admin/colaboradores/{id}")
    @ApiOperation(value = "Deleta um Colaborador")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        verifyIfColaboradorExists(id);
        colaboradorDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PutMapping(path = "admin/colaboradores")
    @ApiOperation(value = "Altera um Colaborador")
    public ResponseEntity<?> update(@Valid @RequestBody Colaborador colaborador) {
        verifyIfColaboradorExists(colaborador.getId());
        colaboradorDAO.save(colaborador);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    private void verifyIfColaboradorExists(Long id){
        if (id==null || !colaboradorDAO.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Colaborador not found for ID: "+id);
        }
    }
}
