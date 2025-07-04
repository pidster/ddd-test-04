-- Initial database schema for Risk Service
-- Creates tables for risk profiles and risk assessments

-- Risk Profiles table
CREATE TABLE risk_profiles (
    profile_id VARCHAR(255) NOT NULL PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    profile_type VARCHAR(50) NOT NULL,
    
    -- Risk Score (embedded)
    calculated_risk_score INTEGER,
    calculated_risk_category VARCHAR(20),
    
    -- Driving History (embedded)
    driving_accidents INTEGER DEFAULT 0,
    driving_violations INTEGER DEFAULT 0,
    driving_years_experience INTEGER,
    driving_license_date DATE,
    
    -- Address (embedded)
    address_street VARCHAR(200),
    address_city VARCHAR(100),
    address_state VARCHAR(2),
    address_zip_code VARCHAR(10),
    address_country VARCHAR(50),
    
    -- Personal Information
    age INTEGER,
    occupation VARCHAR(100),
    annual_income DECIMAL(12,2),
    
    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT chk_age CHECK (age >= 16 AND age <= 120),
    CONSTRAINT chk_annual_income CHECK (annual_income >= 0),
    CONSTRAINT chk_driving_accidents CHECK (driving_accidents >= 0),
    CONSTRAINT chk_driving_violations CHECK (driving_violations >= 0),
    CONSTRAINT chk_driving_experience CHECK (driving_years_experience >= 0)
);

-- Risk Profile Factors table (for ElementCollection)
CREATE TABLE risk_profile_factors (
    profile_id VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    impact DECIMAL(5,3) NOT NULL,
    
    PRIMARY KEY (profile_id, type, description),
    FOREIGN KEY (profile_id) REFERENCES risk_profiles(profile_id) ON DELETE CASCADE,
    
    CONSTRAINT chk_impact CHECK (impact > 0)
);

-- Risk Assessments table
CREATE TABLE risk_assessments (
    assessment_id VARCHAR(255) NOT NULL PRIMARY KEY,
    profile_id VARCHAR(255) NOT NULL,
    policy_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    
    -- Calculated Risk Score (embedded)
    calculated_risk_score INTEGER,
    calculated_risk_category VARCHAR(20),
    
    -- Pricing information
    base_premium DECIMAL(10,2) NOT NULL,
    risk_multiplier DECIMAL(5,3),
    final_premium DECIMAL(10,2),
    
    -- Assessment details
    assessment_notes TEXT,
    assessor_id VARCHAR(255),
    
    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    version BIGINT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT chk_base_premium CHECK (base_premium > 0),
    CONSTRAINT chk_risk_multiplier CHECK (risk_multiplier > 0),
    CONSTRAINT chk_final_premium CHECK (final_premium >= 0),
    
    -- Foreign key constraint
    FOREIGN KEY (profile_id) REFERENCES risk_profiles(profile_id) ON DELETE CASCADE
);

-- Assessment Risk Factors table (for ElementCollection)
CREATE TABLE assessment_risk_factors (
    assessment_id VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    impact DECIMAL(5,3) NOT NULL,
    
    PRIMARY KEY (assessment_id, type, description),
    FOREIGN KEY (assessment_id) REFERENCES risk_assessments(assessment_id) ON DELETE CASCADE,
    
    CONSTRAINT chk_assessment_impact CHECK (impact > 0)
);

-- Indexes for performance
CREATE INDEX idx_risk_profiles_customer_id ON risk_profiles(customer_id);
CREATE INDEX idx_risk_profiles_type ON risk_profiles(profile_type);
CREATE INDEX idx_risk_profiles_score ON risk_profiles(calculated_risk_score);
CREATE INDEX idx_risk_profiles_created_at ON risk_profiles(created_at);

CREATE INDEX idx_risk_assessments_profile_id ON risk_assessments(profile_id);
CREATE INDEX idx_risk_assessments_status ON risk_assessments(status);
CREATE INDEX idx_risk_assessments_policy_type ON risk_assessments(policy_type);
CREATE INDEX idx_risk_assessments_assessor ON risk_assessments(assessor_id);
CREATE INDEX idx_risk_assessments_created_at ON risk_assessments(created_at);

-- Unique constraint to prevent duplicate profiles per customer/type
CREATE UNIQUE INDEX uk_risk_profiles_customer_type ON risk_profiles(customer_id, profile_type);
