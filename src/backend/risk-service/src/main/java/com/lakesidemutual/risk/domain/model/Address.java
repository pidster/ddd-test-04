package com.lakesidemutual.risk.domain.model;

/**
 * Value Object representing a physical address for risk assessment purposes.
 * Used primarily for location-based risk evaluation.
 */
public record Address(
    String street,
    String city,
    String zipCode,
    String state,
    String country
) {
    
    public Address {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street cannot be empty");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        if (zipCode == null || !isValidZipCode(zipCode)) {
            throw new IllegalArgumentException("Invalid ZIP code: " + zipCode);
        }
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("State cannot be empty");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be empty");
        }
    }
    
    /**
     * Returns the full address as a formatted string.
     * 
     * @return formatted address string
     */
    public String getFullAddress() {
        return String.format("%s, %s, %s %s, %s", street, city, state, zipCode, country);
    }
    
    /**
     * Checks if this address is in a major metropolitan area.
     * This is a simplified implementation for MVP.
     * 
     * @return true if in a major city
     */
    public boolean isMetropolitanArea() {
        // Simplified logic based on ZIP code patterns
        // In production, this would use real geographic data
        String firstDigit = zipCode.substring(0, 1);
        return "0123456789".contains(firstDigit) && 
               (zipCode.startsWith("1") || zipCode.startsWith("9") || 
                zipCode.startsWith("2") || zipCode.startsWith("3"));
    }
    
    /**
     * Gets the geographic region based on ZIP code.
     * This is a simplified implementation for MVP.
     * 
     * @return geographic region
     */
    public String getRegion() {
        if (zipCode.startsWith("0") || zipCode.startsWith("1")) return "Northeast";
        if (zipCode.startsWith("2") || zipCode.startsWith("3")) return "Southeast";
        if (zipCode.startsWith("4") || zipCode.startsWith("5")) return "Midwest";
        if (zipCode.startsWith("6") || zipCode.startsWith("7")) return "South";
        if (zipCode.startsWith("8") || zipCode.startsWith("9")) return "West";
        return "Unknown";
    }
    
    /**
     * Validates ZIP code format (US format for MVP).
     * 
     * @param zipCode the ZIP code to validate
     * @return true if valid format
     */
    private static boolean isValidZipCode(String zipCode) {
        if (zipCode == null) return false;
        
        // US ZIP code patterns: 12345 or 12345-6789
        return zipCode.matches("\\d{5}") || zipCode.matches("\\d{5}-\\d{4}");
    }
    
    // Getter methods for compatibility
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }
}
