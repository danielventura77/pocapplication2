package br.com.ventura.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Classe do modelo para mapeamento do Sector
 * @author Daniel Ventura
 *
 */
@Entity
public class Setor extends AbstractEntity{

	private static final long serialVersionUID = 6946964818624815453L;
	
	@Column(length=60, unique=true)
	private String nome;
	
	@OneToMany(mappedBy = "setor", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Colaborador> colaboradores;
	
	public Setor() {
	}
	
	public Setor(String nome) {
		super();
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Colaborador> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(List<Colaborador> colaboradores) {
		this.colaboradores = colaboradores;
	}

	@Override
	public String toString() {
		return this.id+ " - "+this.nome;
	}
	

}
