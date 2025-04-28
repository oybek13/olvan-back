package brb.team.olvanback.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Test")
@CrossOrigin
public class TestController {

    @GetMapping("/hello")
    @PreAuthorize("hasAnyRole('ROLE_SCHOOL', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public String hello() {
        return "hello";
    }
}
