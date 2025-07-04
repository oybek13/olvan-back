package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.entity.Lesson;
import brb.team.olvanback.entity.Teacher;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.Days;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.DataNotFoundException;
import brb.team.olvanback.repository.LessonRepository;
import brb.team.olvanback.repository.TeacherRepository;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.service.extra.AppService;
import brb.team.olvanback.specs.LessonSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final AppService appService;

    public CommonResponse getSchedule(int page,
                                      int size,
                                      LocalDate date,
                                      Days day,
                                      String name,
                                      String teacherFullName) {
        Long orgId = appService.getOrgId();
        String role = appService.getRole();
        User user = userRepository.findByUsername(appService.getUsername()).orElseThrow(() -> new DataNotFoundException("User not found!"));
        Teacher teacher = teacherRepository.findByUserId(user.getId()).orElse(null);
        if (UserRole.ROLE_SCHOOL.name().equals(role)) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Specification<Lesson> spec = Specification.where(LessonSpecification.hasOrgId(orgId))
                    .and(LessonSpecification.hasBeginDate(date))
                    .and(LessonSpecification.hasDay(day))
                    .and(LessonSpecification.hasTeacherFullName(teacherFullName))
                    .and(LessonSpecification.hasName(name))
                    .and(LessonSpecification.status(true));
            Page<Lesson> lessonPage = lessonRepository.findAll(spec, pageable);
            return CommonResponse.builder()
                    .success(true)
                    .message("Success!")
                    .data(Map.of(
                            "content", lessonPage.getContent(),
                            "page", lessonPage.getNumber(),
                            "size", lessonPage.getSize(),
                            "totalElements", lessonPage.getTotalElements(),
                            "totalPages", lessonPage.getTotalPages()))
                    .build();
        } else if (UserRole.ROLE_TEACHER.name().equals(role)) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Specification<Lesson> spec = Specification.where(LessonSpecification.hasOrgId(orgId))
                    .and(LessonSpecification.hasTeacherFullName(teacher.getFullName()))
                    .and(LessonSpecification.hasBeginDate(date))
                    .and(LessonSpecification.hasDay(day))
                    .and(LessonSpecification.status(true));
            Page<Lesson> lessonPage = lessonRepository.findAll(spec, pageable);
            return CommonResponse.builder()
                    .success(true)
                    .message("Success!")
                    .data(Map.of(
                            "content", lessonPage.getContent(),
                            "page", lessonPage.getNumber(),
                            "size", lessonPage.getSize(),
                            "totalElements", lessonPage.getTotalElements(),
                            "totalPages", lessonPage.getTotalPages()))
                    .build();
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Specification<Lesson> spec = Specification.where(LessonSpecification.hasBeginDate(date))
                    .and(LessonSpecification.hasDay(day))
                    .and(LessonSpecification.hasTeacherFullName(teacherFullName))
                    .and(LessonSpecification.hasName(name))
                    .and(LessonSpecification.status(true));
            Page<Lesson> lessonPage = lessonRepository.findAll(spec, pageable);
            return CommonResponse.builder()
                    .success(true)
                    .message("Success!")
                    .data(Map.of(
                            "content", lessonPage.getContent(),
                            "page", lessonPage.getNumber(),
                            "size", lessonPage.getSize(),
                            "totalElements", lessonPage.getTotalElements(),
                            "totalPages", lessonPage.getTotalPages()))
                    .build();
        }
    }
}
