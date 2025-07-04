# Risk Service API Documentation

## Overview
The Lakeside Mutual Risk Service provides comprehensive risk assessment and management capabilities for insurance operations. Built using Domain-Driven Design principles, it handles risk profile creation, risk factor analysis, and risk score calculations.

## Base URL
```
http://localhost:8083/api/v1
```

## Risk Profile Endpoints

### Create Risk Profile
**POST** `/risk/profiles`

Creates a new risk profile for a customer.

**Request Body:**
```json
{
  "customerId": "CUST-001",
  "profileType": "AUTO",
  "drivingHistory": {
    "accidents": 0,
    "violations": 1,
    "yearsOfExperience": 10,
    "licenseDate": "2010-01-01"
  },
  "address": {
    "street": "123 Main St",
    "city": "Anytown",
    "state": "CA",
    "zipCode": "12345",
    "country": "USA"
  },
  "age": 30,
  "occupation": "Software Engineer",
  "annualIncome": 75000.00
}
```

**Response:**
```json
{
  "profileId": "uuid-string",
  "customerId": "CUST-001",
  "profileType": "AUTO",
  "currentRiskScore": {
    "value": 350,
    "category": "MEDIUM",
    "isHighRisk": false
  },
  "riskFactors": [
    {
      "type": "DEMOGRAPHIC",
      "description": "Age factor",
      "impact": 1.1
    }
  ],
  "drivingHistory": {
    "accidents": 0,
    "violations": 1,
    "yearsOfExperience": 10,
    "licenseDate": "2010-01-01"
  },
  "address": {
    "street": "123 Main St",
    "city": "Anytown",
    "state": "CA",
    "zipCode": "12345",
    "country": "USA"
  },
  "age": 30,
  "occupation": "Software Engineer",
  "annualIncome": 75000.00,
  "createdAt": "2025-07-02T10:30:00",
  "updatedAt": "2025-07-02T10:30:00"
}
```

### Get Risk Profile by Customer ID
**GET** `/risk/profiles/{customerId}`

Retrieves the risk profile for a specific customer.

**Path Parameters:**
- `customerId` (string): The customer identifier

### Get All Risk Profiles for Customer
**GET** `/risk/profiles/customer/{customerId}/all`

Returns all risk profiles associated with a customer.

### Get Risk Profiles by Type
**GET** `/risk/profiles/type/{profileType}`

Returns all risk profiles of a specific type (AUTO, HOME, LIFE, etc.).

### Get High-Risk Profiles
**GET** `/risk/profiles/high-risk`

Returns all profiles that are classified as high-risk for review.

### Update Risk Factor
**PUT** `/risk/profiles/{customerId}/factors/{factorType}`

Updates a specific risk factor for a customer's profile.

**Request Body:**
```json
{
  "description": "Updated factor description",
  "impact": 1.2
}
```

### Recalculate Risk Score
**POST** `/risk/profiles/{customerId}/recalculate`

Triggers a recalculation of the risk score for a customer's profile.

### Check Profile Existence
**GET** `/risk/profiles/customer/{customerId}/exists?profileType=AUTO`

Checks if a customer has a risk profile of a specific type.

## Risk Assessment Endpoints

### Create Assessment
**POST** `/risk/assessments`

Performs a risk assessment for quote generation.

### Get Assessment History
**GET** `/risk/assessments/{customerId}`

Returns assessment history for a customer.

### Recalculate Assessment
**PUT** `/risk/assessments/{assessmentId}/recalculate`

Recalculates an existing risk assessment.

## Error Responses

### 400 Bad Request
```json
{
  "error": "Bad Request",
  "message": "Validation failed",
  "details": ["Field 'customerId' is required"]
}
```

### 404 Not Found
```json
{
  "error": "Not Found",
  "message": "Risk profile not found for customer: CUST-001"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

## Domain Model

### Risk Score Categories
- **LOW**: 0-200
- **MEDIUM**: 201-500  
- **HIGH**: 501-800
- **VERY_HIGH**: 801-1000

### Risk Factor Types
- **DEMOGRAPHIC**: Age, gender, marital status
- **DRIVING_HISTORY**: Accidents, violations, experience
- **LOCATION**: Geographic risk factors
- **OCCUPATION**: Job-related risk factors
- **VEHICLE**: Car make, model, safety features
- **COVERAGE**: Insurance coverage details
- **CREDIT**: Credit-based factors
- **PRIOR_INSURANCE**: Previous insurance history

### Profile Types
- **AUTO**: Automobile insurance
- **HOME**: Home/property insurance
- **LIFE**: Life insurance
- **HEALTH**: Health insurance

## Authentication
Currently, the service operates without authentication for development purposes. In production, all endpoints should be secured with appropriate authentication and authorization mechanisms.

## Health Check
**GET** `/actuator/health`

Returns the service health status.
