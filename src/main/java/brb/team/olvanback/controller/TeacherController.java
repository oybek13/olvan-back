package brb.team.olvanback.controller;

import brb.team.olvanback.dto.TeacherRequest;
import brb.team.olvanback.service.TeacherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher")
@Tag(name = "Teachers")
@CrossOrigin
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_SCHOOL', 'ROLE_EDUCATIONAL_CENTER')")
    public ResponseEntity<?> create(@RequestBody TeacherRequest teacherRequest) throws JsonProcessingException {
        return ResponseEntity.ok(teacherService.createTeacher(teacherRequest));
    }
}
