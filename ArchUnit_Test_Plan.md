# Plan de Pruebas con ArchUnit para Arquitectura Hexagonal

## Introducción

Este documento describe un plan de pruebas utilizando ArchUnit para validar y mantener la integridad de la arquitectura hexagonal en el proyecto HexagonalApp. ArchUnit es una biblioteca de pruebas que permite verificar reglas de arquitectura en código Java, asegurando que la estructura se mantenga limpia y las dependencias sean correctas.

## Arquitectura Hexagonal en el Proyecto

La arquitectura hexagonal divide el código en tres capas principales:

- **Domain**: Lógica de negocio pura, independiente de frameworks externos.
- **Application**: Casos de uso, puertos de entrada y salida.
- **Infrastructure**: Adaptadores que implementan los puertos, dependencias externas.

Estructura de paquetes:
- `com.example.hexagonalapp.domain.*`
- `com.example.hexagonalapp.application.*`
- `com.example.hexagonalapp.infrastructure.*`

## Reglas de Arquitectura a Validar

### 1. Dependencias entre Capas

#### Regla 1.1: Domain no debe depender de Application o Infrastructure
```java
@ArchTest
public static final ArchRule domain_should_not_depend_on_application_or_infrastructure =
    noClasses().that().resideInAPackage("..domain..")
        .should().dependOnClassesThat().resideInAPackage("..application..")
        .orShould().dependOnClassesThat().resideInAPackage("..infrastructure..");
```

**Explicación**: La capa de dominio debe ser independiente y no conocer detalles de implementación externa.

#### Regla 1.2: Application no debe depender de Infrastructure
```java
@ArchTest
public static final ArchRule application_should_not_depend_on_infrastructure =
    noClasses().that().resideInAPackage("..application..")
        .should().dependOnClassesThat().resideInAPackage("..infrastructure..");
```

**Explicación**: Los casos de uso deben definir interfaces (puertos) sin conocer las implementaciones concretas.

#### Regla 1.3: Infrastructure puede depender de Application y Domain
```java
@ArchTest
public static final ArchRule infrastructure_can_depend_on_application_and_domain =
    classes().that().resideInAPackage("..infrastructure..")
        .should().onlyDependOnClassesThat().resideInAPackage("..domain..")
        .orShould().onlyDependOnClassesThat().resideInAPackage("..application..")
        .orShould().onlyDependOnClassesThat().resideOutsideOfPackages("..hexagonalapp..");
```

**Explicación**: Los adaptadores implementan puertos definidos en application y pueden usar domain.

### 2. Convenciones de Nomenclatura de Paquetes

#### Regla 2.1: Paquetes de Domain deben seguir estructura específica
```java
@ArchTest
public static final ArchRule domain_packages_should_follow_conventions =
    classes().that().resideInAPackage("..domain..")
        .should().resideInAPackage("..domain.model..")
        .orShould().resideInAPackage("..domain.service..");
```

**Explicación**: Mantener consistencia en la organización del dominio.

#### Regla 2.2: Paquetes de Application
```java
@ArchTest
public static final ArchRule application_packages_should_follow_conventions =
    classes().that().resideInAPackage("..application..")
        .should().resideInAPackage("..application.port.in..")
        .orShould().resideInAPackage("..application.port.out..")
        .orShould().resideInAPackage("..application.service..")
        .orShould().resideInAPackage("..application.dto..");
```

#### Regla 2.3: Paquetes de Infrastructure
```java
@ArchTest
public static final ArchRule infrastructure_packages_should_follow_conventions =
    classes().that().resideInAPackage("..infrastructure..")
        .should().resideInAPackage("..infrastructure.adapter..")
        .orShould().resideInAPackage("..infrastructure.persistence..");
```

### 3. Uso de Anotaciones

#### Regla 3.1: Domain no debe usar anotaciones de Spring
```java
@ArchTest
public static final ArchRule domain_should_not_use_spring_annotations =
    noClasses().that().resideInAPackage("..domain..")
        .should().beAnnotatedWith(Service.class)
        .orShould().beAnnotatedWith(Repository.class)
        .orShould().beAnnotatedWith(Component.class)
        .orShould().beAnnotatedWith(Controller.class)
        .orShould().beAnnotatedWith(RestController.class);
```

**Explicación**: El dominio debe ser framework-agnostic.

#### Regla 3.2: Controllers solo en Infrastructure Input Adapters
```java
@ArchTest
public static final ArchRule controllers_should_be_in_infrastructure_input =
    classes().that().areAnnotatedWith(RestController.class)
        .or().areAnnotatedWith(Controller.class)
        .should().resideInAPackage("..infrastructure.adapter.in..");
```

#### Regla 3.3: Services en Application o Infrastructure
```java
@ArchTest
public static final ArchRule services_should_be_in_application_or_infrastructure =
    classes().that().areAnnotatedWith(Service.class)
        .should().resideInAPackage("..application.service..")
        .orShould().resideInAPackage("..infrastructure..");
```

### 4. Reglas Adicionales

#### Regla 4.1: Interfaces de puertos en Application
```java
@ArchTest
public static final ArchRule ports_should_be_interfaces_in_application =
    classes().that().haveNameMatching(".*UseCase")
        .or().haveNameMatching(".*Port")
        .should().beInterfaces()
        .andShould().resideInAPackage("..application.port..");
```

#### Regla 4.2: Implementaciones en Infrastructure
```java
@ArchTest
public static final ArchRule implementations_should_be_in_infrastructure =
    classes().that().implement(Interface.class)
        .and().haveNameMatching(".*Impl")
        .or().haveNameMatching(".*Adapter")
        .should().resideInAPackage("..infrastructure..");
```

## Implementación de las Pruebas

### Clase de Prueba ArchUnit

Crear la clase `HexagonalArchitectureTest` en `src/test/java`:

```java
package com.example.hexagonalapp;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(packages = "com.example.hexagonalapp")
public class HexagonalArchitectureTest {

    // Incluir todas las reglas definidas arriba

}
```

### Dependencia Maven

Agregar a `pom.xml`:

```xml
<dependency>
    <groupId>com.tngtech.archunit</groupId>
    <artifactId>archunit-junit5</artifactId>
    <version>1.2.1</version>
    <scope>test</scope>
</dependency>
```

## Beneficios

- **Prevención de violaciones**: Las pruebas fallarán si se introducen dependencias incorrectas.
- **Documentación viva**: El código de las reglas sirve como documentación.
- **Refactoring seguro**: Asegura que cambios no rompan la arquitectura.
- **Integración continua**: Ejecutar en CI para mantener calidad.

## Ejecución

Ejecutar con Maven:
```bash
mvn test -Dtest=HexagonalArchitectureTest
```

O en IDE como pruebas JUnit.

## Mantenimiento

- Actualizar reglas cuando evolucione la arquitectura.
- Revisar fallos de pruebas para entender violaciones.
- Usar como guía para nuevos desarrolladores.