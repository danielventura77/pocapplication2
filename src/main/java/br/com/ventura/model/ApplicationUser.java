package br.com.ventura.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Classe do modelo para mapeamento do Usuário da Aplicação
 * @author Daniel Ventura
 */
@Entity(name="user")
public class ApplicationUser extends AbstractEntity {
	
	private static final long serialVersionUID = 5417470964519947109L;
	
	@NotNull
    @Column(length=10, unique = true)
    private String username;
	
    @NotNull
    @Column(length=60)
    private String password;
    
    @NotNull
    @Column(length=60)
    private String name;
    
    @NotNull
    private boolean admin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
