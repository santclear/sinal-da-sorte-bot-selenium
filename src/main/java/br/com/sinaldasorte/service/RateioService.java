package br.com.sinaldasorte.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sinaldasorte.domain.Rateio;
import br.com.sinaldasorte.repository.RateioRepository;
import br.com.sinaldasorte.service.exceptions.ObjetoNaoEncontradoException;

@Service
public class RateioService {
	
	@Autowired
	private RateioRepository repo;
	
	public Rateio encontre(Long id) {
		Optional<Rateio> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjetoNaoEncontradoException("Objeto não encontrado! Id: " + id + ", Tipo: " + Rateio.class.getName()));
	}
	
	public List<Rateio> insiraTodos(List<Rateio> objs) {
		return	repo.saveAll(objs);
	}
	
	public List<Rateio> procureTodos() {
		return repo.findAll();
	}
}
