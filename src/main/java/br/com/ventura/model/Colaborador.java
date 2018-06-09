package br.com.ventura.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * Classe do modelo para mapeamento do Colaborador
 * 
 * @author Daniel Ventura
 *
 */

@Entity
@JsonInclude(Include.NON_NULL)
public class Colaborador extends AbstractEntity{
	
	private static final long serialVersionUID = 782043514582443730L;

	@NotNull(message="CPF obrigatório")
	@CPF(message="CPF inválido")
	@Size(max=11, message="O CPF deve ter 11 dígitos")
	@Column(length=11, unique=true)
	private String cpf;
	
	@NotNull(message="Nome obrigatório")
	@Size(min=3, max=60, message="O nome deve ter entre 3 e 60 caracteres")
	@Column(length=60)
	private String nome;
	
	@NotNull(message="Telefone obrigatório")
	@Pattern(regexp="\\(\\d{2}\\) \\d{4,5}-\\d{4}$", message="O telefone deve ter o formato (DD) DDDDD-DDDD.")
	@Column(length=15)
	private String telefone;
	
	@NotNull(message="Email obrigatório")
	@Email(message = "Email inválido")
	@Size(max=50, message="O Email deve ter até 50 caracteres")
	@Column(length=50)
	private String email;
	
	@ManyToOne
    @JoinColumn(name = "fk_id_setor", nullable=false)
	@JsonBackReference
	private Setor setor;
	
	@Transient
	private String descricaoSetor;
			
	public Colaborador() {
	}
	
	public Colaborador(String cpf, String nome, String telefone, String email, Setor setor) {
		super();
		this.cpf = cpf;
		this.nome = nome;
		this.telefone = telefone;
		this.email = email;
		this.setor = setor;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public String getDescricaoSetor() {
		return this.setor.toString();
	}

	public void setDescricaoSetor(String descricaoSetor) {
		this.descricaoSetor = descricaoSetor;
	}
	

}
