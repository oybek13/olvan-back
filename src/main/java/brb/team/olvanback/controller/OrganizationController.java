package brb.team.olvanback.controller;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.OrganizationRequest;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.service.OrganizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/org")
@Tag(name = "Organization")
@CrossOrigin
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CommonResponse> create(@RequestBody OrganizationRequest organizationRequest) throws JsonProcessingException {
        return ResponseEntity.ok(organizationService.createOrganization(organizationRequest));
    }

    @GetMapping("/{orgId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CommonResponse> getOne(@PathVariable long orgId) throws JsonProcessingException {
        return ResponseEntity.ok(organizationService.getOneOrganization(orgId));
    }

    @PutMapping("/{orgId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CommonResponse> update(@PathVariable long orgId,
                                                 @RequestBody OrganizationRequest organizationRequest) throws JsonProcessingException {
        return ResponseEntity.ok(organizationService.editOrganization(organizationRequest, orgId));
    }

    @DeleteMapping("/{orgId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CommonResponse> delete(@PathVariable long orgId) {
        return ResponseEntity.ok(organizationService.deleteOrganization(orgId));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CommonResponse> getAll(@RequestParam(value = "username", required = false) String username,
                                                 @RequestParam(value = "fullName", required = false) String fullName,
                                                 @RequestParam(value = "inn", required = false) String inn,
                                                 @RequestParam(value = "role", required = false) UserRole role,
                                                 @RequestParam(value = "status", required = false) boolean status,
                                                 @RequestParam(value = "page") int page,
                                                 @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(organizationService.getAll(username, fullName, inn, role, status, page, size));
    }
}
