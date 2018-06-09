package br.com.ventura.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.ventura.model.Colaborador;
import br.com.ventura.model.Setor;
import br.com.ventura.repository.SetorRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Classe endpoint da RestAPI
 * @author Daniel Ventura
 */
@Api(value="API REST Setores")
@RestController
@RequestMapping("v1")
public class SetorEndpoint {
	
    private final SetorRepository setorDAO;
    
    @Autowired
    public SetorEndpoint(SetorRepository setorDAO) {
        this.setorDAO = setorDAO;
    }
    
    @ApiOperation(value = "Listar Colaboradores agrupados por Setor", response = Setor[].class)
    @GetMapping(path = "protected/setores", produces="application/json")
    public @ResponseBody Iterable<Setor> listAll() {
    	
    	List<Setor> setores = setorDAO.findAll();
    	for(Setor setor : setores) {
    		for(Colaborador colaborador : setor.getColaboradores()) {
    			colaborador.setId(null);
    			colaborador.setCpf(null);
    			colaborador.setTelefone(null);
    		}
    	}
    	
        return setores;
    }


}
