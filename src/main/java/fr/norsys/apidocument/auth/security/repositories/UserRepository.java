package fr.norsys.apidocument.auth.security.repositories;

import fr.norsys.apidocument.auth.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
    User save(User user);
}
