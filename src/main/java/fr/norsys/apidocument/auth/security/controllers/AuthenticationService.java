package fr.norsys.apidocument.auth.security.controllers;

import fr.norsys.apidocument.auth.security.JwtService;
import fr.norsys.apidocument.auth.security.models.User;
import fr.norsys.apidocument.auth.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {



    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private  final UserRepository userRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        try {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail());

        var jwtToken = jwtService.generateToken(user);
        System.out.println(user.getId() + "********" + jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .build();

        userRepository.save(user);



        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

}
