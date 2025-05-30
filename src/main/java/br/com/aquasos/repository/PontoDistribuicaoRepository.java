package br.com.aquasos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.aquasos.model.PontoDistribuicao;

@Repository
public interface PontoDistribuicaoRepository extends JpaRepository<PontoDistribuicao, Long>, JpaSpecificationExecutor<PontoDistribuicao> {
    
}
