package br.com.sinaldasorte.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sinaldasorte.domain.Sorteio;
import br.com.sinaldasorte.repository.SorteioRepository;
import br.com.sinaldasorte.service.exceptions.ObjetoNaoEncontradoException;

@Service
public class SorteioService {
	
	@Autowired
	private SorteioRepository repo;
	
	public Sorteio encontre(Long id) {
		Optional<Sorteio> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjetoNaoEncontradoException("Objeto não encontrado! Id: " + id + ", Tipo: " + Sorteio.class.getName()));
	}
	
	public List<Sorteio> insiraTodos(List<Sorteio> objs) {
		return	repo.saveAll(objs);
	}
	
	public List<Sorteio> procureTodos() {
		return repo.findAll();
	}
}
