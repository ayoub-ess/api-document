package fr.norsys.apidocument.document.utils;

import fr.norsys.apidocument.document.models.Document;
import fr.norsys.apidocument.document.models.MetaData;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DocumentSpecifications {
    public static Specification<Document> findByTypeOrNameOrCreationDate(String searchName) {
        return (root, query, cb) -> {
            Predicate predicateType = cb.equal(root.get("type"), searchName);
            Predicate predicateFileName = cb.like(root.get("name"), "%" + searchName + "%");
            Join<Document, MetaData> metadataJoin = root.join("metadata");
            Predicate predicateKey = cb.equal(metadataJoin.get("key"), "size");
            Predicate predicateValue = cb.greaterThan(metadataJoin.get("value"), searchName);
            try {
                LocalDate date = LocalDate.parse(searchName);
                Predicate predicateCreationDate = cb.greaterThan(root.get("creationDate").as(LocalDate.class), date);
                return predicateCreationDate;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return cb.or(predicateType, predicateFileName,cb.and(predicateKey, predicateValue));
            }
        };
    }

}
