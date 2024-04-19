package fr.norsys.apidocument.document.repositories;

import fr.norsys.apidocument.document.models.Permission;
import fr.norsys.apidocument.document.models.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByType(PermissionType type);
}
