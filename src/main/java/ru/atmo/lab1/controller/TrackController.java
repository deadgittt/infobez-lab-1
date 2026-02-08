package ru.atmo.lab1.controller;

import lombok.RequiredArgsConstructor;
import ru.atmo.lab1.dto.TrackDto;
import ru.atmo.lab1.entity.Track;
import ru.atmo.lab1.service.TrackService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;

    @GetMapping()
    public List<Track> getAllTracks() {
        return trackService.getAllTracks();
    }

    @PostMapping()
    public void create(@Valid @RequestBody TrackDto trackDto) {
        trackService.create(trackDto);
    }
}
