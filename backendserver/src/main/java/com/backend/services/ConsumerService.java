package com.backend.services;

import java.util.List;

import com.backend.dtos.ConsumerRequestDTO;
import com.backend.dtos.ConsumerResponseDTO;

public interface ConsumerService {

    /**
     * Creates a consumer profile for an already registered user.
     *
     * @param request Consumer profile details.
     * @return Created consumer profile.
     */
    ConsumerResponseDTO createConsumer(ConsumerRequestDTO request);

    /**
     * Retrieves a consumer profile using its ID.
     *
     * @param consumerId Consumer ID.
     * @return Consumer profile details.
     */
    ConsumerResponseDTO getConsumerById(Long consumerId);

    /**
     * Retrieves all registered consumer profiles.
     *
     * @return List of all consumers.
     */
    List<ConsumerResponseDTO> getAllConsumers();

    /**
     * Updates an existing consumer profile.
     *
     * @param consumerId Consumer ID.
     * @param request Updated consumer details.
     * @return Updated consumer profile.
     */
    ConsumerResponseDTO updateCurrentConsumer(
            ConsumerRequestDTO request);
    
    /**
     * Deletes a consumer profile.
     *
     * @param consumerId Consumer ID.
     */
    void deleteCurrentConsumer();


    
    ConsumerResponseDTO getCurrentConsumer();

}