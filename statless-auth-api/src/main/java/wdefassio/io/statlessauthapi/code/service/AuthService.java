package wdefassio.io.statlessauthapi.code.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import wdefassio.io.statlessauthapi.code.dto.AuthRequest;
import wdefassio.io.statlessauthapi.code.dto.TokenDto;
import wdefassio.io.statlessauthapi.code.repository.UserRepository;
import wdefassio.io.statlessauthapi.infra.exception.ValidationException;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public TokenDto login(AuthRequest request) {
        var user = userRepository
                .findByUsername(request.username())
                .orElseThrow(() -> new ValidationException("User not found!"));

        validatePassword(request.password(), user.getPassword());

        var accessToken = jwtService.createToken(user);
        return new TokenDto(accessToken);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (ObjectUtils.isEmpty(rawPassword)) {
            throw new ValidationException("The password must be informed!");
        }
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ValidationException("The password is incorrect!");
        }
    }

    public TokenDto validateToken(String accessToken) {
        validateExistingToken(accessToken);
        jwtService.validateAccessToken(accessToken);
        return new TokenDto(accessToken);
    }

    private void validateExistingToken(String accessToken) {
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new ValidationException("Access token must be informed!");
        }
    }
}
