package com.backend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "consumers")

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "user")
public class Consumer extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id", nullable = false)
    @MapsId
    private User user;

    @NotBlank(message = "Address is required")
    @Column(nullable = false, length = 255)
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String state;

    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Invalid Pincode"
    )
    @Column(nullable = false, length = 6)
    private String pincode;

    public Consumer(String address, String city,
                    String state, String pincode) 
    {
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }
}