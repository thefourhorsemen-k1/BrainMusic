package com.horsemen.bmserver.controller;

import com.horsemen.bmserver.exception.ResourceNotFoundException;
import com.horsemen.bmserver.model.Songs;
import com.horsemen.bmserver.repository.SongsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class SongsController {

    @Autowired
    SongsRepository songsRepository;

    @GetMapping("/user/songs")
    public List <Songs> getAllSongs() {
        return songsRepository.findAll();
    }

    @PostMapping("/admin/songs")
    public Songs createSongs(@Valid @RequestBody Songs songs) {
        return songsRepository.save(songs);
    }

    @DeleteMapping("/admin/songs/{id}")
    public ResponseEntity <?> deleteSongs(@PathVariable(value = "id") Long songsId) {
        Songs songs = songsRepository.findById(songsId)
                .orElseThrow(() -> new ResourceNotFoundException("Songs", "id", songsId));

        songsRepository.delete(songs);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/songs/{id}")
    public Songs getSongsById(@PathVariable(value = "id") Long songsId) {
        return songsRepository.findById(songsId)
                .orElseThrow(() -> new ResourceNotFoundException("Songs", "id", songsId));
    }
    
    @PutMapping("/admin/songs/{id}")
    public Songs updateSongs(@PathVariable(value = "id") Long songsId,
                             @Valid @RequestBody Songs songsDetails) {

        Songs songs = songsRepository.findById(songsId)
                .orElseThrow(() -> new ResourceNotFoundException("Songs", "id", songsId));
        songs.setId(songsDetails.getId());
        songs.setCategory(songsDetails.getCategory());
        songs.setImageUrl(songsDetails.getImageUrl());
        songs.setSongUrl(songsDetails.getSongUrl());
        songs.setName(songsDetails.getName());
        Songs updatedSongs = songsRepository.save(songs);
        return updatedSongs;
    }
}
