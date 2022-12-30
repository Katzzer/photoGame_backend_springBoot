package com.pavelkostal.api.externalApiCalls;

import com.pavelkostal.api.model.PositionStackResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "PositionStack",
        url = "http://api.positionstack.com/"
)

public interface PositionStack {

    @GetMapping("v1/forward")
    PositionStackResponseData getData(
            @RequestParam(name = "access_key") String accessKey,
            @RequestParam(name = "query") String query
    );

}
