package fr.norsys.apidocument.auth.repositories;

import fr.norsys.apidocument.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
    User save(User user);

    User findUserByEmail(String email);
}
