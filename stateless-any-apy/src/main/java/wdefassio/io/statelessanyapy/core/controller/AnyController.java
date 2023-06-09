package wdefassio.io.statelessanyapy.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wdefassio.io.statelessanyapy.core.dto.AnyResponse;
import wdefassio.io.statelessanyapy.core.service.AnyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/resource")
public class AnyController {


    private final AnyService anyService;


    @GetMapping()
    public AnyResponse getResource(@RequestHeader String accessToken) {
        return anyService.getData(accessToken);
    }

}
