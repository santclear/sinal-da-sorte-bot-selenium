package br.com.sinaldasorte.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sinaldasorte.domain.Loteria;

@Repository
public interface LoteriaRepository extends JpaRepository<Loteria, Long> {

}
