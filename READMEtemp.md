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
```
├── components
│   ├── navbar
│   │   ├── navbar.component.css
│   │   ├── navbar.component.html
│   │   └── navbar.component.ts
│   ├── example-detail (details of the selected item)
│   │   ├── example-detail.component.css
│   │   ├── example-detail.component.html
│   │   └── example-detail.component.ts
│   ├── example-form (for create and update item)
│   │   ├── example-form.component.css
│   │   ├── example-form.component.html
│   │   └── example-form.component.ts
│   └── example-list (items list view)
│       ├── example-list.component.css
│       ├── example-list.component.html
│       └── example-list.component.ts
├── models
│   ├── ExampleCreateItem.model.ts
│   ├── ExampleDetailItem.model.ts
│   ├── ExampleFormData.model.ts (if Class has Enum field)
│   ├── ExampleListItem.model.ts
│   ├── ExampleShortListItem.model.ts (if Class is a filed in the other Class)
│   └── SampleEnumOption.model.ts
├── services
│   └── Example.service.ts
├── app.component.css (override)
├── app.component.html (override)
├── app.component.ts (override)
├── app.module.ts (override)
└── app-routing.module.ts (override)
```
**backend/**
```
├── config
│   └── SpringWebConfig.java (CORS policy)
├── controller
│   └── ExampleController.java *
├── domain
│   ├── Example.java
│   └── SampleEnum.java
├── dto
│   ├── ExampleCreateItem.java
│   ├── ExampleDetailItem.java
│   ├── ExampleFormData.java (if Class has Enum field)
│   ├── ExampleListItem.java
│   ├── ExampleShortListItem.java (if Class is a filed in the other Class)
│   └── SampleEnumOption.java
├── exception (for validation - under construction!)
│   ├── ApiError.java
│   ├── GlobalExceptionHandler.java
│   └── ValidationError.java
├── repository
│   └── ExampleRepository.java
├── service
│   └── ExampleService.java
├── validator (this function under construction!)
│   └── ExampleValidator.java
└── "ProjectName"Application.java (override)
``` 
**\* HTTP requests handled by the controller:**
- `@GetMapping("/formData")` Preliminary data required to create the item (predefined selectable values)
- `@PostMapping` Create a new item
- `@PutMapping("/{id}")` Update the item with the specified id
- `@DeleteMapping("/{id}")` Delete the item with the specified id
- `@GetMapping` Get list of existing items
- `@GetMapping("/{id}")` Get details of the items with the specified id
- `@GetMapping("/formData/{id}")` Preliminary data required to modify the element 

