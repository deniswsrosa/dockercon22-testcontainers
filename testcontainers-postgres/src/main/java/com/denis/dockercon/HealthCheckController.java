package com.denis.dockercon;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthCheckController {

    @GetMapping(path = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getServiceHealthStatus() {
        return "{ \"serviceStatus\": \"Service is Up\" }";
    }
}