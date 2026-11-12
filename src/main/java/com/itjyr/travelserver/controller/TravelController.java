package com.itjyr.travelserver.controller;

import com.itjyr.travelserver.dto.ChatRequestDTO;
import com.itjyr.travelserver.dto.TravelRequestDTO;
import com.itjyr.travelserver.service.TravelService;
import com.itjyr.travelserver.vo.Result;
import com.itjyr.travelserver.vo.TravelRecommendVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/travel")
public class TravelController {
    private final TravelService travelService;

    @PostMapping("/recommend")
    public Result<TravelRecommendVO> recommend(@Valid @RequestBody TravelRequestDTO travelRequestDTO) {
        TravelRecommendVO recommend = travelService.recommend(travelRequestDTO);
        return Result.ok(recommend);
    }
    @PostMapping(value = "/chat", produces = "text/event-stream")
    public SseEmitter chat(@Valid @RequestBody ChatRequestDTO chatRequestDTO){
        return travelService.chat(chatRequestDTO);
    }
}
