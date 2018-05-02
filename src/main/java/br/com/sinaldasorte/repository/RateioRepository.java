package br.com.sinaldasorte.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sinaldasorte.domain.Rateio;

@Repository
public interface RateioRepository extends JpaRepository<Rateio, Long> {

}
