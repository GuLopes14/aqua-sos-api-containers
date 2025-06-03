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
            if (filter.quantidadeLitros() != null) {
                predicates.add(cb.equal(root.get("quantidadeLitros"), filter.quantidadeLitros()));
            }
            if (filter.endereco() != null && !filter.endereco().isBlank()) {
                predicates.add(cb.like(cb.upper(root.get("endereco")), "%" + filter.endereco().toUpperCase() + "%"));
            }
            if (filter.nivelUrgencia() != null && !filter.nivelUrgencia().isBlank()) {
                predicates.add(cb.equal(cb.upper(root.get("nivelUrgencia").as(String.class)), filter.nivelUrgencia().toUpperCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}