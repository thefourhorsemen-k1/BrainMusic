package com.horsemen.bmserver.repository;

import com.horsemen.bmserver.model.Songs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
public interface SongsRepository extends JpaRepository <Songs, Long> {
}
