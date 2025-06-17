# Fonctionnalité Gestion des Voyages - GoSquad

## Vue d'ensemble

Cette fonctionnalité permet la gestion complète des voyages dans l'application GoSquad, incluant la création, modification, suppression et génération de fiches voyage.

## Fonctionnalités

### ✅ Gestion CRUD des Voyages
- **Créer** un nouveau voyage avec toutes les informations requises
- **Lire** la liste des voyages et les détails d'un voyage spécifique
- **Modifier** les informations d'un voyage existant
- **Supprimer** un voyage

### ✅ Génération de Fiches Voyage
- **Fiche HTML** : Visualisation directe dans le navigateur
- **Fiche PDF** : Téléchargement pour impression ou archivage
- **Design professionnel** avec logo et mise en forme

### ✅ Validation des Données
- Validation côté backend avec messages d'erreur explicites
- Vérification de la cohérence des dates
- Contrôle des valeurs numériques (participants > 0, budget > 0)

## Structure des Données

### Modèle Voyage
```typescript
interface Voyage {
  id: number;
  titre: string;
  destination: string;
  date_depart: string;
  date_retour: string;
  participants: number;
  budget: number;
  client_id: number;
  statut: 'PLANIFIE' | 'EN_COURS' | 'TERMINE';
}
```

## Architecture Backend

### Couches Implémentées

#### 1. Domain Layer
- `VoyageEntity.java` : Entité métier avec validation

#### 2. Infrastructure Layer
- `VoyageModel.java` : Modèle de données
- `VoyageRepository.java` : Interface repository
- `VoyageRepositoryImpl.java` : Implémentation JDBC

#### 3. UseCase Layer
- `VoyageService.java` : Interface service métier
- `VoyageServiceImpl.java` : Logique métier et validation
- `VoyageMapper.java` : Conversion Entity ↔ Model
- `VoyageFicheService.java` : Service de génération de fiches
- `VoyageFicheServiceImpl.java` : Implémentation génération HTML/PDF

#### 4. Presentation Layer
- `VoyageController.java` : Contrôleur REST
- `VoyageRequestDTO.java` : DTO pour les requêtes

## API Endpoints

### Gestion des Voyages
```
GET    /api/voyages              - Liste tous les voyages
GET    /api/voyages/{id}         - Détails d'un voyage
GET    /api/voyages/client/{id}  - Voyages d'un client
POST   /api/voyages              - Créer un voyage
PUT    /api/voyages/{id}         - Modifier un voyage
DELETE /api/voyages/{id}         - Supprimer un voyage
```

### Génération de Fiches
```
GET    /api/voyages/{id}/fiche     - Fiche HTML
GET    /api/voyages/{id}/fiche/pdf - Télécharger PDF
```

## Frontend Angular

### Composants
- `VoyagesComponent` : Composant principal de gestion
- Interface utilisateur responsive avec formulaires
- Actions CRUD avec feedback utilisateur

### Services
- `VoyageService` : Communication avec l'API backend
- Méthodes pour toutes les opérations CRUD
- Support génération de fiches

## Base de Données

### Table `voyages`
```sql
CREATE TABLE voyages (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    date_depart DATE NOT NULL,
    date_retour DATE NOT NULL,
    participants INTEGER NOT NULL CHECK (participants > 0),
    budget DECIMAL(10, 2) NOT NULL CHECK (budget > 0),
    client_id INTEGER NOT NULL,
    statut VARCHAR(20) NOT NULL DEFAULT 'PLANIFIE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Index et Contraintes
- Index sur `client_id`, `statut`, `date_depart`
- Contraintes de validation sur `participants` et `budget`
- Trigger automatique pour `updated_at`

## Tests

### Tests Unitaires Backend
- `VoyageServiceTest.java` : Tests complets du service
- Couverture des cas nominaux et d'erreur
- Mocking des dépendances avec Mockito

### Cas de Test Couverts
- ✅ Création de voyage valide
- ✅ Validation des champs obligatoires
- ✅ Validation des dates cohérentes
- ✅ Validation des valeurs numériques
- ✅ Récupération par ID
- ✅ Mise à jour
- ✅ Suppression
- ✅ Gestion des erreurs

## Génération de Fiches

### Fiche HTML
- Design responsive et professionnel
- Informations complètes du voyage et du client
- Statut coloré selon l'état du voyage
- Footer avec date de génération

### Fiche PDF
- Conversion HTML vers PDF (base implémentée)
- Téléchargement direct depuis l'interface
- Nom de fichier automatique : `fiche-voyage-{id}.pdf`

## Installation et Configuration

### Prérequis
- Java 17+
- Spring Boot 3.x
- PostgreSQL 15+
- Angular 17+
- Node.js 18+

### Configuration Backend
1. Ajouter les dépendances dans `build.gradle`
2. Exécuter le script de migration SQL
3. Configurer la base de données dans `application.properties`

### Configuration Frontend
1. Les services et composants sont déjà intégrés
2. Vérifier les imports Angular dans `app.module.ts`
3. Ajouter les routes si nécessaire

## Utilisation

### Créer un Voyage
1. Cliquer sur "Nouveau Voyage"
2. Remplir le formulaire avec :
   - Titre du voyage
   - Destination
   - Dates de départ et retour
   - Nombre de participants
   - Budget
   - ID du client
   - Statut
3. Valider le formulaire

### Générer une Fiche
1. Dans la liste des voyages, cliquer sur "Fiche" pour voir en HTML
2. Cliquer sur "PDF" pour télécharger la fiche

## Évolutions Futures

### Améliorations Possibles
- [ ] Intégration avec un vrai générateur PDF (iText, Flying Saucer)
- [ ] Sélecteur de client avec autocomplete
- [ ] Upload de documents attachés au voyage
- [ ] Notifications automatiques aux clients
- [ ] Gestion des activités, hébergements et transports
- [ ] Calcul automatique du budget basé sur les composants
- [ ] Historique des modifications
- [ ] Export Excel de la liste des voyages

### Optimisations Techniques
- [ ] Cache Redis pour les données fréquemment accédées
- [ ] Pagination pour les grandes listes
- [ ] Recherche et filtres avancés
- [ ] Validation côté frontend avec Angular Reactive Forms
- [ ] Internationalisation (i18n)

## Maintenance

### Logs et Monitoring
- Logs applicatifs dans les services
- Métriques de performance à ajouter
- Monitoring des erreurs de génération PDF

### Sécurité
- Validation des autorisations d'accès aux voyages
- Audit trail des modifications
- Chiffrement des données sensibles

---

**Développé pour GoSquad - Agence de Voyage**  
*Version 1.0 - Décembre 2024*