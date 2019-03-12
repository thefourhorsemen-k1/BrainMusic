package com.horsemen.bmserver.repository;

import com.horsemen.bmserver.model.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
public interface FeedBackRepository extends JpaRepository <FeedBack, Long> {
}
