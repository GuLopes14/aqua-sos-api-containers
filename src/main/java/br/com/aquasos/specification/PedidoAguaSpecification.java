package br.com.aquasos.specification;

import br.com.aquasos.controller.PedidoAguaController.PedidoAguaFilter;
import br.com.aquasos.model.PedidoAgua;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PedidoAguaSpecification {
    public static Specification<PedidoAgua> withFilters(PedidoAguaFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.usuarioId() != null) {
                predicates.add(cb.equal(root.get("usuario").get("id"), filter.usuarioId()));
            }
            if (filter.pontoDistribuicaoId() != null) {
                predicates.add(cb.equal(root.get("ponto").get("id"), filter.pontoDistribuicaoId()));
            }
            if (filter.quantidadeLitros() != null) {
                predicates.add(cb.equal(root.get("quantidadeLitros"), filter.quantidadeLitros()));
            }
            if (filter.status() != null && !filter.status().isBlank()) {
                predicates.add(cb.equal(cb.upper(root.get("status").as(String.class)), filter.status().toUpperCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}