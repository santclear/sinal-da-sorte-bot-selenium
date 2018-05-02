package br.com.sinaldasorte.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sinaldasorte.domain.Loteria;
import br.com.sinaldasorte.repository.LoteriaRepository;
import br.com.sinaldasorte.service.exceptions.ObjetoNaoEncontradoException;

@Service
public class LoteriaService {
	
	@Autowired
	private LoteriaRepository repo;
	
	public Loteria encontre(Long id) {
		Optional<Loteria> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjetoNaoEncontradoException("Objeto não encontrado! Id: " + id + ", Tipo: " + Loteria.class.getName()));
	}
	
	public List<Loteria> procureTodos() {
		return repo.findAll();
	}
}
