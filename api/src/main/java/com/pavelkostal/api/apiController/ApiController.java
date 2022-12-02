package com.pavelkostal.api.apiController;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.service.PhotoService;
import com.pavelkostal.api.tool.Tools;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/v1/data")
@AllArgsConstructor
public class ApiController {

    private final PhotoService photoService;

    @PostMapping()
    public ResponseEntity<Photo> saveImage(@RequestBody Photo photo) {
        if (!Tools.isValidGps(photo.getGpsPositionLatitude(), photo.getGpsPositionLongitude())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        photoService.savePhoto(photo);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
