# SpringBootus

Create Angular frontend and Java11/SpringBoot backend based on a given domain structure

## Technologies used
`Java11` `Spring Boot` `SQL` `JPA` `Hibernate` `HTML/CSS` `Bootstrap` `JavaScript` `TypeScript` `Angular` `JavaFX`

## To get started, you need:
**for the backend:**
- An empty spring boot application from https://start.spring.io/ Language: `Java 11`, Build: `Maven`, Dependencies: `WEB / Spring Web` and `SQL / Spring Data JPA` and the dependency required for the database you want to use, for example: `SQL / MySQL Driver`
- A blank schema in your database for the project that will need to be set in the `application.yaml` or `application.properties` file

**for the frontend:**
- New empty `angular-frontend` project with `app-routing`, installed `bootstrap` and `jquery`.

## Generated files and directories:
With `Example` Domain Class and `SampleEnum` Enum Class ( we use enums for predefined values )

**frontend/src/app/**
- components
  - navbar
    - navbar.component.css
    - navbar.component.html
    - navbar.component.ts
  - example-detail
    - example-detail.component.css
    - example-detail.component.html
    - example-detail.component.ts
  - example-form
    - example-form.component.css
    - example-form.component.html
    - example-form.component.ts
  - example-list
    - example-list.component.css
    - example-list.component.html
    - example-list.component.ts
- models
  - ExampleCreateItem.model.ts
  - ExampleDetailItem.model.ts
  - ExampleFormData.model.ts (if Class have Enum field)
  - ExampleListItem.model.ts
  - ExampleShortListItem.model.ts (if Class is a filed in the other Class)
  - SampleEnumOption.model.ts
- services
  - Example.service.ts
- app.component.css (override)
- app.component.html (override)
- app.component.ts (override)
- app.module.ts (override)
- app-routing.module.ts (override)

**backend/**
- config
  - SpringWebConfig.java
- controller
  - ExampleController.java
- domain
  



