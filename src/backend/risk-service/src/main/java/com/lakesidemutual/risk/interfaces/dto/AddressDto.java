package com.lakesidemutual.risk.interfaces.dto;

import com.lakesidemutual.risk.domain.model.Address;
import jakarta.validation.constraints.*;

/**
 * DTO for address information
 */
public record AddressDto(
    @NotBlank(message = "Street address is required")
    @Size(max = 200, message = "Street address must not exceed 200 characters")
    String street,
    
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    String city,
    
    @NotBlank(message = "State is required")
    @Size(min = 2, max = 2, message = "State must be 2 characters")
    @Pattern(regexp = "[A-Z]{2}", message = "State must be 2 uppercase letters")
    String state,
    
    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "\\d{5}(-\\d{4})?", message = "ZIP code must be in format 12345 or 12345-6789")
    String zipCode,
    
    @NotBlank(message = "Country is required")
    @Size(max = 50, message = "Country must not exceed 50 characters")
    String country
) {
    
    public static AddressDto fromDomain(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressDto(
            address.street(),
            address.city(),
            address.state(),
            address.zipCode(),
            address.country()
        );
    }
    
    public Address toDomain() {
        return new Address(street, city, state, zipCode, country);
    }
}
