package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.OrganizationAccountRequest;
import brb.team.olvanback.dto.OrganizationRequest;
import brb.team.olvanback.dto.PageOrgResponse;
import brb.team.olvanback.entity.Contract;
import brb.team.olvanback.entity.Organization;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.DataNotFoundException;
import brb.team.olvanback.exception.UserPasswordNotMatchException;
import brb.team.olvanback.exception.UsernameAlreadyExistException;
import brb.team.olvanback.mapper.Mapper;
import brb.team.olvanback.repository.ContractRepository;
import brb.team.olvanback.repository.OrganizationRepository;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.service.extra.MinioService;
import brb.team.olvanback.specs.OrganizationSpecification;
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
    private final OrganizationRepository organizationRepository;
    @Value("${minio.url}")
    private String url;
    @Value("${minio.bucket}")
    private String bucket;

    public CommonResponse createOrganization(String organization, MultipartFile contract) throws Exception {
        OrganizationRequest request = objectMapper.readValue(organization, OrganizationRequest.class);
        if (userRepository.existsByUsername(request.getUsername()))
            throw new UsernameAlreadyExistException("Username already exist: " + request.getUsername());
        log.info("Request to create user: {}", objectMapper.writeValueAsString(request));
        User savedUser = userRepository.save(User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_SCHOOL)
                .isActive(request.getIsActive())
                .build());
        log.info("Response after creating user: {}", objectMapper.writeValueAsString(savedUser));
        log.info("Request to create organization: {}", objectMapper.writeValueAsString(request));
        Organization savedOrg = organizationRepository.save(Organization.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .directorPhoneNumber(request.getDirectorPhoneNumber())
                .directorFullName(request.getDirectorFullName())
                .dateBegin(request.getDateBegin())
                .address(request.getAddress())
                .innOrPinfl(request.getInn())
                .user(savedUser)
                .build());
        log.info("Response after creating organization: {}", objectMapper.writeValueAsString(savedOrg));
        uploadContract(contract, savedOrg);
        return CommonResponse.builder()
                .success(true)
                .message("Organization created successfully!")
                .build();
    }

    private void uploadContract(MultipartFile file, Organization organization) throws Exception {
        // Generate the object name (e.g., the file name) that will be stored in the bucket
        String objectName = file.getOriginalFilename();
        minioService.sendingTheFileToMinio(file);
        if (contractRepository.existsByOrgId(organization.getId())) {
            Contract contract = contractRepository.findByOrgId(organization.getId()).orElse(null);
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
                    .orgId(organization.getId())
                    .build());
            log.warn("Contract saved successfully: {}", objectMapper.writeValueAsString(entity));
        }
    }

    public CommonResponse getOneOrganization(Long id) throws JsonProcessingException {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + id));
        User user = userRepository.findById(organization.getUser().getId()).orElse(null);
        log.warn("Response from getOneOrganization: {}", objectMapper.writeValueAsString(organization));
        Contract contract = contractRepository.findByOrgId(organization.getId()).orElse(null);
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Mapper.mapOrg(user, organization, contract))
                .build();
    }

    public CommonResponse editOrganization(String data, MultipartFile contract, Long id) throws Exception {
        OrganizationRequest request = objectMapper.readValue(data, OrganizationRequest.class);
        log.info("Request to edit organization: {} \t {}", objectMapper.writeValueAsString(request), id);
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + id));
        User user = userRepository.findById(organization.getUser().getId()).orElseThrow(() -> new DataNotFoundException("User not found with id: " + organization.getUser().getId()));
        if (!user.getUsername().equals(request.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new UsernameAlreadyExistException("Username already exist: " + request.getUsername());
            }
        }
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(request.getIsActive());
        organization.setFullName(request.getFullName());
        organization.setPhoneNumber(request.getPhoneNumber());
        organization.setDirectorPhoneNumber(request.getDirectorPhoneNumber());
        organization.setDirectorFullName(request.getDirectorFullName());
        organization.setDateBegin(request.getDateBegin());
        organization.setAddress(request.getAddress());
        organization.setInnOrPinfl(request.getInn());
        User editedUser = userRepository.save(user);
        log.info("Response after editing user: {}", objectMapper.writeValueAsString(editedUser));
        Organization editedOrganization = organizationRepository.save(organization);
        log.info("Response after editing organization: {}", objectMapper.writeValueAsString(editedOrganization));
        if (contract != null) {
            uploadContract(contract, editedOrganization);
        }
        return CommonResponse.builder()
                .success(true)
                .message("Organization edited successfully!")
                .build();
    }

    public CommonResponse deleteOrganization(Long id) {
        log.info("Request to delete organization: {}", id);
        Organization organization = organizationRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Organization not found with id: " + id));
        organizationRepository.delete(organization);
        log.warn("Organization deleted with id: {}", id);
        User user = userRepository.findById(organization.getUser().getId()).orElseThrow(
                () -> new DataNotFoundException("UserOrganization not found with id: " + organization.getUser().getId()));
        userRepository.delete(user);
        log.warn("UserOrganization deleted with id: {}", organization.getUser().getId());
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

        Specification<Organization> spec = Specification
                .where(OrganizationSpecification.hasFullName(fullName))
                .and(OrganizationSpecification.hasAddress(address))
                .and(OrganizationSpecification.hasInn(inn))
                .and(OrganizationSpecification.hasUserActive(active));

        Page<Organization> orgPage = organizationRepository.findAll(spec, pageable);

        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(PageOrgResponse.builder()
                        .page(page)
                        .size(size)
                        .totalElements(orgPage.getTotalElements())
                        .contents(mapper.mapOrgs(orgPage.getContent()))
                        .build())
                .build();
    }


    public CommonResponse getOneAccount() {
        Long orgId = jwtGenerator.extractOrgId(request.getHeader("Authorization").substring(7));
        Organization organization = organizationRepository.findById(orgId).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + orgId));
        User user = userRepository.findById(organization.getUser().getId()).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + orgId));
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Mapper.mapOrgAccount(user, organization))
                .build();
    }

    public CommonResponse updateAccount(Long id, OrganizationAccountRequest organizationAccountRequest) throws JsonProcessingException {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + id));
        User user = userRepository.findById(organization.getId()).orElse(null);
        if (!user.getUsername().equals(organizationAccountRequest.getUsername())) {
            if (userRepository.existsByUsername(organizationAccountRequest.getUsername())) {
                throw new UsernameAlreadyExistException("Username already exist: " + organizationAccountRequest.getUsername());
            }
        }
        user.setUsername(organizationAccountRequest.getUsername());
        organization.setFullName(organizationAccountRequest.getFullName());
        organization.setDirectorFullName(organizationAccountRequest.getDirectorFullName());
        organization.setPhoneNumber(organizationAccountRequest.getPhoneNumber());
        organization.setAddress(organizationAccountRequest.getAddress());
        organization.setInnOrPinfl(organizationAccountRequest.getInn());
        User editedUser = userRepository.save(user);
        Organization editedOrganization = organizationRepository.save(organization);
        log.info("Organization itself updated successfully: {} \t {}", objectMapper.writeValueAsString(editedUser), objectMapper.writeValueAsString(editedOrganization));
        return CommonResponse.builder()
                .success(true)
                .message("Updated!")
                .build();
    }

    public CommonResponse changePassword(Long id, String currentPassword, String newPassword) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Organization not found with id: " + id));
        User user = userRepository.findById(organization.getUser().getId()).orElse(null);
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
