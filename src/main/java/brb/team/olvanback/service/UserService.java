package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.SignInRequest;
import brb.team.olvanback.dto.SignUpRequest;
import brb.team.olvanback.entity.Organization;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.DataNotFoundException;
import brb.team.olvanback.repository.OrganizationRepository;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.utils.jwt.JwtGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;
    private final OrganizationRepository organizationRepository;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtGenerator jwtGenerator, PasswordEncoder passwordEncoder, OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.passwordEncoder = passwordEncoder;
        this.organizationRepository = organizationRepository;
    }

    public CommonResponse signUp(SignUpRequest sign) {
        userRepository.save(User.builder()
                .username(sign.getUsername())
                .password(passwordEncoder.encode(sign.getPassword()))
                .role(sign.getRole())
                .isActive(true)
                .build());
        log.warn("User created successfully!");
        return CommonResponse.builder()
                .success(true)
                .message("Sign up successful")
                .build();
    }

    public CommonResponse signIn(SignInRequest sign) {
        List<String> roles = new ArrayList<>();
        User user = userRepository.findByUsername(sign.getUsername()).orElseThrow(() -> new DataNotFoundException("Username not found: " + sign.getUsername()));
        roles.add(user.getRole().toString());
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(sign.getUsername(), sign.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserRole role = user.getRole();
        if (role == UserRole.ROLE_SUPER_ADMIN) {
            return CommonResponse.builder()
                    .success(true)
                    .message("Sign in successful")
                    .data(jwtGenerator.generateToken(sign.getUsername(), roles, user.getId()))
                    .build();
        }
        if (role == UserRole.ROLE_CLIENT) {
            return CommonResponse.builder()
                    .success(true)
                    .message("Sign in successful")
                    .data(jwtGenerator.generateToken(sign.getUsername(), roles, null))
                    .build();
        }
        Organization organization = organizationRepository.findByUser(user).orElseThrow(() -> new DataNotFoundException("Organization not found !"));
        return CommonResponse.builder()
                .success(true)
                .message("Sign in successful")
                .data(jwtGenerator.generateToken(sign.getUsername(), roles, organization.getId()))
                .build();
    }


}
