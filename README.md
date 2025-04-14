# GoSquad - Plateforme de Gestion pour Agences de Voyage

GoSquad est une application complète de gestion de voyages pour les agences de voyage, conçue pour simplifier la gestion des clients, des voyages, des devis et des factures.

Projet Annuel ESGI 2025

## 🚀 État du Projet

Le projet est actuellement en développement actif. Voici l'état d'avancement des différentes fonctionnalités :

### Fonctionnalités Implémentées
✅ Définition des **besoins** et rédaction du cahier des charges.  
✅ Élaboration des **modèles de données** (clients, voyages, activités, devis, facturation…).  
✅ Création du **schéma API** (routes REST, endpoints principaux).  
✅ Mise en place de l’**environnement de développement** (Git, Angular, Spring Boot, MongoDB).  
✅ Configuration de **l’authentification** avec JWT.  

### Fonctionnalités en Cours de Développement
🔄 Développement des **modules essentiels** :
- 🔄 Gestion des utilisateurs (connexion, rôles, permissions).  
- 🔄 Création et gestion des **clients**.  
- 🔄 Création et gestion des **voyages** (destination, budget, participants).  
- 🔄 Création et gestion des **devis et factures**.  
- 🔄 Suivi des **paiements** et rappels automatiques.  
✅ Déploiement d’un **premier prototype** sur Heroku/Vercel pour tests internes.  

### Fonctionnalités Planifiées
📅 Ajout des **suggestions automatiques** basées sur le budget.  
📅 Intégration de **paiements en ligne** (Stripe, PayPal).  
📅 Système de **notifications** (email, push).  
📅 Intégration avec **Google Calendar**.  
📅 Système de **chat en ligne**.  
📅 Optimisation de la sécurité et conformité RGPD.  

📅 Tests unitaires et E2E avec Cypress/JUnit.  
📅 Corrections des bugs et optimisation des performances.  
📅 Déploiement final et communication avec les agences.  

# Architecture Technique

Pour une documentation détaillée de l'architecture, veuillez consulter le fichier [ARCHITECTURE.md](ARCHITECTURE.md).

## 🛠️ Stack Technique
| Composant  | Technologie |
|------------|-------------|
| **Frontend** | Angular 17+, Angular Material |
| **Backend** | Spring Boot 3.x (REST API) |
| **Base de données** | PostgreSQL 15+ |
| **Auth & Sécurité** | JWT, Spring Security |
| **Déploiement** | Vercel (Frontend), Railway/Heroku (Backend), PostgreSQL (Database) |
| **DevOps** | Docker, GitHub Actions |

## 🔌 API REST
### Authentification (Planifiée)
```
POST /api/auth/register  - Inscription d’un utilisateur
POST /api/auth/login     - Connexion et génération d’un JWT
GET  /api/auth/me        - Récupérer les infos de l’utilisateur connecté
```

### Gestion des clients (En développement)
```
POST   /api/clients       - Ajouter un nouveau client
GET    /api/clients       - Récupérer la liste des clients
GET    /api/clients/{id}  - Récupérer un client spécifique
PUT    /api/clients/{id}  - Modifier un client
DELETE /api/clients/{id}  - Supprimer un client
```

### Gestion des voyages (En développement)
```
POST   /api/voyages       - Ajouter un voyage
GET    /api/voyages       - Récupérer la liste des voyages
GET    /api/voyages/{id}  - Récupérer un voyage spécifique
PUT    /api/voyages/{id}  - Modifier un voyage
DELETE /api/voyages/{id}  - Supprimer un voyage
```

### Gestion des conseillers (Implémentée)
```
GET    /getAllAdvisor     - Récupérer la liste des conseillers
GET    /getAdvisor/{id}   - Récupérer un conseiller spécifique
```

### Gestion des devis & factures (Planifiée)
```
POST   /api/devis       - Créer un devis
GET    /api/devis/{id}  - Récupérer un devis
POST   /api/factures    - Générer une facture
GET    /api/factures/{id} - Récupérer une facture
```

## 📋 Structure du Projet

```
projet/
├── api/                  # Backend Spring Boot
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── gosquad/
│           │           ├── core/              # Exceptions, utilitaires
│           │           ├── data/              # Couche d'accès aux données
│           │           ├── domain/            # Logique métier
│           │           ├── infrastructure/    # Configuration, sécurité
│           │           └── presentation/      # Contrôleurs REST
│           └── resources/                     # Configuration, migrations
├── front/                # Frontend Angular
│   └── src/
│       └── app/
│           ├── core/                          # Composants et services partagés
│           ├── features/                      # Modules fonctionnels
│           │   ├── clients/
│           │   ├── voyages/
│           │   ├── payments/
│           │   └── ...
│           └── shared/                        # Composants partagés
└── diagrams/             # Diagrammes d'architecture
```

## 📌 Modèle de données (PostgreSQL)
### Client
```json
{
  "_id": "clt123",
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@email.com",
  "telephone": "+33612345678",
  "adresse": "10 rue des Lilas, Paris",
  "voyages": ["voy456", "voy789"]
}
```

### Voyage
```json
{
  "_id": "voy456",
  "titre": "Vacances à Bali",
  "destination": "Bali, Indonésie",
  "dates": {
    "depart": "2024-07-01",
    "retour": "2024-07-15"
  },
  "participants": 2,
  "budget": 3000,
  "clientId": "clt123"
}
```

### Facture
```json
{
  "_id": "fact001",
  "clientId": "clt123",
  "voyageId": "voy456",
  "total": 2800,
  "statut": "payé",
  "date_emission": "2024-06-01"
}
```

## 🚀 Démarrage du Projet

### Prérequis
- Mise en place de l’environnement (Angular, Spring Boot, MongoDB Atlas).
- Développement de l’authentification (JWT).
- Création du premier tableau de bord.

## 📝 Plan de Développement

### Phase 1: MVP (En cours)
- Finalisation de l'authentification
- Implémentation complète des API clients et voyages
- Développement du système de devis et facturation

### Phase 2: Fonctionnalités Avancées
- Intégration des paiements en ligne
- Système de notifications
- Intégration avec Google Calendar

### Phase 3: Optimisation et Finalisation
- Système de chat en ligne
- Optimisation des performances
- Tests complets et déploiement final

## 👥 Équipe

Projet Annuel ESGI 2025

## 📄 Licence

Ce projet est sous licence [MIT](LICENSE).
