package brb.team.olvanback.controller;

import brb.team.olvanback.dto.PupilRequest;
import brb.team.olvanback.service.PupilService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pupil")
@Tag(name = "Pupils")
@CrossOrigin
@RequiredArgsConstructor
public class PupilController {

    private final PupilService pupilService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    public ResponseEntity<?> create(@RequestBody PupilRequest pupilRequest) throws JsonProcessingException {
        return ResponseEntity.ok(pupilService.createPupil(pupilRequest));
    }

    @GetMapping("/{pupilId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    public ResponseEntity<?> getOne(@PathVariable("pupilId") Long pupilId) throws JsonProcessingException {
        return ResponseEntity.ok(pupilService.getOnePupil(pupilId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    public ResponseEntity<?> getAll(@RequestParam(value = "page") int page,
                                    @RequestParam(value = "size") int size,
                                    @RequestParam(value = "id", required = false) Long id,
                                    @RequestParam(value = "enrollType", required = false) String enrollType,
                                    @RequestParam(value = "status", required = false) Boolean status,
                                    @RequestParam(value = "dateBegin", required = false) String dateBegin) throws JsonProcessingException {
        return ResponseEntity.ok(pupilService.getAllPupils(page, size, id, enrollType, status, dateBegin));
    }

    @PutMapping("/{pupilId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    public ResponseEntity<?> update(@PathVariable(value = "pupilId") Long pupilId,
                                    @RequestBody PupilRequest pupilRequest) throws JsonProcessingException {
        return ResponseEntity.ok(pupilService.updatePupil(pupilRequest, pupilId));
    }

    @DeleteMapping("/{pupilId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    public ResponseEntity<?> delete(@PathVariable(value = "pupilId") Long pupilId) throws JsonProcessingException {
        return ResponseEntity.ok(pupilService.deletePupil(pupilId));
    }
}
