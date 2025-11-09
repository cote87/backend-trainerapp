package com.reffocase.backend.trainerapp.backend_trainerapp.specifications;

import org.springframework.data.jpa.domain.Specification;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Trainer;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class TrainerSpecification {

    public static Specification<Trainer> filter(
            String name,
            String lastname,
            String documentNumber,
            String thematic,
            String province,
            Boolean enabled) {
        return (Root<Trainer> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Predicate p = cb.conjunction();

            if (enabled != null) {
                p = cb.and(p, cb.equal(root.get("enabled"), enabled));
            }

            if (name != null && !name.isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (lastname != null && !lastname.isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("lastname")), "%" + lastname.toLowerCase() + "%"));
            }

            if (documentNumber != null && !documentNumber.isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("documentNumber")), "%" + documentNumber.toLowerCase() + "%"));
            }

            if (province != null && !province.isBlank()) {
                Join<Object, Object> provinceJoin = root.join("province", JoinType.LEFT);
                p = cb.and(p, cb.like(cb.lower(provinceJoin.get("name")), "%" + province.toLowerCase() + "%"));
            }

            if (enabled != null && enabled && thematic != null && !thematic.isBlank()) {
                Join<Object, Object> thematicsJoin = root.join("thematics", JoinType.LEFT);
                p = cb.and(p, cb.like(cb.lower(thematicsJoin.get("name")), "%" + thematic.toLowerCase() + "%"));
            }

            query.distinct(true);
            return p;
        };
    }
}
