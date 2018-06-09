package br.com.ventura;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import java.util.Arrays;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.ventura.model.Colaborador;
import br.com.ventura.model.Setor;
import br.com.ventura.repository.ColaboradorRepository;

/**
 * Testes de integração do endpoint Colaborador
 * @author Daniel Ventura
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ColaboradorEndpointTest {
	
    @Autowired
    private TestRestTemplate restTemplate;
    
    @LocalServerPort
    private int port;
    
    @MockBean
    private ColaboradorRepository colaboradorDao;
    
    @Autowired
    private MockMvc mockMvc;
    
    private HttpEntity<Void> protectedHeader;
    private HttpEntity<Void> adminHeader;

    @Before
    public void configProtectedHeaders() {
        String str = "{\"username\":\"user\",\"password\":\"pass\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.protectedHeader = new HttpEntity<>(headers);
    }
    @Before
    public void configAdminHeaders() {
        String str = "{\"username\":\"admin\",\"password\":\"pass\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.adminHeader = new HttpEntity<>(headers);
    }

    @Before
    public void setup() {
   	
    	Colaborador colaboradorMock1 = 
    			new Colaborador("64338925078","Maria José", "(21) 99777-8228", "maria@gmail.com", getSetorMock());
    	colaboradorMock1.setId(1L);
    	
    	Colaborador colaboradorMock2 = 
    			new Colaborador("41066376000","João Mário", "(21) 99333-1128", "joao@gmail.com", getSetorMock());
    	colaboradorMock2.setId(2L);
    	
    	Iterable<Colaborador> colaboradores = Arrays.asList(colaboradorMock1, colaboradorMock2);
    	
        BDDMockito.when(colaboradorDao.findById(colaboradorMock1.getId())).thenReturn(Optional.of(colaboradorMock1));
        BDDMockito.when(colaboradorDao.findAll()).thenReturn(colaboradores);
    }
    
    @Test
    public void listColaboradoresWithoutTokenShouldReturnStatusCode403() {
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/colaboradores/", GET, null, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }
    
    
    @Test
    public void getColaboradorByIdWithoutTokenShouldReturnStatusCode403() {
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/colaboradores/1", GET, null, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }
    
    @Test
    public void listColaboradoresWhenTokenIsCorrectShouldReturnStatusCode200() {
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/colaboradores/", GET, protectedHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getColaboradorByIdWhenTokenIsCorrectShouldReturnStatusCode200() {
        ResponseEntity<Colaborador> response = restTemplate.exchange("/v1/protected/colaboradores/1", GET, protectedHeader, Colaborador.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
    
    @Test
    public void getColaboradorByIdWhenTokenIsCorrectAndColaboradorDoesNotExistShouldReturnStatusCode404() {
        ResponseEntity<Colaborador> response = restTemplate.exchange("/v1/protected/colaboradores/-1", GET, protectedHeader, Colaborador.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }
    
    @Test
    public void deleteWhenUserHasRoleAdminAndColaboradorExistsShouldReturnStatusCode200() {
        BDDMockito.doNothing().when(colaboradorDao).deleteById(1L);
        ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/colaboradores/1", DELETE, adminHeader, String.class);
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }
    
    @Test
    public void deleteWhenUserHasRoleAdminAndColaboradorDoesNotExistShouldReturnStatusCode404() throws Exception {
        String token = adminHeader.getHeaders().get("Authorization").get(0);
        BDDMockito.doNothing().when(colaboradorDao).deleteById(-1L);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/colaboradores/{id}", -1L).header("Authorization",token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    
    
    @Test
    public void deleteWhenUserDoesNotHaveRoleAdminShouldReturnStatusCode403() throws Exception {
        String token = protectedHeader.getHeaders().get("Authorization").get(0);
        BDDMockito.doNothing().when(colaboradorDao).deleteById(1L);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/colaboradores/{id}", 1L).header("Authorization",token))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    
    @Test
    public void createWhenNomeColaboradorIsNullShouldReturnStatusCode400BadRequest() throws Exception {
    	
    	Colaborador colaboradorMock1 = 
    			new Colaborador("64338925078", null, "(21) 99777-8228", "maria@gmail.com", getSetorMock());
        
        BDDMockito.when(colaboradorDao.save(colaboradorMock1)).thenReturn(colaboradorMock1);
        
        ResponseEntity<String> response = restTemplate
        		.exchange("/v1/admin/colaboradores/", POST, new HttpEntity<>(colaboradorMock1, adminHeader.getHeaders()), String.class);
        
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
        Assertions.assertThat(response.getBody()).contains("fieldMessage", "Nome obrigatório");
    }
    

	private Setor getSetorMock() {
		Setor setor = new Setor("Setor A");
    	setor.setId(1L);
		return setor;
	}

	
}
