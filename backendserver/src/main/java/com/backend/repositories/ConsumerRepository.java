package com.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entities.Consumer;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    Optional<Consumer> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

}