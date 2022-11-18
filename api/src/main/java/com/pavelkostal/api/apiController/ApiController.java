package com.pavelkostal.api.apiController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/v1/data")
public class ApiController {

    @GetMapping()
    public String welcomeMessage() {
        return "Welcome";
    }
}
