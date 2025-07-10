# GoSquad - Architecture Document

## Table of Contents
1. [Définition des Composants](#définition-des-composants)
2. [Choix Technologiques](#choix-technologiques)
3. [Organisation des Fichiers et Dossiers](#organisation-des-fichiers-et-dossiers)
4. [Architecture de l'Application](#architecture-de-lapplication)
   - [System Architecture Overview](#system-architecture-overview)
   - [Frontend Architecture](#frontend-architecture)
   - [Backend Architecture](#backend-architecture)
   - [Database Architecture](#database-architecture)
   - [API Architecture](#api-architecture)
5. [Modèle-Vue-Contrôleur (MVC)](#modèle-vue-contrôleur-mvc)
6. [Gestion des Dépendances](#gestion-des-dépendances)
7. [Performance et Scalabilité](#performance-et-scalabilité)
8. [Tests et Déploiement](#tests-et-déploiement)
9. [Maintenance et Évolutivité](#maintenance-et-évolutivité)
10. [Security Architecture](#security-architecture)
11. [Deployment Architecture](#deployment-architecture)
12. [Data Flow Diagrams](#data-flow-diagrams)
13. [Diagrammes](#diagrammes)
    - [Diagramme de Composants](#diagramme-de-composants)
    - [Diagramme de Classes (UML)](#diagramme-de-classes-uml)
    - [Diagramme de Séquence (UML)](#diagramme-de-séquence-uml)
    - [Diagramme de Cas d'Utilisation (UML)](#diagramme-de-cas-dutilisation-uml)
    - [Diagramme de Flux de Données (DFD)](#diagramme-de-flux-de-données-dfd)
    - [Diagramme ER (Entity-Relationship)](#diagramme-er-entity-relationship)

## Définition des Composants

GoSquad est une application de gestion de voyages pour les agences de voyage, construite avec une architecture moderne qui sépare clairement les composants frontend et backend. Le système suit un modèle client-serveur avec une communication API RESTful.

### Composants Principaux:
- **Frontend**: Application Angular (SPA - Single Page Application)
- **Backend**: API REST Spring Boot
- **Base de données**: PostgreSQL
- **Authentification**: Authentification basée sur JWT
- **Déploiement**: Vercel (Frontend), Railway/Heroku (Backend), PostgreSQL (Base de données)

## Choix Technologiques

### Frontend
- **Framework**: Angular 17+
- **UI Components**: Angular Material
- **State Management**: NgRx (optionnel)
- **HTTP Client**: Angular HttpClient
- **Styling**: CSS/SCSS
- **Testing**: Jasmine, Karma, Cypress

### Backend
- **Framework**: Spring Boot 3.x
- **API**: REST
- **Security**: Spring Security, JWT
- **Database Access**: JDBC
- **Testing**: JUnit, Mockito

### Base de données
- **SGBD**: PostgreSQL 15+
- **Migrations**: Flyway (optionnel)
- **Connection Pooling**: HikariCP

### DevOps
- **CI/CD**: GitHub Actions
- **Containerization**: Docker
- **Deployment**: Vercel, Railway/Heroku
- **Monitoring**: Prometheus, Grafana (optionnel)

## Organisation des Fichiers et Dossiers

### Structure Frontend (Angular)
```
front/
├── src/
│   ├── app/
│   │   ├── core/              # Composants et services partagés
│   │   │   ├── components/    # Composants réutilisables (header, navigation, etc.)
│   │   │   └── services/      # Services partagés (auth, api, etc.)
│   │   ├── features/          # Modules fonctionnels
│   │   │   ├── dashboard/
│   │   │   ├── clients/
│   │   │   ├── voyages/
│   │   │   ├── payments/
│   │   │   └── ...
│   │   ├── shared/            # Composants, directives et pipes partagés
│   │   ├── app.component.*    # Composant racine
│   │   ├── app.config.ts      # Configuration de l'application
│   │   └── app.routes.ts      # Routes de l'application
│   ├── assets/                # Ressources statiques (images, fonts, etc.)
│   └── styles.css             # Styles globaux
├── angular.json               # Configuration Angular
└── package.json               # Dépendances et scripts
```

### Structure Backend (Spring Boot)
```
api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── gosquad/
│   │   │           ├── core/              # Exceptions, utilitaires, etc.
│   │   │           ├── data/              # Couche d'accès aux données
│   │   │           │   ├── advisors/      # Repositories spécifiques
│   │   │           │   ├── clients/
│   │   │           │   └── ...
│   │   │           ├── domain/            # Logique métier
│   │   │           │   ├── advisors/      # Services spécifiques
│   │   │           │   ├── clients/
│   │   │           │   └── ...
│   │   │           ├── infrastructure/    # Configuration, sécurité, etc.
│   │   │           ├── presentation/      # Contrôleurs REST
│   │   │           └── ApiApplication.java # Point d'entrée de l'application
│   │   └── resources/
│   │       ├── application.properties     # Configuration de l'application
│   │       └── db/                        # Scripts SQL, migrations
│   └── test/                              # Tests unitaires et d'intégration
├── build.gradle                           # Configuration Gradle
└── settings.gradle                        # Configuration du projet
```

## Architecture de l'Application

### System Architecture Overview

GoSquad is built using a modern microservices architecture with a clear separation between frontend and backend components. The system follows a client-server model with RESTful API communication.

```
┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│                 │      │                 │      │                 │
│  Angular        │◄────►│  Spring Boot    │◄────►│  PostgreSQL     │
│  Frontend       │      │  Backend API    │      │  Database       │
│                 │      │                 │      │                 │
└─────────────────┘      └─────────────────┘      └─────────────────┘
```

### Key Components:
- **Frontend**: Angular-based SPA (Single Page Application)
- **Backend**: Spring Boot REST API
- **Database**: PostgreSQL relational database
- **Authentication**: JWT-based authentication
- **Deployment**: Vercel (Frontend), Railway/Heroku (Backend), PostgreSQL (Database)

## Frontend Architecture

The frontend is built with Angular and follows a feature-based architecture with a core module for shared components.

```
┌─────────────────────────────────────────────────────────────┐
│                       Angular Frontend                      │
├─────────────┬─────────────┬─────────────┬─────────────┬─────┘
│             │             │             │             │
│  Core       │  Features   │  Shared     │  Services   │  Assets
│  Components │  Modules    │  Components │             │
│             │             │             │             │
└─────────────┴─────────────┴─────────────┴─────────────┴─────┘
```

### Core Components:
- Header
- Feature Navigation
- Splashscreen

### Feature Modules:
- Dashboard
- Clients
- Voyages
- Payments
- Documents
- Activities
- Accommodations
- Transports
- Statistics
- Administration
- Support
- Notifications
- History

### Services:
- API Service: Handles communication with the backend
- Authentication Service: Manages user authentication and session
- Feature-specific services (Dashboard, Clients, etc.)

## Backend Architecture

The backend is built with Spring Boot and follows a layered architecture.

```
┌─────────────────────────────────────────────────────────────┐
│                    Spring Boot Backend                      │
├─────────────┬─────────────┬─────────────┬─────────────┬─────┘
│             │             │             │             │
│  Controllers│  Services   │  Repositories│ Models     │  Config
│             │             │             │             │
└─────────────┴─────────────┴─────────────┴─────────────┴─────┘
```

### Controllers:
- AuthController: Handles authentication and user management
- ClientController: Manages client data
- VoyageController: Manages travel data
- PaymentController: Handles payment processing
- DocumentController: Manages documents (invoices, quotes)
- AdvisorController: Manages travel advisors data
- AdviseController: Global exception handler
- HomeController: Provides welcome message for API root
- Other feature-specific controllers

### Services:
- AuthService: Business logic for authentication
- ClientService: Business logic for client management
- VoyageService: Business logic for travel management
- PaymentService: Business logic for payment processing
- DocumentService: Business logic for document management
- AdvisorService: Business logic for travel advisor management
- Other feature-specific services

### Repositories:
- PostgreSQL repositories for each entity (Client, Voyage, Payment, Advisor, etc.)
- JDBC-based data access with connection pooling

### Models:
- Client
- Voyage
- Payment
- Document (Invoice, Quote)
- User
- Advisor
- Other domain-specific models

### Configuration:
- Security Configuration (JWT)
- PostgreSQL Configuration (JDBC, connection pooling)
- CORS Configuration
- Other application configurations

## Database Architecture

GoSquad uses PostgreSQL as its primary database, with the following tables:

```
┌─────────────────────────────────────────────────────────────────────┐
│                       PostgreSQL Database                           │
├─────────────┬─────────────┬─────────────┬─────────────┬─────────────┬─────┘
│             │             │             │             │             │
│  Users      │  Clients    │  Voyages    │  Payments   │  Documents  │  Advisors
│             │             │             │             │             │
└─────────────┴─────────────┴─────────────┴─────────────┴─────────────┴─────┘
```

### Tables:
- **Users**: Store user information and authentication details
- **Clients**: Store client information
- **Voyages**: Store travel information
- **Payments**: Store payment information
- **Documents**: Store document information (invoices, quotes)
- **Advisors**: Store travel advisor information
- Other feature-specific tables

### Data Models:

#### User
```sql
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL, -- hashed
  role VARCHAR(20) NOT NULL, -- ADMIN, AGENT
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

#### Client
```sql
CREATE TABLE clients (
  id SERIAL PRIMARY KEY,
  nom VARCHAR(100) NOT NULL,
  prenom VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  telephone VARCHAR(20),
  adresse TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

#### Voyage
```sql
CREATE TABLE voyages (
  id SERIAL PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  destination VARCHAR(100) NOT NULL,
  date_depart DATE NOT NULL,
  date_retour DATE NOT NULL,
  participants INTEGER NOT NULL,
  budget DECIMAL(10, 2) NOT NULL,
  client_id INTEGER NOT NULL REFERENCES clients(id),
  statut VARCHAR(20) NOT NULL, -- PLANIFIE, EN_COURS, TERMINE
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Relations tables for many-to-many relationships
CREATE TABLE voyage_activites (
  voyage_id INTEGER NOT NULL REFERENCES voyages(id),
  activite_id INTEGER NOT NULL REFERENCES activites(id),
  PRIMARY KEY (voyage_id, activite_id)
);

CREATE TABLE voyage_hebergements (
  voyage_id INTEGER NOT NULL REFERENCES voyages(id),
  hebergement_id INTEGER NOT NULL REFERENCES hebergements(id),
  PRIMARY KEY (voyage_id, hebergement_id)
);

CREATE TABLE voyage_transports (
  voyage_id INTEGER NOT NULL REFERENCES voyages(id),
  transport_id INTEGER NOT NULL REFERENCES transports(id),
  PRIMARY KEY (voyage_id, transport_id)
);
```

#### Payment
```sql
CREATE TABLE payments (
  id SERIAL PRIMARY KEY,
  montant DECIMAL(10, 2) NOT NULL,
  date DATE NOT NULL,
  methode VARCHAR(20) NOT NULL, -- CARTE, VIREMENT, ESPECES
  statut VARCHAR(20) NOT NULL, -- EN_ATTENTE, COMPLETE, ANNULE
  facture_id INTEGER REFERENCES documents(id),
  client_id INTEGER NOT NULL REFERENCES clients(id),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

#### Document (Invoice/Quote)
```sql
CREATE TABLE documents (
  id SERIAL PRIMARY KEY,
  type VARCHAR(20) NOT NULL, -- FACTURE, DEVIS
  numero VARCHAR(50) NOT NULL UNIQUE,
  date DATE NOT NULL,
  montant DECIMAL(10, 2) NOT NULL,
  statut VARCHAR(20) NOT NULL, -- BROUILLON, ENVOYE, PAYE, ANNULE
  client_id INTEGER NOT NULL REFERENCES clients(id),
  voyage_id INTEGER NOT NULL REFERENCES voyages(id),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE document_lignes (
  id SERIAL PRIMARY KEY,
  document_id INTEGER NOT NULL REFERENCES documents(id),
  description TEXT NOT NULL,
  quantite INTEGER NOT NULL,
  prix_unitaire DECIMAL(10, 2) NOT NULL,
  montant DECIMAL(10, 2) NOT NULL
);
```

#### Advisor
```sql
CREATE TABLE advisors (
  id SERIAL PRIMARY KEY,
  firstname VARCHAR(100) NOT NULL,
  lastname VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  phone_number VARCHAR(20),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## API Architecture

The API follows RESTful principles with the following endpoints:

### Authentication
```
POST /api/auth/register  - Register a new user
POST /api/auth/login     - Login and generate JWT
GET  /api/auth/me        - Get current user information
```

### Clients
```
POST   /api/clients       - Add a new client
GET    /api/clients       - Get all clients
GET    /api/clients/{id}  - Get a specific client
PUT    /api/clients/{id}  - Update a client
DELETE /api/clients/{id}  - Delete a client
```

### Voyages
```
POST   /api/voyages       - Add a new voyage
GET    /api/voyages       - Get all voyages
GET    /api/voyages/{id}  - Get a specific voyage
PUT    /api/voyages/{id}  - Update a voyage
DELETE /api/voyages/{id}  - Delete a voyage
```

### Payments
```
POST   /api/payments       - Add a new payment
GET    /api/payments       - Get all payments
GET    /api/payments/{id}  - Get a specific payment
PUT    /api/payments/{id}  - Update a payment
DELETE /api/payments/{id}  - Delete a payment
```

### Documents
```
POST   /api/documents       - Create a new document
GET    /api/documents       - Get all documents
GET    /api/documents/{id}  - Get a specific document
PUT    /api/documents/{id}  - Update a document
DELETE /api/documents/{id}  - Delete a document
```

### Advisors
```
GET    /getAllAdvisor       - Get all advisors
GET    /getAdvisor/{id}     - Get a specific advisor by ID
```

## Modèle-Vue-Contrôleur (MVC)

GoSquad implémente le pattern MVC (Modèle-Vue-Contrôleur) pour séparer les responsabilités entre les données (modèle), l'interface utilisateur (vue) et la logique de l'application (contrôleur).

### Modèle (Model)
- Représente les données et la logique métier
- Implémenté dans la couche domain et data du backend
- Entités, services et repositories

### Vue (View)
- Représente l'interface utilisateur
- Implémentée dans le frontend Angular
- Composants, templates et styles

### Contrôleur (Controller)
- Gère les interactions entre le modèle et la vue
- Implémenté dans la couche presentation du backend (contrôleurs REST)
- Reçoit les requêtes HTTP, appelle les services appropriés et renvoie les réponses

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│             │     │             │     │             │
│  Vue        │────►│ Contrôleur  │────►│  Modèle     │
│  (Angular)  │     │  (Spring)   │     │ (Services,  │
│             │◄────│             │◄────│ Repositories)│
└─────────────┘     └─────────────┘     └─────────────┘
```

## Gestion des Dépendances

### Frontend (npm)
- Gestion des dépendances via npm/yarn
- package.json pour définir les dépendances
- node_modules pour stocker les dépendances

### Backend (Gradle)
- Gestion des dépendances via Gradle
- build.gradle pour définir les dépendances
- Dépendances principales:
  - spring-boot-starter-web
  - spring-boot-starter-security
  - spring-boot-starter-jdbc
  - postgresql
  - jjwt (JSON Web Token)
  - lombok

## Performance et Scalabilité

### Stratégies de Performance
- **Frontend**:
  - Lazy loading des modules Angular
  - Optimisation des bundles (tree-shaking, minification)
  - Mise en cache des données
  - Compression des assets
- **Backend**:
  - Connection pooling pour la base de données
  - Mise en cache des données fréquemment accédées
  - Pagination des résultats
  - Optimisation des requêtes SQL
- **Base de données**:
  - Indexation appropriée
  - Optimisation des requêtes
  - Partitionnement (pour les grandes tables)

### Stratégies de Scalabilité
- **Frontend**:
  - Déploiement sur CDN
  - Architecture stateless
- **Backend**:
  - Architecture stateless
  - Conteneurisation avec Docker
  - Scaling horizontal (multiple instances)
  - Load balancing
- **Base de données**:
  - Réplication
  - Sharding (pour les très grandes bases de données)
  - Read replicas pour les opérations de lecture intensives

## Tests et Déploiement

### Stratégie de Test
- **Tests Unitaires**:
  - Frontend: Jasmine/Karma
  - Backend: JUnit, Mockito
- **Tests d'Intégration**:
  - Backend: Spring Boot Test
- **Tests End-to-End**:
  - Cypress
- **Tests de Performance**:
  - JMeter (optionnel)

### Pipeline CI/CD
- **Intégration Continue**:
  - GitHub Actions
  - Exécution automatique des tests
  - Analyse de code statique (SonarQube, optionnel)
- **Déploiement Continu**:
  - Déploiement automatique sur les environnements de test
  - Déploiement manuel sur production après approbation

### Environnements
- **Développement**: Environnement local
- **Test**: Pour QA et tests
- **Staging**: Environnement de pré-production
- **Production**: Environnement de production

## Maintenance et Évolutivité

### Maintenance
- **Monitoring**:
  - Logs centralisés
  - Métriques d'application
  - Alertes sur incidents
- **Backup**:
  - Sauvegarde régulière de la base de données
  - Stratégie de restauration
- **Mises à jour**:
  - Mises à jour de sécurité
  - Mises à jour des dépendances

### Évolutivité
- **Architecture modulaire**:
  - Facilite l'ajout de nouvelles fonctionnalités
  - Permet de remplacer des composants sans affecter l'ensemble du système
- **API versionnée**:
  - Permet d'évoluer l'API sans casser la compatibilité
- **Documentation**:
  - Documentation technique à jour
  - Documentation API (Swagger/OpenAPI)

## Security Architecture

GoSquad implements a comprehensive security architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                    Security Architecture                    │
├─────────────┬─────────────┬─────────────┬─────────────┬─────┘
│             │             │             │             │
│  JWT Auth   │  HTTPS      │  Role-based │  Password   │  CORS
│             │             │  Access     │  Encryption │  Policy
└─────────────┴─────────────┴─────────────┴─────────────┴─────┘
```

### Key Security Features:
- **JWT Authentication**: Secure token-based authentication
- **HTTPS**: Encrypted communication
- **Role-based Access Control**: Different permissions for different user roles
- **Password Encryption**: Secure password storage with bcrypt
- **CORS Policy**: Controlled cross-origin resource sharing
- **Input Validation**: Prevent injection attacks
- **Rate Limiting**: Prevent brute force attacks
- **GDPR Compliance**: Data protection measures

## Deployment Architecture

GoSquad uses a cloud-based deployment architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                   Deployment Architecture                   │
├─────────────┬─────────────┬─────────────┬─────────────┬─────┘
│             │             │             │             │
│  Vercel     │  Railway/   │  MongoDB    │  CI/CD      │  Monitoring
│  (Frontend) │  Heroku     │  Atlas      │  Pipeline   │  & Logging
│             │  (Backend)  │  (Database) │             │
└─────────────┴─────────────┴─────────────┴─────────────┴─────┘
```

### Deployment Components:
- **Frontend**: Deployed on Vercel for fast global CDN
- **Backend**: Deployed on Railway or Heroku for scalable API hosting
- **Database**: MongoDB Atlas for managed database service
- **CI/CD Pipeline**: Automated testing and deployment
- **Monitoring & Logging**: Application performance monitoring and centralized logging

### Deployment Environments:
- **Development**: Local development environment
- **Testing**: For QA and testing
- **Staging**: Pre-production environment
- **Production**: Live environment

## Data Flow Diagrams

### Authentication Flow
```
┌─────────┐     ┌─────────┐     ┌─────────┐     ┌─────────┐
│         │     │         │     │         │     │         │
│  User   │────►│ Angular │────►│  Spring │────►│PostgreSQL│
│         │     │ Frontend│     │ Backend │     │         │
│         │◄────│         │◄────│         │◄────│         │
└─────────┘     └─────────┘     └─────────┘     └─────────┘
    │               │               │               │
    │               │               │               │
    └───────────────▼───────────────▼───────────────┘
                    │
                    ▼
              JWT Token Flow
```

### Client Management Flow
```
┌─────────┐     ┌─────────┐     ┌─────────┐     ┌─────────┐
│         │     │         │     │         │     │         │
│  Agent  │────►│ Angular │────►│  Spring │────►│PostgreSQL│
│         │     │ Frontend│     │ Backend │     │         │
│         │◄────│         │◄────│         │◄────│         │
└─────────┘     └─────────┘     └─────────┘     └─────────┘
    │               │               │               │
    │               │               │               │
    └───────────────▼───────────────▼───────────────┘
                    │
                    ▼
            Client Data Flow
```

### Voyage Management Flow
```
┌─────────┐     ┌─────────┐     ┌─────────┐     ┌─────────┐
│         │     │         │     │         │     │         │
│  Agent  │────►│ Angular │────►│  Spring │────►│PostgreSQL│
│         │     │ Frontend│     │ Backend │     │         │
│         │◄────│         │◄────│         │◄────│         │
└─────────┘     └─────────┘     └─────────┘     └─────────┘
    │               │               │               │
    │               │               │               │
    └───────────────▼───────────────▼───────────────┘
                    │
                    ▼
            Voyage Data Flow
```

### Payment Processing Flow
```
┌─────────┐     ┌─────────┐     ┌─────────┐     ┌─────────┐     ┌─────────┐
│         │     │         │     │         │     │         │     │         │
│  Client │────►│ Angular │────►│  Spring │────►│ Payment │────►│PostgreSQL│
│         │     │ Frontend│     │ Backend │     │ Gateway │     │         │
│         │◄────│         │◄────│         │◄────│         │◄────│         │
└─────────┘     └─────────┘     └─────────┘     └─────────┘     └─────────┘
    │               │               │               │               │
    │               │               │               │               │
    └───────────────▼───────────────▼───────────────▼───────────────┘
                    │
                    ▼
            Payment Data Flow
```

### Document Generation Flow
```
┌─────────┐     ┌─────────┐     ┌─────────┐     ┌─────────┐
│         │     │         │     │         │     │         │
│  Agent  │────►│ Angular │────►│  Spring │────►│PostgreSQL│
│         │     │ Frontend│     │ Backend │     │         │
│         │◄────│         │◄────│         │◄────│         │
└─────────┘     └─────────┘     └─────────┘     └─────────┘
    │               │               │               │
    │               │               │               │
    └───────────────▼───────────────▼───────────────┘
                    │
                    ▼
          Document Generation Flow
```

### Advisor Management Flow
```
┌─────────┐     ┌─────────┐     ┌─────────┐     ┌─────────┐
│         │     │         │     │         │     │         │
│  Agent  │────►│ Angular │────►│  Spring │────►│PostgreSQL│
│         │     │ Frontend│     │ Backend │     │         │
│         │◄────│         │◄────│         │◄────│         │
└─────────┘     └─────────┘     └─────────┘     └─────────┘
    │               │               │               │
    │               │               │               │
    └───────────────▼───────────────▼───────────────┘
                    │
                    ▼
          Advisor Management Flow
```

## Diagrammes

### Diagramme de Composants

Le diagramme de composants montre les principaux composants du système et leurs interactions.

```
┌───────────────────────────────────────────────────────────────────────┐
│                           GoSquad System                              │
├───────────────┬───────────────────────────────────┬───────────────────┤
│               │                                   │                   │
│  ┌───────────┐│  ┌───────────────────────────┐   │  ┌───────────┐    │
│  │           ││  │                           │   │  │           │    │
│  │  Angular  ││  │        Spring Boot        │   │  │PostgreSQL │    │
│  │  Frontend ││  │        Backend API        │   │  │ Database  │    │
│  │           ││  │                           │   │  │           │    │
│  └─────┬─────┘│  └─────────────┬─────────────┘   │  └─────┬─────┘    │
│        │      │                │                 │        │          │
└────────┼──────┴────────────────┼─────────────────┴────────┼──────────┘
         │                       │                          │
         │  HTTP/REST            │  JDBC                    │
         └───────────────────────┘◄─────────────────────────┘
```

### Diagramme de Classes (UML)

Le diagramme de classes montre les principales classes du système et leurs relations.

```
┌───────────────┐     ┌───────────────┐     ┌───────────────┐
│    Entity     │     │     Model     │     │  Repository   │
├───────────────┤     ├───────────────┤     ├───────────────┤
│ - id: Integer │     │ - id: Integer │     │ - tableName   │
├───────────────┤     ├───────────────┤     ├───────────────┤
│ + getId()     │     │ + getId()     │     │ + getAll()    │
│ + setId()     │     │ + setId()     │     │ + getById()   │
└───────┬───────┘     └───────┬───────┘     └───────┬───────┘
        │                     │                     │
        │                     │                     │
┌───────▼───────┐     ┌───────▼───────┐     ┌───────▼───────────┐
│ AdvisorEntity │     │ AdvisorModel  │     │AdvisorRepository  │
├───────────────┤     ├───────────────┤     ├───────────────────┤
│ - firstname   │     │ - firstname   │     │                   │
│ - lastname    │     │ - lastname    │     ├───────────────────┤
│ - email       │     │ - email       │     │ + getAll()        │
│ - phone       │     │ - phone       │     │ + getById()       │
├───────────────┤     ├───────────────┤     └───────────────────┘
│ + getters     │     │ + getters     │               │
│ + setters     │     │ + setters     │               │
└───────────────┘     └───────────────┘               │
                                                      │
                                        ┌─────────────▼─────────────┐
                                        │  AdvisorRepositoryImpl    │
                                        ├───────────────────────────┤
                                        │ - TABLE_NAME              │
                                        ├───────────────────────────┤
                                        │ + getAll()                │
                                        │ + getById()               │
                                        │ + mapResultSetToEntity()  │
                                        └───────────────────────────┘
```

### Diagramme de Séquence (UML)

Le diagramme de séquence montre les interactions entre les composants lors d'une requête pour obtenir un conseiller par ID.

```
┌─────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│         │     │             │     │             │     │             │     │             │
│  Client │     │ Controller  │     │  Service    │     │ Repository  │     │ PostgreSQL  │
│         │     │             │     │             │     │             │     │             │
└────┬────┘     └──────┬──────┘     └──────┬──────┘     └──────┬──────┘     └──────┬──────┘
     │                 │                   │                   │                   │
     │  GET /advisor/1 │                   │                   │                   │
     │────────────────►│                   │                   │                   │
     │                 │                   │                   │                   │
     │                 │  getAdvisorById(1)│                   │                   │
     │                 │──────────────────►│                   │                   │
     │                 │                   │                   │                   │
     │                 │                   │  getById(1)       │                   │
     │                 │                   │──────────────────►│                   │
     │                 │                   │                   │                   │
     │                 │                   │                   │  SELECT * FROM    │
     │                 │                   │                   │  advisor WHERE    │
     │                 │                   │                   │  id = 1           │
     │                 │                   │                   │──────────────────►│
     │                 │                   │                   │                   │
     │                 │                   │                   │  Result Set       │
     │                 │                   │                   │◄──────────────────│
     │                 │                   │                   │                   │
     │                 │                   │  AdvisorEntity    │                   │
     │                 │                   │◄──────────────────│                   │
     │                 │                   │                   │                   │
     │                 │  AdvisorEntity    │                   │                   │
     │                 │◄──────────────────│                   │                   │
     │                 │                   │                   │                   │
     │  JSON Response  │                   │                   │                   │
     │◄────────────────│                   │                   │                   │
     │                 │                   │                   │                   │
```

### Diagramme de Cas d'Utilisation (UML)

Le diagramme de cas d'utilisation montre les principales fonctionnalités du système du point de vue des utilisateurs.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                GoSquad System                               │
│                                                                             │
│  ┌──────────┐                                                               │
│  │          │                                                               │
│  │  Agent   │                                                               │
│  │          │                                                               │
│  └────┬─────┘                                                               │
│       │                                                                     │
│       │    ┌─────────────────────┐     ┌─────────────────────┐             │
│       ├───►│                     │     │                     │             │
│       │    │  Gérer les clients  │     │  Gérer les voyages  │             │
│       ├───►│                     │     │                     │             │
│       │    └─────────────────────┘     └─────────────────────┘             │
│       │                                                                     │
│       │    ┌─────────────────────┐     ┌─────────────────────┐             │
│       ├───►│                     │     │                     │             │
│       │    │  Gérer les devis    │     │  Gérer les factures │             │
│       ├───►│                     │     │                     │             │
│       │    └─────────────────────┘     └─────────────────────┘             │
│       │                                                                     │
│       │    ┌─────────────────────┐     ┌─────────────────────┐             │
│       ├───►│                     │     │                     │             │
│       │    │  Gérer les paiements│     │Gérer les conseillers│             │
│       └───►│                     │     │                     │             │
│            └─────────────────────┘     └─────────────────────┘             │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Diagramme de Flux de Données (DFD)

Le diagramme de flux de données montre comment les données circulent dans le système.

```
┌─────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│         │     │             │     │             │     │             │
│  Agent  │────►│ Gestion des │────►│ Gestion des │────►│ Gestion des │
│         │     │  Clients    │     │  Voyages    │     │  Documents  │
│         │◄────│             │◄────│             │◄────│             │
└─────────┘     └─────────────┘     └─────────────┘     └─────────────┘
                       │                   │                   │
                       │                   │                   │
                       ▼                   ▼                   ▼
                 ┌─────────────────────────────────────────────┐
                 │                                             │
                 │              Base de données                │
                 │              PostgreSQL                     │
                 │                                             │
                 └─────────────────────────────────────────────┘
```

### Diagramme ER (Entity-Relationship)

Le diagramme ER montre les entités principales du système et leurs relations.

```
┌───────────┐       ┌───────────┐       ┌───────────┐
│           │       │           │       │           │
│   Users   │       │  Clients  │       │  Voyages  │
│           │       │           │       │           │
└───────────┘       └─────┬─────┘       └─────┬─────┘
                          │                   │
                          │ 1:N               │ 1:N
                          │                   │
┌───────────┐       ┌─────▼─────┐       ┌─────▼─────┐
│           │       │           │       │           │
│  Advisors │       │ Documents │◄──────┤ Payments  │
│           │       │           │       │           │
└───────────┘       └───────────┘       └───────────┘
```
