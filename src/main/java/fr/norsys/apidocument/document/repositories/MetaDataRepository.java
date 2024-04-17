package fr.norsys.apidocument.document.repositories;

import fr.norsys.apidocument.document.entities.MetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MetaDataRepository extends JpaRepository<MetaData, Long> {
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MetaData m WHERE m.key = 'hash' AND m.value = :hashValue")
    boolean existsHashValue(String hashValue);
}
