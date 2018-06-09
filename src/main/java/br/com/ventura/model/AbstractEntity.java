package br.com.ventura.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Classe responsável por prover um campo id auto-incrementável para
 * as classes @Entity que a estenderem, assim como a implementação
 * dos métodos equals e hashCode tendo como parâmetro este campo id.
 * 
 * @author Daniel Ventura
 */
@MappedSuperclass
public class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 5067043081187335702L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEntity that = (AbstractEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
