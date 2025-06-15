package brb.team.olvanback.controller;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.LessonRequest;
import brb.team.olvanback.service.LessonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lesson")
@CrossOrigin
@Tag(name = "Lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    @Operation(summary = "Create lesson")
    public ResponseEntity<CommonResponse> createLesson(@RequestBody LessonRequest lessonRequest) throws JsonProcessingException {
        return ResponseEntity.ok(lessonService.create(lessonRequest));
    }

    @GetMapping("/teachers")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    @Operation(summary = "Teachers list")
    public ResponseEntity<CommonResponse> teacherList(@RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(lessonService.getTeachersList(name, page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    @Operation(summary = "Fetch a lesson")
    public ResponseEntity<CommonResponse> getOneLesson(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getOne(id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    @Operation(summary = "Fetch all lessons")
    public ResponseEntity<CommonResponse> getAllLessons(@RequestParam(value = "page") int page,
                                           @RequestParam(value = "size") int size,
                                           @RequestParam(value = "id", required = false) Long id,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "status", required = false) Boolean status,
                                           @RequestParam(value = "teacherFullName", required = false) String teacherFullName) {
        return ResponseEntity.ok(lessonService.getAll(page, size, id, name, teacherFullName, status));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    @Operation(summary = "Update a lesson")
    public ResponseEntity<CommonResponse> updateLesson(@PathVariable Long id,
                                                       @RequestBody LessonRequest lessonRequest) throws JsonProcessingException {
        return ResponseEntity.ok(lessonService.update(id, lessonRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    @Operation(summary = "Delete a lesson")
    public ResponseEntity<CommonResponse> deleteLesson(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.delete(id));
    }
}
