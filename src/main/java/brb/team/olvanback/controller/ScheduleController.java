package brb.team.olvanback.controller;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.enums.Days;
import brb.team.olvanback.service.ScheduleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/schedule")
@CrossOrigin
@Tag(name = "Schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_SCHOOL', 'ROLE_TEACHER')")
    @Operation(summary = "Get schedule")
    public ResponseEntity<CommonResponse> getSchedule(@RequestParam(value = "page") int page,
                                                      @RequestParam(value = "size") int size,
                                                      @RequestParam(value = "date") LocalDate date,
                                                      @RequestParam(value = "day") Days day,
                                                      @RequestParam(value = "lessonName", required = false) String name,
                                                      @RequestParam(value = "teacherName", required = false) String teacherName) throws JsonProcessingException {
        return ResponseEntity.ok(scheduleService.getSchedule(page, size, date, day, name, teacherName));
    }
}
