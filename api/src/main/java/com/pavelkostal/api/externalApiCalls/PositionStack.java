package com.pavelkostal.api.externalApiCalls;

import com.pavelkostal.api.model.PositionStackResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "PositionStack",
        url = "http://api.positionstack.com/"
)

public interface PositionStack {

    @GetMapping("v1/forward?access_key=9a78eefebbaee92c86702374251227ac&query=Prag")
    PositionStackResponseData getData();
}
