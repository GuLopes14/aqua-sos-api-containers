package br.com.aquasos.specification;

import br.com.aquasos.model.PontoDistribuicao;
import br.com.aquasos.controller.PontoDistribuicaoController.PontoDistribuicaoFilter;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class PontoDistribuicaoSpecification {

    public static Specification<PontoDistribuicao> withFilters(PontoDistribuicaoFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.nome() != null && !filter.nome().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + filter.nome().toLowerCase() + "%"));
            }
            if (filter.endereco() != null && !filter.endereco().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("endereco")), "%" + filter.endereco().toLowerCase() + "%"));
            }
            if (filter.cidade() != null && !filter.cidade().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("cidade")), "%" + filter.cidade().toLowerCase() + "%"));
            }
            if (filter.capacidadeTotalLitros() != null) {
                predicates.add(cb.equal(root.get("capacidadeTotalLitros"), filter.capacidadeTotalLitros()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}