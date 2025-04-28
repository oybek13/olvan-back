package brb.team.olvanback.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")
    @PreAuthorize("hasAnyRole('ROLE_SCHOOL', 'ROLE_ADMIN')")
    public String hello() {
        return "hello";
    }
}
