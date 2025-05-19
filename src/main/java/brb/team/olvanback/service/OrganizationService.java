package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.OrganizationRequest;
import brb.team.olvanback.dto.PageResponse;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.DataNotFoundException;
import brb.team.olvanback.exception.UsernameAlreadyExistException;
import brb.team.olvanback.mapper.Mapper;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.specs.UserSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    public CommonResponse createOrganization(OrganizationRequest request) throws JsonProcessingException {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new UsernameAlreadyExistException("Username already exist: " + request.getUsername());
        log.info("Request to create organization: {}", objectMapper.writeValueAsString(request));
        User savedUser = userRepository.save(User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .inn(request.getInn())
                .role(request.getRole())
                .isActive(request.isActive())
                .build());
        log.info("Response after creating organization: {}", objectMapper.writeValueAsString(savedUser));
        return CommonResponse.builder()
                .success(true)
                .message("Organization created successfully!")
                .build();
    }

    public CommonResponse getOneOrganization(Long id) throws JsonProcessingException {
        List<UserRole> roles = new ArrayList<>();
        roles.add(UserRole.ROLE_SCHOOL);
        roles.add(UserRole.ROLE_EDUCATIONAL_CENTER);
        User user = userRepository.findByIdAndRoleIn(id, roles).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + id));
        log.warn("Response from getOneOrganization: {}", objectMapper.writeValueAsString(user));
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Mapper.map(user))
                .build();
    }

    public CommonResponse editOrganization(OrganizationRequest request, Long id) throws JsonProcessingException {
        log.info("Request to edit organization: {} \t {}", objectMapper.writeValueAsString(request), id);
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + id));
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setInn(request.getInn());
        user.setRole(request.getRole());
        user.setActive(request.isActive());
        User editedUser = userRepository.save(user);
        log.info("Response after editing organization: {}", objectMapper.writeValueAsString(editedUser));
        return CommonResponse.builder()
                .success(true)
                .message("Organization edited successfully!")
                .build();
    }

    public CommonResponse deleteOrganization(Long id) {
        log.info("Request to delete organization: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + id));
        userRepository.delete(user);
        log.warn("Organization deleted with id: {}", id);
        return CommonResponse.builder()
                .success(true)
                .message("Organization deleted successfully!")
                .build();
    }

    public CommonResponse getAll(String username,
                                 String fullName,
                                 String inn,
                                 Boolean active,
                                 int page,
                                 int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<String> roles = new ArrayList<>();
        roles.add(UserRole.ROLE_SCHOOL.name());
        roles.add(UserRole.ROLE_EDUCATIONAL_CENTER.name());
        Specification<User> spec = Specification.where(UserSpecification.hasRoleIn(roles))
                .and(UserSpecification.hasUsername(username))
                .and(UserSpecification.hasFullName(fullName))
                .and(UserSpecification.hasInn(inn))
                .and(UserSpecification.isActive(active));
        Page<User> userPage = userRepository.findAll(spec, pageable);
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(PageResponse.builder()
                        .page(page)
                        .size(size)
                        .totalElements(userPage.getTotalElements())
                        .contents(Mapper.map(userPage.getContent()))
                        .build())
                .build();
    }


}
