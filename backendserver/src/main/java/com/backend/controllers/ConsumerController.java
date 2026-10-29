package com.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.backend.dtos.ConsumerRequestDTO;
import com.backend.dtos.ConsumerResponseDTO;
import com.backend.services.ConsumerService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/consumers")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ConsumerController {

    private final ConsumerService consumerService;
    
    @PreAuthorize("hasRole('CONSUMER')")
    @GetMapping("/me")
    public ResponseEntity<ConsumerResponseDTO> getCurrentConsumer() {

        return ResponseEntity.ok(
                consumerService.getCurrentConsumer());
    }

    /**
     * Create a new Consumer profile.
     */
    @PreAuthorize("hasRole('CONSUMER')")
    @PostMapping
    public ResponseEntity<ConsumerResponseDTO> createConsumer(
            @Valid @RequestBody ConsumerRequestDTO request) {

        ConsumerResponseDTO consumer =
                consumerService.createConsumer(request);

        return new ResponseEntity<>(consumer, HttpStatus.CREATED);
    }

    /**
     * Get Consumer by ID.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{consumerId}")
    public ResponseEntity<ConsumerResponseDTO> getConsumerById(
            @PathVariable Long consumerId) {

        ConsumerResponseDTO consumer =
                consumerService.getConsumerById(consumerId);

        return ResponseEntity.ok(consumer);
    }

    /**
     * Get all Consumers.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ConsumerResponseDTO>> getAllConsumers() {

        List<ConsumerResponseDTO> consumers =
                consumerService.getAllConsumers();

        return ResponseEntity.ok(consumers);
    }

    /**
     * Update Consumer profile.
     */
    @PreAuthorize("hasRole('CONSUMER')")
    @PutMapping("/me")
    public ResponseEntity<ConsumerResponseDTO> updateCurrentConsumer(
            @Valid @RequestBody ConsumerRequestDTO request) {

        return ResponseEntity.ok(
                consumerService.updateCurrentConsumer(request));
    }

    /**
     * Delete Consumer profile.
     */

    
    @PreAuthorize("hasRole('CONSUMER')")
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteCurrentConsumer() {

        consumerService.deleteCurrentConsumer();

        return ResponseEntity.ok("Consumer profile deleted successfully.");
    }

}