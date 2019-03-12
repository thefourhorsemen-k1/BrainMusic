package com.horsemen.bmserver.controller;

import com.horsemen.bmserver.exception.ResourceNotFoundException;
import com.horsemen.bmserver.model.FeedBack;
import com.horsemen.bmserver.repository.FeedBackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class FeedBackController {
    @Autowired
    FeedBackRepository feedBackRepository;

    @GetMapping("/admin/feedbacks")
    public List <FeedBack> getAllFeedbacks() {
        return feedBackRepository.findAll();
    }

    @PostMapping("/feedback")
    public FeedBack createFeedback(@Valid @RequestBody FeedBack feedBack) {
        return feedBackRepository.save(feedBack);
    }

    @GetMapping("/admin/feedbacks/{id}")
    public FeedBack getFeedbackById(@PathVariable(value = "id") Long feedbackId) {
        return feedBackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", "id", feedbackId));
    }

    @DeleteMapping("/admin/feedbacks/{id}")
    public ResponseEntity <?> deleteFeedback(@PathVariable(value = "id") Long feedbackId) {
        FeedBack feedBack = feedBackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", "id", feedbackId));
        feedBackRepository.delete(feedBack);
        return ResponseEntity.ok().build();
    }
}
