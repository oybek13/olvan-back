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
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL')")
    public ResponseEntity<?> create(@RequestBody TeacherRequest teacherRequest) throws JsonProcessingException {
        return ResponseEntity.ok(teacherService.createTeacher(teacherRequest));
    }

    @GetMapping("/{teacherId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL')")
    public ResponseEntity<?> getOne(@PathVariable Long teacherId) throws JsonProcessingException {
        return ResponseEntity.ok(teacherService.getOneTeacher(teacherId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL')")
    public ResponseEntity<?> getAll(@RequestParam(value = "page") int page,
                                    @RequestParam(value = "size") int size,
                                    @RequestParam(value = "fullName", required = false) String fullName,
                                    @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                    @RequestParam(value = "status", required = false) Boolean status,
                                    @RequestParam(value = "dateBegin", required = false) String dateBegin,
                                    @RequestParam(value = "studentCount", required = false) Integer studentCount) throws JsonProcessingException {
        return ResponseEntity.ok(teacherService.getAllTeachers(page, size, fullName, phoneNumber, status, dateBegin, studentCount));
    }

    @DeleteMapping("/{teacherId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL')")
    public ResponseEntity<?> delete(@PathVariable Long teacherId) throws JsonProcessingException {
        return ResponseEntity.ok(teacherService.deleteTeacher(teacherId));
    }

    @PutMapping("/{teacherId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL')")
    public ResponseEntity<?> update(@PathVariable Long teacherId, @RequestBody TeacherRequest teacherRequest) throws JsonProcessingException {
        return ResponseEntity.ok(teacherService.updateTeacher(teacherId, teacherRequest));
    }


}
