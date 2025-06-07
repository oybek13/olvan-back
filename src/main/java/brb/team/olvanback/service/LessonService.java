package brb.team.olvanback.service;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.LessonRequest;
import brb.team.olvanback.dto.TeacherNameResponse;
import brb.team.olvanback.entity.Lesson;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import brb.team.olvanback.exception.DataNotFoundException;
import brb.team.olvanback.repository.LessonRepository;
import brb.team.olvanback.repository.UserRepository;
import brb.team.olvanback.service.extra.AppService;
import brb.team.olvanback.specs.LessonSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final AppService appService;
    private final ObjectMapper objectMapper;

    public CommonResponse create(LessonRequest lessonRequest) throws JsonProcessingException {
        Lesson savedLesson = lessonRepository.save(Lesson.builder()
                .orgId(appService.getOrgId())
                .name(lessonRequest.getName())
                .description(lessonRequest.getDescription())
                .ageCategory(lessonRequest.getAgeCategory())
                .pupilCount(lessonRequest.getPupilCount())
                .price(lessonRequest.getPrice())
                .teacherFullName(lessonRequest.getTeacherFullName())
                .status(lessonRequest.getStatus())
                .dateBegin(lessonRequest.getDateBegin())
                .days(objectMapper.writeValueAsString(lessonRequest.getDays()))
                .timeBegin(lessonRequest.getTimeBegin())
                .timeEnd(lessonRequest.getTimeEnd())
                .build());
        log.info("Lesson saved successfully: {}", objectMapper.writeValueAsString(savedLesson));
        return CommonResponse.builder()
                .success(true)
                .message("Lesson saved successfully!")
                .build();
    }

    public CommonResponse getTeachersList(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());
        Page<User> teacherPage;
        if (name != null && !name.isEmpty()) {
            teacherPage = userRepository.findByOrgIdAndRoleAndFullNameContainingIgnoreCase(
                    appService.getOrgId(), UserRole.ROLE_TEACHER, name, pageable);
        } else {
            teacherPage = userRepository.findByOrgIdAndRole(
                    appService.getOrgId(), UserRole.ROLE_TEACHER, pageable);
        }
        List<TeacherNameResponse> names = teacherPage
                .stream()
                .map(teacher -> TeacherNameResponse.builder().name(teacher.getFullName()).build())
                .collect(Collectors.toList());
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(Map.of(
                        "content", names,
                        "page", teacherPage.getNumber(),
                        "size", teacherPage.getSize(),
                        "totalElements", teacherPage.getTotalElements(),
                        "totalPages", teacherPage.getTotalPages()
                ))
                .build();
    }

    public CommonResponse getOne(Long id) {
        Lesson lesson = findById(id);
        return CommonResponse.builder()
                .success(true)
                .message("Success!")
                .data(lesson)
                .build();
    }

    public CommonResponse getAll(int page,
                                 int size,
                                 Long id,
                                 String name,
                                 String teacherFullName,
                                 Boolean status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Specification<Lesson> spec = Specification.where(LessonSpecification.hasOrgId(appService.getOrgId()))
                .and(LessonSpecification.hasId(id))
                .and(LessonSpecification.hasTeacherFullName(teacherFullName))
                .and(LessonSpecification.hasName(name))
                .and(LessonSpecification.status(status));
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

    public CommonResponse update(Long id, LessonRequest request) throws JsonProcessingException {
        Lesson lesson = findById(id);
        lesson.setName(request.getName());
        lesson.setDescription(request.getDescription());
        lesson.setAgeCategory(request.getAgeCategory());
        lesson.setPupilCount(request.getPupilCount());
        lesson.setPrice(request.getPrice());
        lesson.setTeacherFullName(request.getTeacherFullName());
        lesson.setStatus(request.getStatus());
        lesson.setDateBegin(request.getDateBegin());
        lesson.setDays(objectMapper.writeValueAsString(request.getDays()));
        lesson.setTimeBegin(request.getTimeBegin());
        lesson.setTimeEnd(request.getTimeEnd());
        Lesson updatedLesson = lessonRepository.save(lesson);
        log.info("Lesson updated successfully: {}", objectMapper.writeValueAsString(updatedLesson));
        return CommonResponse.builder()
                .success(true)
                .message("Updated successfully!")
                .build();
    }

    public CommonResponse delete(Long id) {
        Lesson lesson = findById(id);
        lessonRepository.delete(lesson);
        log.info("Lesson deleted successfully with id: {}", id);
        return CommonResponse.builder()
                .success(true)
                .message("Deleted successfully!")
                .build();
    }

    private Lesson findById(Long id) {
        return lessonRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Lesson not found with id: ".concat(String.valueOf(id))));
    }
}
