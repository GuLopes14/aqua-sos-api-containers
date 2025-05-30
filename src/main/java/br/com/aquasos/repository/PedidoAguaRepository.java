package br.com.aquasos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.aquasos.model.PedidoAgua;

@Repository
public interface PedidoAguaRepository extends JpaRepository<PedidoAgua, Long>, JpaSpecificationExecutor<PedidoAgua> {

}
