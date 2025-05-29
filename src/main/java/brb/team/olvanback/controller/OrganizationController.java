package brb.team.olvanback.controller;

import brb.team.olvanback.dto.CommonResponse;
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

    @GetMapping("/{orgId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CommonResponse> getOne(@PathVariable long orgId) throws JsonProcessingException {
        return ResponseEntity.ok(organizationService.getOneOrganization(orgId));
    }

    @PutMapping(value = "/{orgId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CommonResponse> update(@PathVariable long orgId,
                                                 @RequestParam(value = "data") String data,
                                                 @RequestParam(value = "contract", required = false) MultipartFile contract) throws Exception {
        return ResponseEntity.ok(organizationService.editOrganization(data, contract, orgId));
    }

    @DeleteMapping("/{orgId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CommonResponse> delete(@PathVariable long orgId) {
        return ResponseEntity.ok(organizationService.deleteOrganization(orgId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> getAll(@RequestParam(value = "address", required = false) String address,
                                    @RequestParam(value = "fullName", required = false) String fullName,
                                    @RequestParam(value = "inn", required = false) String inn,
                                    @RequestParam(value = "active") boolean active,
                                    @RequestParam(value = "page") int page,
                                    @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(organizationService.getAll(address, fullName, inn, active, page, size));
    }
}
