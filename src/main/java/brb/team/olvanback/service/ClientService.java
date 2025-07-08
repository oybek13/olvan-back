package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.RegisterClientDto;
import brb.team.olvanback.entity.Client;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.DataNotFoundException;
import brb.team.olvanback.exception.UsernameAlreadyExistException;
import brb.team.olvanback.repository.ClientRepository;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.service.extra.AppService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final AppService appService;

    @Transactional
    public CommonResponse registerClient(RegisterClientDto request) throws JsonProcessingException {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistException("Username already exists: " + request.getUsername());
        }
        User userClient = userRepository.save(User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_CLIENT)
                .isActive(true)
                .build());
        log.info("UserClient created successfully: {}", objectMapper.writeValueAsString(userClient));
        Client client = clientRepository.save(Client.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .enrollType(request.getEnrollType())
                .bio(request.getBio())
                .user(userClient)
                .build());
        log.info("Client created successfully: {}", objectMapper.writeValueAsString(client));
        return CommonResponse.builder()
                .success(true)
                .message("Client created successfully!")
                .build();
    }

    public CommonResponse getClientInfo() {
        String role = appService.getRole();
        String username = appService.getUsername();
        if (role.equals(UserRole.ROLE_CLIENT.name())) {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("User not found with username: " + username));
            Client client = clientRepository.findByUser(user).orElseThrow(() -> new DataNotFoundException("Client not found with username: " + username));
            return CommonResponse.builder()
                    .success(true)
                    .message("Success!")
                    .data(RegisterClientDto.builder()
                            .username(user.getUsername())
                            .fullName(client.getFullName())
                            .phoneNumber(client.getPhoneNumber())
                            .address(client.getAddress())
                            .enrollType(client.getEnrollType())
                            .bio(client.getBio())
                            .build())
                    .build();
        }
        return CommonResponse.builder()
                .success(false)
                .message("Oops, user not Client")
                .build();
    }

    @Transactional
    public CommonResponse editClientProfile(Long id, RegisterClientDto request) throws JsonProcessingException {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Client not found with id: " + id));
        User user = userRepository.findById(client.getUser().getId()).orElseThrow(
                () -> new DataNotFoundException("UserClient not found with id: " + client.getUser().getId()));
        if (!request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new UsernameAlreadyExistException("Username already exists: " + request.getUsername());
            }
        }
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User editedUserClient = userRepository.save(user);
        log.info("UserClient updated: {}", objectMapper.writeValueAsString(editedUserClient));
        client.setFullName(request.getFullName());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setAddress(request.getAddress());
        client.setEnrollType(request.getEnrollType());
        client.setBio(request.getBio());
        Client editedClient = clientRepository.save(client);
        log.info("Client updated: {}", objectMapper.writeValueAsString(editedClient));
        return CommonResponse.builder()
                .success(true)
                .message("Edited successfully!")
                .build();
    }



}
