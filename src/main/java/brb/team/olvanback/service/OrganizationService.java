package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.OrganizationAccountRequest;
import brb.team.olvanback.dto.OrganizationRequest;
import brb.team.olvanback.dto.PageOrgResponse;
import brb.team.olvanback.entity.Contract;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.DataNotFoundException;
import brb.team.olvanback.exception.UserPasswordNotMatchException;
import brb.team.olvanback.exception.UsernameAlreadyExistException;
import brb.team.olvanback.mapper.Mapper;
import brb.team.olvanback.repository.ContractRepository;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.service.extra.MinioService;
import brb.team.olvanback.specs.OrgSpecification;
import brb.team.olvanback.utils.jwt.JwtGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final MinioService minioService;
    private final ContractRepository contractRepository;
    private final Mapper mapper;
    private final JwtGenerator jwtGenerator;
    private final HttpServletRequest request;
    @Value("${minio.url}")
    private String url;
    @Value("${minio.bucket}")
    private String bucket;

    public CommonResponse createOrganization(String organization, MultipartFile contract) throws Exception {
        OrganizationRequest request = objectMapper.readValue(organization, OrganizationRequest.class);
        if (userRepository.existsByUsername(request.getUsername()))
            throw new UsernameAlreadyExistException("Username already exist: " + request.getUsername());
        log.info("Request to create organization: {}", objectMapper.writeValueAsString(request));
        User savedUser = userRepository.save(User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .parentsPhoneNumber(request.getDirectorPhoneNumber())
                .parentsFullName(request.getDirectorFullName())
                .dateBegin(request.getDateBegin())
                .address(request.getAddress())
                .inn(request.getInn())
                .role(UserRole.ROLE_SCHOOL)
                .isActive(request.getIsActive())
                .build());
        log.info("Response after creating organization: {}", objectMapper.writeValueAsString(savedUser));
        uploadContract(contract, savedUser);
        return CommonResponse.builder()
                .success(true)
                .message("Organization created successfully!")
                .build();
    }

    private void uploadContract(MultipartFile file, User user) throws Exception {
        // Generate the object name (e.g., the file name) that will be stored in the bucket
        String objectName = file.getOriginalFilename();
        minioService.sendingTheFileToMinio(file);
        if (contractRepository.existsByUserId(user.getId())) {
            Contract contract = contractRepository.findByUserId(user.getId()).orElse(null);
            contract.setFileName(objectName);
            contract.setFilePath(url + "/" + bucket + "/" + objectName);
            contract.setFileSize(file.getSize());
            Contract updatedContract = contractRepository.save(contract);
            log.warn("Contract updated successfully: {}", objectMapper.writeValueAsString(updatedContract));
        } else {
            Contract entity = contractRepository.save(Contract.builder()
                    .fileName(objectName)
                    .filePath(url + "/" + bucket + "/" + objectName)
                    .fileSize(file.getSize())
                    .user(user)
                    .build());
            log.warn("Contract saved successfully: {}", objectMapper.writeValueAsString(entity));
        }
    }

    public CommonResponse getOneOrganization(Long id) throws JsonProcessingException {
        User user = userRepository.findByIdAndRole(id, UserRole.ROLE_SCHOOL).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + id));
        log.warn("Response from getOneOrganization: {}", objectMapper.writeValueAsString(user));
        Contract contract = contractRepository.findByUserId(user.getId()).orElse(null);
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Mapper.mapOrg(user, contract))
                .build();
    }

    public CommonResponse editOrganization(String data, MultipartFile contract, Long id) throws Exception {
        OrganizationRequest request = objectMapper.readValue(data, OrganizationRequest.class);
        log.info("Request to edit organization: {} \t {}", objectMapper.writeValueAsString(request), id);
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + id));
        if (!user.getUsername().equals(request.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new UsernameAlreadyExistException("Username already exist: " + request.getUsername());
            }
        }
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setParentsPhoneNumber(request.getDirectorPhoneNumber());
        user.setParentsFullName(request.getDirectorFullName());
        user.setDateBegin(request.getDateBegin());
        user.setAddress(request.getAddress());
        user.setInn(request.getInn());
        user.setActive(request.getIsActive());
        User editedUser = userRepository.save(user);
        log.info("Response after editing organization: {}", objectMapper.writeValueAsString(editedUser));
        if (contract != null) {
            uploadContract(contract, editedUser);
        }
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

    public CommonResponse getAll(String address,
                                 String fullName,
                                 String inn,
                                 Boolean active,
                                 int page,
                                 int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Specification<User> spec = Specification.where(OrgSpecification.hasRoleIn(UserRole.ROLE_SCHOOL.name()))
                .and(OrgSpecification.hasAddress(address))
                .and(OrgSpecification.hasFullName(fullName))
                .and(OrgSpecification.hasInn(inn))
                .and(OrgSpecification.isActive(active));
        Page<User> userPage = userRepository.findAll(spec, pageable);
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(PageOrgResponse.builder()
                        .page(page)
                        .size(size)
                        .totalElements(userPage.getTotalElements())
                        .contents(mapper.mapOrgs(userPage.getContent()))
                        .build())
                .build();
    }

    public CommonResponse getOneAccount() {
        Long orgId = jwtGenerator.extractOrgId(request.getHeader("Authorization").substring(7));
        User user = userRepository.findById(orgId).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + orgId));
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Mapper.mapOrgAccount(user))
                .build();
    }

    public CommonResponse updateAccount(Long id, OrganizationAccountRequest organizationAccountRequest) throws JsonProcessingException {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + id));
        if (!user.getUsername().equals(organizationAccountRequest.getUsername())) {
            if (userRepository.existsByUsername(organizationAccountRequest.getUsername())) {
                throw new UsernameAlreadyExistException("Username already exist: " + organizationAccountRequest.getUsername());
            }
        }
        user.setFullName(organizationAccountRequest.getFullName());
        user.setParentsFullName(organizationAccountRequest.getDirectorFullName());
        user.setPhoneNumber(organizationAccountRequest.getPhoneNumber());
        user.setUsername(organizationAccountRequest.getUsername());
        user.setAddress(organizationAccountRequest.getAddress());
        user.setInn(organizationAccountRequest.getInn());
        User savedUser = userRepository.save(user);
        log.info("Organization itself updated successfully: {}", objectMapper.writeValueAsString(savedUser));
        return CommonResponse.builder()
                .success(true)
                .message("Updated!")
                .build();
    }

    public CommonResponse changePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + id));
        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            log.info("Organization itself changed password!");
            return CommonResponse.builder()
                    .success(true)
                    .message("Password changed successfully!")
                    .build();
        } else {
            throw new UserPasswordNotMatchException("Current password is incorrect: ".concat(currentPassword));
        }
    }
}
