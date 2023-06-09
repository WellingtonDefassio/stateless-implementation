package wdefassio.io.statlessauthapi.code.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wdefassio.io.statlessauthapi.code.dto.AuthRequest;
import wdefassio.io.statlessauthapi.code.dto.TokenDto;
import wdefassio.io.statlessauthapi.code.service.AuthService;

@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
public class AuthController {


    private AuthService authService;

    @PostMapping("/login")
    public TokenDto login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }
    @PostMapping("/token/validate")
    public TokenDto validateToken(@RequestHeader String accessToken) {
        return authService.validateToken(accessToken);
    }


}
