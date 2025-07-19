package com.gosquad.presentation.controller;

import com.gosquad.presentation.DTO.travelActivityDTO;
import com.gosquad.usecase.travel_activity.TravelActivityPostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/travel-activity")
public class TravelActivityController {

    private final TravelActivityPostService travelActivityPostService;

    @Autowired
    public TravelActivityController(TravelActivityPostService travelActivityPostService) {
        this.travelActivityPostService = travelActivityPostService;
    }

    @PostMapping("/add")
    public void addActivityToTravel(HttpServletRequest request, @RequestBody travelActivityDTO dto) throws Exception {
        travelActivityPostService.createCategory(request, dto);
    }
}
