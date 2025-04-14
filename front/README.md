# Front

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 19.0.7.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Karma](https://karma-runner.github.io) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.

# GoSquad Frontend

This is the frontend application for GoSquad, a travel agency management system.

## Architecture

The frontend is built with Angular and follows a feature-based architecture with a clear separation between container and presenter components.

### Core Concepts

#### Container/Presenter Pattern

The application follows the Container/Presenter pattern (also known as Smart/Dumb components):

- **Container Components**: These components are responsible for fetching data, managing state, and handling business logic. They use services to interact with the backend API and pass data to presenter components.
- **Presenter Components**: These components are responsible for displaying data and capturing user input. They receive data via `@Input()` properties and emit events via `@Output()` properties. They don't interact with services directly.

This pattern provides several benefits:
- Clear separation of concerns
- Improved testability
- Reusable UI components
- Simplified state management

#### Directory Structure

```
front/
├── src/
│   ├── app/
│   │   ├── core/              # Core components and services
│   │   │   ├── components/    # Shared components
│   │   │   ├── models/        # Data models/interfaces
│   │   │   └── services/      # API services
│   │   ├── features/          # Feature modules
│   │   │   ├── clients/       # Client management feature
│   │   │   │   ├── presenters/# Presenter components
│   │   │   │   └── ...
│   │   │   ├── voyages/
│   │   │   ├── payments/
│   │   │   └── ...
│   │   ├── shared/            # Shared components, directives, pipes
│   │   └── ...
│   └── ...
└── ...
```

### Services

The application uses services to interact with the backend API:

- **ApiService**: Core service for making HTTP requests to the backend
- **AdvisorService**: Service for advisor-related operations
- **ClientService**: Service for client-related operations
- **VoyageService**: Service for voyage-related operations
- **PaymentService**: Service for payment-related operations
- **DocumentService**: Service for document-related operations

### Data Models

Data models are defined as TypeScript interfaces in the `core/models` directory. These interfaces are used throughout the application to ensure type safety.

## Development

### Prerequisites

- Node.js 16+
- npm 8+

### Setup

1. Clone the repository
2. Navigate to the `front` directory
3. Run `npm install` to install dependencies
4. Run `npm start` to start the development server

### Building

Run `npm run build` to build the application for production.

### Testing

Run `npm test` to execute the unit tests.
