package fr.norsys.apidocument.auth.security.service;

import fr.norsys.apidocument.auth.security.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private UserService userService;

    public UserDetailServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.loadUserByEmail(email);
        if(user==null) throw new RuntimeException("user not found" + email);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
//                .authorities(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                .build();
        return userDetails;
    }
}
