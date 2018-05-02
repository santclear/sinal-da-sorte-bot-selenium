package br.com.sinaldasorte.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sinaldasorte.domain.Sorteio;

@Repository
public interface SorteioRepository extends JpaRepository<Sorteio, Long> {

}
