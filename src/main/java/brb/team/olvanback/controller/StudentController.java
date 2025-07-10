package brb.team.olvanback.controller;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.StudentRequest;
import brb.team.olvanback.service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@Tag(name = "Students")
@CrossOrigin
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    public ResponseEntity<?> create(@RequestBody StudentRequest studentRequest) throws JsonProcessingException {
        return ResponseEntity.ok(studentService.createStudent(studentRequest));
    }

    @GetMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    public ResponseEntity<?> getOne(@PathVariable("studentId") Long studentId) throws JsonProcessingException {
        return ResponseEntity.ok(studentService.getOneStudent(studentId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    public ResponseEntity<?> getAll(@RequestParam(value = "page") int page,
                                    @RequestParam(value = "size") int size,
                                    @RequestParam(value = "fullName", required = false) String fullName,
                                    @RequestParam(value = "status", required = false) Boolean status,
                                    @RequestParam(value = "courseType", required = false) List<String> courseType) throws JsonProcessingException {
        return ResponseEntity.ok(studentService.getAllStudents(page, size, status, fullName, courseType));
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    public ResponseEntity<?> update(@PathVariable(value = "studentId") Long studentId,
                                    @RequestBody StudentRequest studentRequest) throws JsonProcessingException {
        return ResponseEntity.ok(studentService.updateStudent(studentRequest, studentId));
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    public ResponseEntity<?> delete(@PathVariable(value = "studentId") Long studentId) throws JsonProcessingException {
        return ResponseEntity.ok(studentService.deleteStudent(studentId));
    }

    @GetMapping("/getCourseTypesByStudentId")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    public ResponseEntity<CommonResponse> getCourseTypes(@RequestParam(value = "studentId") Long id) {
        return ResponseEntity.ok(studentService.getCourseTypesByStudentId(id));
    }
}
