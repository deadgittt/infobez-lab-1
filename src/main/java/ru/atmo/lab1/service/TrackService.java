package ru.atmo.lab1.service;

import lombok.RequiredArgsConstructor;
import ru.atmo.lab1.dto.TrackDto;
import ru.atmo.lab1.entity.Track;
import ru.atmo.lab1.repository.TrackRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackService {
    private final TrackRepository trackRepository;

    @Transactional
    public void create(TrackDto trackDto) {
        var newTrack = Track.builder()
                .name(escape(trackDto.getName()))
                .author(escape(trackDto.getAuthor()))
                .numberOfPlays(trackDto.getNumberOfPlays())
                .build();
        trackRepository.save(newTrack);
    }

    @Transactional
    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    private String escape(String value) {
        return HtmlUtils.htmlEscape(value);
    }
}
