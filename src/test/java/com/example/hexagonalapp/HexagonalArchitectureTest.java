package com.example.hexagonalapp;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.example.hexagonalapp")
public class HexagonalArchitectureTest {

    // 1. Dependencias entre Capas

    @ArchTest
    public static final ArchRule domain_should_not_depend_on_application_or_infrastructure =
        noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..application..")
            .orShould().dependOnClassesThat().resideInAPackage("..infrastructure..");

    @ArchTest
    public static final ArchRule application_should_not_depend_on_infrastructure =
        noClasses().that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..");

    @ArchTest
    public static final ArchRule infrastructure_adapters_should_not_depend_on_input_adapters =
        noClasses().that().resideInAPackage("..infrastructure.adapter.out..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure.adapter.in..");

    // 2. Convenciones de Nomenclatura de Paquetes

    @ArchTest
    public static final ArchRule domain_packages_should_follow_conventions =
        classes().that().resideInAPackage("..domain..")
            .should().resideInAPackage("..domain.model..")
            .orShould().resideInAPackage("..domain.service..");
    
    @ArchTest
    public static final ArchRule application_packages_should_follow_conventions =
        classes().that().resideInAPackage("..application..")
            .should().resideInAPackage("..application.port.in..")
            .orShould().resideInAPackage("..application.port.out..")
            .orShould().resideInAPackage("..application.service..")
            .orShould().resideInAPackage("..application.dto..")
            .orShould().resideInAPackage("..application.config..");

    @ArchTest
    public static final ArchRule infrastructure_packages_should_follow_conventions =
        classes().that().resideInAPackage("..infrastructure..")
            .should().resideInAPackage("..infrastructure.adapter..")
            .orShould().resideInAPackage("..infrastructure.persistence..");

    // 3. Uso de Anotaciones

    @ArchTest
    public static final ArchRule domain_should_not_use_spring_annotations =
        noClasses().that().resideInAPackage("..domain..")
            .should().beAnnotatedWith(Service.class)
            .orShould().beAnnotatedWith(Repository.class)
            .orShould().beAnnotatedWith(Component.class)
            .orShould().beAnnotatedWith(Controller.class)
            .orShould().beAnnotatedWith(RestController.class);

    @ArchTest
    public static final ArchRule controllers_should_be_in_infrastructure_input =
        classes().that().areAnnotatedWith(RestController.class)
            .or().areAnnotatedWith(Controller.class)
            .should().resideInAPackage("..infrastructure.adapter.in..");

    @ArchTest
    public static final ArchRule services_should_be_in_application_or_infrastructure =
        classes().that().areAnnotatedWith(Service.class)
            .should().resideInAPackage("..application.service..")
            .orShould().resideInAPackage("..infrastructure..");

    // 4. Reglas Adicionales

    @ArchTest
    public static final ArchRule ports_should_be_interfaces_in_application =
        classes().that().haveNameMatching(".*UseCase")
            .or().haveNameMatching(".*Port")
            .should().beInterfaces()
            .andShould().resideInAPackage("..application.port..");

    @ArchTest
    public static final ArchRule implementations_should_be_in_infrastructure =
        classes().that().haveNameMatching(".*Impl")
            .or().haveNameMatching(".*Adapter")
            .should().resideInAPackage("..infrastructure..");

}