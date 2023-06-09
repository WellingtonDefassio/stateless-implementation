package wdefassio.io.statelessanyapy.core.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import wdefassio.io.statelessanyapy.core.dto.AnyResponse;

@Service
@AllArgsConstructor
public class AnyService {

    private final JwtService jwtService;

    public AnyResponse getData(String accessToken) {
        jwtService.validateAccessToken(accessToken);
        var authUser = jwtService.getAuthenticatedUser(accessToken);
        var ok = HttpStatus.OK;
        return new AnyResponse(ok.name(), ok.value(), authUser);
    }

}
