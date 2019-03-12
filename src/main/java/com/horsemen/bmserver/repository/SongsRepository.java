package com.horsemen.bmserver.repository;

import com.horsemen.bmserver.model.Songs;
import com.horsemen.bmserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
public interface SongsRepository extends JpaRepository <Songs, Long> {

    List<Songs> findAllByCategoryContaining(String category);
}
