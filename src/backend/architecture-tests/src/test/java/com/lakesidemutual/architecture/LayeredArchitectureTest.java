package com.lakesidemutual.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LayeredArchitectureTest {

    private JavaClasses importedClasses;

    @BeforeEach
    public void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.lakesidemutual");
    }

    @Test
    public void layeredArchitectureShouldBeRespected() {
        Architectures.layeredArchitecture()
            // Define Layers
            .layer("Domain").definedBy("..domain..")
            .layer("Application").definedBy("..application..")
            .layer("Infrastructure").definedBy("..infrastructure..")
            .layer("Interfaces").definedBy("..interfaces..")
            
            // Define Rules
            .whereLayer("Interfaces").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Interfaces")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
            .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
            
            .because("We follow DDD layering principles")
            .check(importedClasses);
    }

    @Test
    public void domainModelsShouldNotDependOnOutsideLayers() {
        com.tngtech.archunit.lang.ArchRule rule = com.tngtech.archunit.lang.syntax.ArchRuleDefinition
            .classes()
            .that().resideInAPackage("..domain..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "java..",
                "javax..",
                "..domain..",
                "org.slf4j.."
            );
        
        rule.check(importedClasses);
    }

    @Test
    public void aggregateRootsShouldBeEntities() {
        com.tngtech.archunit.lang.ArchRule rule = com.tngtech.archunit.lang.syntax.ArchRuleDefinition
            .classes()
            .that().areAnnotatedWith(com.lakesidemutual.domain.model.AggregateRoot.class)
            .should().beAnnotatedWith(javax.persistence.Entity.class)
            .because("Aggregate roots should be entities");
        
        rule.check(importedClasses);
    }

    @Test
    public void repositoriesShouldResideInInfrastructureLayer() {
        com.tngtech.archunit.lang.ArchRule rule = com.tngtech.archunit.lang.syntax.ArchRuleDefinition
            .classes()
            .that().haveNameMatching(".*Repository")
            .should().resideInAPackage("..infrastructure.persistence..")
            .because("Repositories should be in the infrastructure.persistence package");
        
        rule.check(importedClasses);
    }

    @Test
    public void valueObjectsShouldBeImmutable() {
        com.tngtech.archunit.lang.ArchRule rule = com.tngtech.archunit.lang.syntax.ArchRuleDefinition
            .classes()
            .that().areAnnotatedWith(com.lakesidemutual.domain.model.ValueObject.class)
            .should().haveOnlyFinalFields()
            .andShould().beImmutable()
            .because("Value objects must be immutable");
        
        rule.check(importedClasses);
    }
}
