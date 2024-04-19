package fr.norsys.apidocument.auth.repositories;

import fr.norsys.apidocument.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
    User save(User user);
    User findUserByEmail(String email);
}
