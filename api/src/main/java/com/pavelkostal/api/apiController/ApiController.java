package com.pavelkostal.api.apiController;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.service.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/v1/data")
@AllArgsConstructor
public class ApiController {

    private final PhotoService photoService;

    @GetMapping()
    public String welcomeMessage() {
        return "Welcome";
    }

    @PostMapping()
    public void saveImage(@RequestBody Photo photo) {
        // TODO: validate photo if image is ok and GPS is ok
        photoService.savePhoto(photo);
    }
}
