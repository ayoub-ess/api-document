package fr.norsys.apidocument.auth.security.service;

import fr.norsys.apidocument.auth.models.User;
import fr.norsys.apidocument.auth.models.UserRole;
import fr.norsys.apidocument.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User addNewUser(String username, String password, String email) {
        User user = userRepository.findByEmail(email);
        if(user!=null) throw new RuntimeException("this user already exist");

        return userRepository.save(User.builder()
                        .username(username)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                .build());
    }



    @Override
    public void deleteRoleFromUser(String username, UserRole userRole) {

    }

    @Override
    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }




}
