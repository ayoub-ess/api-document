package fr.norsys.apidocument.auth.security.service;


import fr.norsys.apidocument.auth.security.models.User;
import fr.norsys.apidocument.auth.security.models.UserRole;

public interface UserService {
    User addNewUser(String username , String password, String email);
    void deleteRoleFromUser(String username , UserRole userRole);

    User loadUserByEmail(String email);
}
