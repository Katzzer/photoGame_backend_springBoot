package com.pavelkostal.api.externalApiCalls;

import com.pavelkostal.api.model.PositionStackResponseDataWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "PositionStack",
        url = "http://api.positionstack.com/v1/"
)

public interface PositionStack {

    @GetMapping("forward")
    PositionStackResponseDataWrapper getDataByCity(
            @RequestParam(name = "access_key") String accessKey,
            @RequestParam(name = "query") String query
    );

    @GetMapping("reverse")
    PositionStackResponseDataWrapper getDataByGps(
            @RequestParam(name = "access_key") String accessKey,
            @RequestParam(name = "query") String query
    );

}
