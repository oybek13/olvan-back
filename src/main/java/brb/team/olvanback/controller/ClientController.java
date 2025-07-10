package brb.team.olvanback.controller;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.KidRequest;
import brb.team.olvanback.dto.RegisterClientDto;
import brb.team.olvanback.service.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@CrossOrigin
@Tag(name = "Clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    //Profile
    @PostMapping("/register")
    @Operation(summary = "Client registration")
    public ResponseEntity<CommonResponse> registerClient(@RequestBody RegisterClientDto request) throws JsonProcessingException {
        return ResponseEntity.ok(clientService.registerClient(request));
    }

    @GetMapping("/profileInfo")
    @Operation(summary = "Client profile info")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<CommonResponse<RegisterClientDto>> profileInfo() {
        return ResponseEntity.ok(clientService.getClientInfo());
    }

    @PutMapping("/updateProfile/{id}")
    @Operation(summary = "Client profile update")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<CommonResponse> updateProfile(@PathVariable Long id,
                                                        @RequestBody RegisterClientDto request) throws JsonProcessingException {
        return ResponseEntity.ok(clientService.editClientProfile(id, request));
    }


    //Kid
    @PostMapping("/createKid")
    @Operation(summary = "Client/Parent creates kid")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<CommonResponse> createKid(@RequestBody KidRequest kidRequest) throws JsonProcessingException {
        return ResponseEntity.ok(clientService.createKid(kidRequest));
    }

    @GetMapping("/allKids")
    @Operation(summary = "Client/Parent fetches all kids")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<CommonResponse> getAllKids() throws JsonProcessingException {
        return ResponseEntity.ok(clientService.getAllKids());
    }

    @PutMapping("/updateKid/{id}")
    @Operation(summary = "Client/Parent update its kid")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<CommonResponse> updateKid(@PathVariable Long id,
                                                    @RequestBody KidRequest kidRequest) throws JsonProcessingException {
        return ResponseEntity.ok(clientService.editKid(id, kidRequest));
    }

    @GetMapping("/getOneKid/{id}")
    @Operation(summary = "Client/Parent fetch one kid")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<CommonResponse> getOneKid(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(clientService.getOneKid(id));
    }

    @DeleteMapping("/deleteKid/{id}")
    @Operation(summary = "Client/Parent delete kid")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<CommonResponse> deleteKid(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.deleteKid(id));
    }

    //Organization and lessons
    @GetMapping("/lessonsByOrgId/{orgId}")
    @Operation(summary = "Get lessons by orgId")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<CommonResponse> lessonsByOrgId(@PathVariable Long orgId,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(clientService.getLessonByOrgId(orgId, page, size));
    }

    @PostMapping("/linkKidToOrg")
    @Operation(summary = "Client selects organizations for their kids")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<CommonResponse> linkKidToOrg(@RequestParam(value = "studentId") Long studentId,
                                                       @RequestParam(value = "orgId") Long orgId,
                                                       @RequestParam(value = "lessonId") Long lessonId) throws JsonProcessingException {
        return ResponseEntity.ok(clientService.linkKidWithOrganization(studentId, orgId, lessonId));
    }

    @GetMapping("/getCurrentOrgsAndLessons")
    @Operation(summary = "Kids selected organizations and lessons")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<CommonResponse> getCurrentOrgsAndLessons(@RequestParam(value = "studentId") Long studentId) {
        return ResponseEntity.ok(clientService.getCurrentLessonsByStudent(studentId));
    }

    //Subscription
    @GetMapping("/subscriptions")
    @Operation(summary = "Get subscriptions")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<CommonResponse> getSubscriptions() {
        return ResponseEntity.ok(clientService.getSubscriptions());
    }

}
