package brb.team.olvanback.controller;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.RegisterClientDto;
import brb.team.olvanback.service.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/updateProfile/{id}")
    @Operation(summary = "Client profile update")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<CommonResponse> update(@PathVariable Long id,
                                                 @RequestBody RegisterClientDto request) throws JsonProcessingException {
        return ResponseEntity.ok(clientService.editClientProfile(id, request));
    }

}
