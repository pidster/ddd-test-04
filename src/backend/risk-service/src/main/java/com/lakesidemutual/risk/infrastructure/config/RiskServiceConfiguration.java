package com.lakesidemutual.risk.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring configuration for the Risk Service infrastructure layer.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.lakesidemutual.risk.infrastructure.repository")
@EntityScan(basePackages = "com.lakesidemutual.risk.domain.model")
@EnableTransactionManagement
public class RiskServiceConfiguration {
}
