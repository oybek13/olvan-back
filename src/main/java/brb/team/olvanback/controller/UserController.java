package brb.team.olvanback.controller;

import brb.team.olvanback.dto.CommonResponse;
import brb.team.olvanback.dto.SignInRequest;
import brb.team.olvanback.dto.SignUpRequest;
import brb.team.olvanback.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse> signUp(@RequestBody SignUpRequest user) {
        return ResponseEntity.ok(userService.signUp(user));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponse> signIn(@RequestBody SignInRequest user) {
        return ResponseEntity.ok(userService.signIn(user));
    }
}
