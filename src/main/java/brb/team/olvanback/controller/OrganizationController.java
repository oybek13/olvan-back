package brb.team.olvanback.controller;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.OrganizationAccountRequest;
import brb.team.olvanback.service.OrganizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/org")
@Tag(name = "Organizations")
@CrossOrigin
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CommonResponse> create(@RequestParam(value = "contract") MultipartFile contract,
                                                 @RequestParam(value = "data") String organizationRequest) throws Exception {
        return ResponseEntity.ok(organizationService.createOrganization(organizationRequest, contract));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CommonResponse> getOne(@PathVariable(value = "id") Long id) throws JsonProcessingException {
        return ResponseEntity.ok(organizationService.getOneOrganization(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CommonResponse> update(@PathVariable(value = "id") Long id,
                                                 @RequestParam(value = "data") String data,
                                                 @RequestParam(value = "contract", required = false) MultipartFile contract) throws Exception {
        return ResponseEntity.ok(organizationService.editOrganization(data, contract, id));
    }

    @DeleteMapping("/{orgId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CommonResponse> delete(@PathVariable(value = "orgId") Long orgId) {
        return ResponseEntity.ok(organizationService.deleteOrganization(orgId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<?> getAll(@RequestParam(value = "address", required = false) String address,
                                    @RequestParam(value = "fullName", required = false) String fullName,
                                    @RequestParam(value = "inn", required = false) String inn,
                                    @RequestParam(value = "active", required = false) Boolean active,
                                    @RequestParam(value = "page") int page,
                                    @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(organizationService.getAll(address, fullName, inn, active, page, size));
    }

    @GetMapping("/account")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL')")
    public ResponseEntity<?> getOneAccount() {
        return ResponseEntity.ok(organizationService.getOneAccount());
    }

    @PutMapping("/updateAccount/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL')")
    public ResponseEntity<?> updateAccount(@PathVariable Long id,
                                           @RequestBody OrganizationAccountRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(organizationService.updateAccount(id, request));
    }

    @PutMapping("/changePassword/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL')")
    public ResponseEntity<?> changePassword(@PathVariable Long id,
                                            @RequestParam(value = "currentPsw") String currentPassword,
                                            @RequestParam(value = "newPsw") String newPassword) {
        return ResponseEntity.ok(organizationService.changePassword(id, currentPassword, newPassword));
    }
}
