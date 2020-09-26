# SpringBootus

Create Angular frontend and Java11/SpringBoot backend based on a given domain structure

## Technologies used
`Java11` `Spring Boot` `SQL` `JPA` `Hibernate` `HTML/CSS` `Bootstrap` `JavaScript` `TypeScript` `Angular` `JavaFX`

## To get started, you need:
**for the backend:**
- An empty spring boot application from https://start.spring.io/ Language: `Java 11`, Build: `Maven`, Dependencies: `WEB / Spring Web` and `SQL / Spring Data JPA` and a dependency required for the database you want to use, for example: `SQL / MySQL Driver`
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
  - `example`-detail *(details of the selected item)*
    - `example`-detail.component.css
    - `example`-detail.component.html
    - `example`-detail.component.ts
  - `example`-form *(for create and update item)*
    - `example`-form.component.css
    - `example`-form.component.html
    - `example`-form.component.ts
  - `example`-list *(items list view)*
    - `example`-list.component.css
    - `example`-list.component.html
    - `example`-list.component.ts
- models
  - `Example`CreateItem.model.ts
  - `Example`DetailItem.model.ts
  - `Example`FormData.model.ts *(if Class have Enum field)*
  - `Example`ListItem.model.ts
  - `Example`ShortListItem.model.ts *(if Class is a filed in the other Class)*
  - `SampleEnum`Option.model.ts
- services
  - `Example`.service.ts
- app.component.css *(override)*
- app.component.html *(override)*
- app.component.ts *(override)*
- app.module.ts *(override)*
- app-routing.module.ts *(override)*

**backend/**
- config
  - SpringWebConfig.java
- controller
  - `Example`Controller.java
- domain
  - `Example`.java
  - `SampleEnum`.java
- dto
  - `Example`CreateItem.java
  - `Example`DetailItem.java
  - `Example`FormData.java *(if Class have Enum field)*
  - `Example`ListItem.java
  - `Example`ShortListItem.java *(if Class is a filed in the other Class)*
  - `SampleEnum`Option.java
- exception *(for validation - under construction!)*
  - ApiError.java
  - GlobalExceptionHandler.java
  - ValidationError.java
- repository
  - `Example`Repository.java
- service
  - `Example`Service.java
- validator *(this function under construction!)*
  - `Example`Validator.java
- `ProjectName`Application.java *(override)*
  



