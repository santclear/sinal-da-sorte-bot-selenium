package br.com.sinaldasorte.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Sorteio implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false)
	private Concurso concurso;
	
	private int numero;
	
	@Column(nullable = false, length = 199)
	private String numerosSorteados;
	
	private String cidadesGanhadoresPremioPrincipal;
	
	private String ufsGanhadoresPremioPrincipal;
	
	@OneToMany(mappedBy = "sorteio")
	private List<Rateio> rateios = new LinkedList<>();
	
	public Sorteio() {}
	
	public Sorteio(Long id, Concurso concurso, int numero, String numerosSorteados,
			String cidadesGanhadoresPremioPrincipal, String ufsGanhadoresPremioPrincipal) {
		this.id = id;
		this.concurso = concurso;
		this.numero = numero;
		this.numerosSorteados = numerosSorteados;
		this.cidadesGanhadoresPremioPrincipal = cidadesGanhadoresPremioPrincipal;
		this.ufsGanhadoresPremioPrincipal = ufsGanhadoresPremioPrincipal;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Concurso getConcurso() {
		return concurso;
	}

	public void setConcurso(Concurso concurso) {
		this.concurso = concurso;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNumerosSorteados() {
		return numerosSorteados;
	}

	public void setNumerosSorteados(String numerosSorteados) {
		this.numerosSorteados = numerosSorteados;
	}

	public List<Rateio> getRateios() {
		return rateios;
	}

	public void setRateios(List<Rateio> rateios) {
		this.rateios = rateios;
	}

	public String getCidadesGanhadoresPremioPrincipal() {
		return cidadesGanhadoresPremioPrincipal;
	}

	public void setCidadesGanhadoresPremioPrincipal(String cidadesGanhadoresPremioPrincipal) {
		this.cidadesGanhadoresPremioPrincipal = cidadesGanhadoresPremioPrincipal;
	}

	public String getUfsGanhadoresPremioPrincipal() {
		return ufsGanhadoresPremioPrincipal;
	}

	public void setUfsGanhadoresPremioPrincipal(String ufsGanhadoresPremioPrincipal) {
		this.ufsGanhadoresPremioPrincipal = ufsGanhadoresPremioPrincipal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sorteio other = (Sorteio) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
