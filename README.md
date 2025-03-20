# Projet-Annuel---GoSquad-
Projet Annuel ESGI 2025 

# Roadmap du Projet GOSQUAD

## 1. Conception & Préparation (1-2 semaines)
✅ Définition des **besoins** et rédaction du cahier des charges.  
✅ Élaboration des **modèles de données** (clients, voyages, activités, devis, facturation…).  
✅ Création du **schéma API** (routes REST, endpoints principaux).  
✅ Mise en place de l’**environnement de développement** (Git, Angular, Spring Boot, MongoDB).  
✅ Configuration de **l’authentification** avec JWT.  

## 2. MVP (4-6 semaines)
✅ Développement des **modules essentiels** :
- Gestion des utilisateurs (connexion, rôles, permissions).  
- Création et gestion des **clients**.  
- Création et gestion des **voyages** (destination, budget, participants).  
- Création et gestion des **devis et factures**.  
- Suivi des **paiements** et rappels automatiques.  
✅ Déploiement d’un **premier prototype** sur Heroku/Vercel pour tests internes.  

## 3. Fonctionnalités avancées (6-8 semaines)
✅ Ajout des **suggestions automatiques** basées sur le budget.  
✅ Intégration de **paiements en ligne** (Stripe, PayPal).  
✅ Système de **notifications** (email, push).  
✅ Intégration avec **Google Calendar**.  
✅ Système de **chat en ligne**.  
✅ Optimisation de la sécurité et conformité RGPD.  

## 4. Tests & Lancement (3-4 semaines)
✅ Tests unitaires et E2E avec Cypress/JUnit.  
✅ Corrections des bugs et optimisation des performances.  
✅ Déploiement final et communication avec les agences.  

# Architecture Technique
## 📌 Stack technique
| Composant  | Technologie |
|------------|-------------|
| **Front-end** | Angular, Angular Material, NgRx (optionnel) |
| **Back-end** | Spring Boot (REST API) |
| **Base de données** | MongoDB Atlas |
| **Auth & Sécurité** | JWT, Spring Security |
| **Déploiement** | Vercel (Front), Railway/Heroku (Back), MongoDB Atlas (DB) |

## 📌 Schéma API REST
### Authentification
```
POST /api/auth/register  - Inscription d’un utilisateur
POST /api/auth/login     - Connexion et génération d’un JWT
GET  /api/auth/me        - Récupérer les infos de l’utilisateur connecté
```

### Gestion des clients
```
POST   /api/clients       - Ajouter un nouveau client
GET    /api/clients       - Récupérer la liste des clients
GET    /api/clients/{id}  - Récupérer un client spécifique
PUT    /api/clients/{id}  - Modifier un client
DELETE /api/clients/{id}  - Supprimer un client
```

### Gestion des voyages
```
POST   /api/voyages       - Ajouter un voyage
GET    /api/voyages       - Récupérer la liste des voyages
GET    /api/voyages/{id}  - Récupérer un voyage spécifique
PUT    /api/voyages/{id}  - Modifier un voyage
DELETE /api/voyages/{id}  - Supprimer un voyage
```

### Gestion des devis & factures
```
POST   /api/devis       - Créer un devis
GET    /api/devis/{id}  - Récupérer un devis
POST   /api/factures    - Générer une facture
GET    /api/factures/{id} - Récupérer une facture
```

## 📌 Modèle de données (MongoDB)
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

# Plan de développement détaillé
## 📌 Semaine 1-2 : Configuration et Authentification
- Mise en place de l’environnement (Angular, Spring Boot, MongoDB Atlas).
- Développement de l’authentification (JWT).
- Création du premier tableau de bord.

## 📌 Semaine 3-4 : Gestion des clients & voyages
- API pour la gestion des clients.
- API pour la gestion des voyages.
- Interface Angular pour afficher les clients et voyages.

## 📌 Semaine 5-6 : Gestion des paiements & facturation
- Ajout des factures et suivi des paiements.
- Génération de devis et export en PDF.

## 📌 Semaine 7-8 : Finalisation et tests
- Optimisation UI et UX.
- Déploiement et tests finaux.

🚀 **Prêt à coder et à transformer cette vision en réalité !** 🎯

