package br.com.sinaldasorte.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sinaldasorte.domain.Concurso;
import br.com.sinaldasorte.repository.ConcursoRepository;
import br.com.sinaldasorte.service.exceptions.ObjetoNaoEncontradoException;
import br.com.sinaldasorte.service.exceptions.ProcessamentoInternoException;

@Service
public class ConcursoService {
	
	@Autowired
	private ConcursoRepository repo;
	
	public Concurso encontre(Long id) {
		Optional<Concurso> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjetoNaoEncontradoException("Objeto não encontrado! Id: " + id + ", Tipo: " + Concurso.class.getName()));
	}
	
	public Integer ultimoNumeroConcurso(Long idLoteria) {
		return repo.ultimoNumeroConcurso(idLoteria);
	}
	
	public Concurso insira(Concurso obj) {
		Integer maxNumeroConcursoLoteriaBd = this.repo.ultimoNumeroConcurso(obj.getLoteria().getId());
		Integer difEntreNumConcursoNovoENumConcursoBd = obj.getNumero() - maxNumeroConcursoLoteriaBd;
//		if(difEntreNumConcursoNovoENumConcursoBd == 1) 
		return	repo.save(obj);
//		else throw new ProcessamentoInternoException(
//				"Para salvar um novo concurso é necessário que a diferença entre o "
//				+ "NÚMERO DO NOVO CONCURSO e o NÚMERO DO ÚLTIMO CONCURSO NO BD sejam iguais a 1.\n"
//				+ "Número do novo concurso:\t\t"+ obj.getNumero() +"\n"
//				+ "Número do último concurso no BD:\t"+ maxNumeroConcursoLoteriaBd + "\n"
//				+ "Diferença entre os dois:\t\t"+ difEntreNumConcursoNovoENumConcursoBd);
	}
	
	public List<Concurso> procureTodos() {
		return repo.findAll();
	}
}
