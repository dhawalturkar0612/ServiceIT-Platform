package com.backend.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dtos.ConsumerRequestDTO;
import com.backend.dtos.ConsumerResponseDTO;
import com.backend.entities.Consumer;
import com.backend.entities.User;
import com.backend.entities.UserRole;
import com.backend.exceptions.DuplicateResourceException;
import com.backend.exceptions.InvalidOperationException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.repositories.ConsumerRepository;
import com.backend.repositories.UserRepository;
import com.backend.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

    private final ConsumerRepository consumerRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final AuditLogService auditLogService;

    @Override
    public ConsumerResponseDTO createConsumer(ConsumerRequestDTO request) {

    	Long userId = SecurityUtils.getCurrentUserId();

    	User user = userRepository.findById(userId)
    	        .orElseThrow(() ->
    	                new ResourceNotFoundException("User not found."));

        if (user.getRole() != UserRole.CONSUMER) {
            throw new InvalidOperationException(
                    "Selected user is not registered as a Consumer.");
        }

        if (consumerRepository.existsByUserId(user.getId())) {
            throw new DuplicateResourceException(
                    "Consumer profile already exists.");
        }

        Consumer consumer = mapper.map(request, Consumer.class);
        consumer.setUser(user);

        Consumer savedConsumer = consumerRepository.save(consumer);

        auditLogService.log(
                user.getEmail(),
                "CONSUMER",
                "PROFILE_CREATED",
                "Consumer " + user.getFullName() + " created profile in " + savedConsumer.getCity() + ", " + savedConsumer.getState() + "."
        );

        return mapToResponse(savedConsumer);
    }

    @Override
    @Transactional(readOnly = true)
    public ConsumerResponseDTO getConsumerById(Long consumerId) {

        Consumer consumer = consumerRepository.findById(consumerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Consumer not found with ID : " + consumerId));

        return mapToResponse(consumer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsumerResponseDTO> getAllConsumers() {

        return consumerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ConsumerResponseDTO updateCurrentConsumer(
            ConsumerRequestDTO request) {

        Consumer consumer = consumerRepository
                .findByUserId(SecurityUtils.getCurrentUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Consumer profile not found."));

        consumer.setAddress(request.getAddress());
        consumer.setCity(request.getCity());
        consumer.setState(request.getState());
        consumer.setPincode(request.getPincode());

        Consumer updatedConsumer = consumerRepository.save(consumer);

        auditLogService.log(
                consumer.getUser().getEmail(),
                "CONSUMER",
                "PROFILE_UPDATED",
                "Consumer " + consumer.getUser().getFullName() + " updated address to: " + updatedConsumer.getAddress() + ", " + updatedConsumer.getCity() + "."
        );

        return mapToResponse(updatedConsumer);
    }

    @Override
    public void deleteCurrentConsumer() {

        Consumer consumer = consumerRepository
                .findByUserId(SecurityUtils.getCurrentUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Consumer profile not found."));

        consumerRepository.delete(consumer);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ConsumerResponseDTO getCurrentConsumer() {

        Long userId = SecurityUtils.getCurrentUserId();

        Consumer consumer = consumerRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Consumer profile not found."));

        return mapToResponse(consumer);
    }

    /**
     * Maps Consumer entity to ConsumerResponseDTO.
     */
    private ConsumerResponseDTO mapToResponse(Consumer consumer) {

        ConsumerResponseDTO response =
                mapper.map(consumer, ConsumerResponseDTO.class);

        // Primary Key
        response.setConsumerId(consumer.getId());

        // User Details
        response.setUserId(consumer.getUser().getId());
        response.setFullName(consumer.getUser().getFullName());
        response.setEmail(consumer.getUser().getEmail());
        response.setPhone(consumer.getUser().getPhone());

        return response;
    }

}