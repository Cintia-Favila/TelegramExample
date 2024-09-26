package org.switf.telegramexample.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.switf.telegramexample.models.PlaceModel;
import org.switf.telegramexample.services.PlaceService;


@RestController
@RequestMapping("/places")
public class PlaceServiceController {

    @Autowired
    PlaceService placeService;

    @PostMapping("/addPlace")
    public PlaceModel createPlaceController(@RequestBody PlaceModel request){
        return placeService.createPlace(request);
    }
}
