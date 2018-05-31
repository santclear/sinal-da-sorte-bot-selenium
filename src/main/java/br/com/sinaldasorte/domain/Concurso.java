package br.com.sinaldasorte.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Concurso implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Loteria loteria;
	
	@Column(nullable = false)
	private Integer numero;
	
	@Temporal(TemporalType.DATE)
	@Column(length = 10)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEEE, dd/MM/yyyy", locale = "pt", timezone = "Brazil/East")
	private Calendar dataDoSorteio;
	
	@Column(precision = 15, scale = 2)
	private BigDecimal arrecadacaoTotal;
	
	@Column(length = 1000)
	private String localizacaoSorteio;
	
	@Column(precision = 15, scale = 2)
	private BigDecimal estimativaDePremioParaOProximoConcurso;
	
	@Column(precision = 15, scale = 2)
	private BigDecimal acumuladoEspecial;
	
	@OneToMany(mappedBy = "concurso")
	private List<Sorteio> sorteios = new LinkedList<>();
	
	public Concurso() {}
	
	public Concurso(Long id, Loteria loteria, Integer numero, Calendar dataDoSorteio, BigDecimal arrecadacaoTotal, String localizacaoSorteio,
		BigDecimal estimativaDePremioParaOProximoConcurso, BigDecimal acumuladoEspecial) {
		super();
		this.id = id;
		this.loteria = loteria;
		this.numero = numero;
		this.dataDoSorteio = dataDoSorteio;
		this.arrecadacaoTotal = arrecadacaoTotal;
		this.localizacaoSorteio = localizacaoSorteio;
		this.estimativaDePremioParaOProximoConcurso = estimativaDePremioParaOProximoConcurso;
		this.acumuladoEspecial = acumuladoEspecial;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Loteria getLoteria() {
		return loteria;
	}

	public void setLoteria(Loteria loteria) {
		this.loteria = loteria;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Calendar getDataDoSorteio() {
		return dataDoSorteio;
	}

	public void setDataDoSorteio(Calendar dataDoSorteio) {
		this.dataDoSorteio = dataDoSorteio;
	}

	public BigDecimal getArrecadacaoTotal() {
		return arrecadacaoTotal;
	}

	public void setArrecadacaoTotal(BigDecimal arrecadacaoTotal) {
		this.arrecadacaoTotal = arrecadacaoTotal;
	}

	public String getLocalizacaoSorteio() {
		return localizacaoSorteio;
	}

	public void setLocalizacaoSorteio(String localizacaoSorteio) {
		this.localizacaoSorteio = localizacaoSorteio;
	}

	public BigDecimal getEstimativaDePremioParaOProximoConcurso() {
		return estimativaDePremioParaOProximoConcurso;
	}

	public void setEstimativaDePremioParaOProximoConcurso(BigDecimal estimativaDePremioParaOProximoConcurso) {
		this.estimativaDePremioParaOProximoConcurso = estimativaDePremioParaOProximoConcurso;
	}

	public BigDecimal getAcumuladoEspecial() {
		return acumuladoEspecial;
	}

	public void setAcumuladoEspecial(BigDecimal acumuladoEspecial) {
		this.acumuladoEspecial = acumuladoEspecial;
	}

	public List<Sorteio> getSorteios() {
		return sorteios;
	}

	public void setSorteios(List<Sorteio> sorteios) {
		this.sorteios = sorteios;
	}
		
	public void addSorteio(Sorteio sorteio) {
		sorteio.setConcurso(this);
		this.sorteios.add(sorteio);
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
		Concurso other = (Concurso) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
