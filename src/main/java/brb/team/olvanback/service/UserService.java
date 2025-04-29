package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.SignInRequest;
import brb.team.olvanback.dto.SignUpRequest;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.utils.jwt.JwtGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtGenerator jwtGenerator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    public CommonResponse signUp(SignUpRequest sign) {
        User savedUser = userRepository.save(User.builder()
                .username(sign.getUsername())
                .password(passwordEncoder.encode(sign.getPassword()))
                .role(sign.getRole())
                .isActive(true)
                .build());
        return CommonResponse.builder()
                .success(true)
                .message("Sign up successful")
                .data(savedUser)
                .build();
    }

    public CommonResponse signIn(SignInRequest sign) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(sign.getUsername(), sign.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return CommonResponse.builder()
                .success(true)
                .message("Sign in successful")
                .data(jwtGenerator.generateToken(sign.getUsername()))
                .build();
    }


}
