package br.com.sinaldasorte.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.sinaldasorte.domain.Concurso;

@Repository
public interface ConcursoRepository extends JpaRepository<Concurso, Long> {
	@Transactional(readOnly = true)
	@Query("SELECT max(concurso.numero) FROM Concurso concurso inner join concurso.loteria loteria WHERE loteria.id = ?1")
	Integer maxNumeroConcursoLoteria(Long idLoteria);
}
