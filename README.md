# Yoga App

Application fullstack de gestion de cours de yoga  
Front-end Angular 19 • Back-end Spring Boot 2.6.1 • MySQL • Tests Cypress & Jest

---

## Sommaire

- [Présentation](#présentation)
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Lancement](#lancement)
- [Tests](#tests)
- [Structure des dossiers](#structure-des-dossiers)
- [Fonctionnalités principales](#fonctionnalités-principales)
- [Auteurs](#auteurs)

---

## Présentation

Cette application permet de gérer des cours de yoga, les enseignants, les utilisateurs, l’inscription/désinscription aux sessions, et l’administration des comptes.  
Elle propose une interface moderne (Angular Material) et une API sécurisée (JWT).

---

## Architecture

- **Front-end** : Angular 19, Material, Cypress, Jest
- **Back-end** : Spring Boot 2.6.1, Spring Security, JWT, JPA, MySQL
- **Base de données** : MySQL
- **Tests** : Cypress (e2e), Jest (unitaires front), JUnit (unitaires back)

---

## Prérequis

- **Node.js** : v20.x
- **npm** : v10.x
- **Angular CLI** : v19.x  
- **Java** : 11
- **Maven** : 3.6+
- **MySQL** : 8.x ou compatible

---

## Installation

### 1. Cloner le dépôt

```bash
git clone <url-du-repo>
cd Testez-Une-application-fullstack
```

### 2. Installer le front-end

```bash
cd front
npm install
```

### 3. Installer le back-end

```bash
cd ../back
mvn clean install
```

---

## Configuration

### 1. Base de données

- Crée la base `yoga_app` dans MySQL :
/home/alyssa/Testez-Une-application-fullstack/ressources/sql/script.sql
```sql
DROP DATABASE IF EXISTS yoga_app;
CREATE DATABASE yoga_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON yoga_app.* TO 'admin'@'localhost' IDENTIFIED BY 'motdepasse';
FLUSH PRIVILEGES;
```

- Mets à jour le fichier `back/src/main/resources/application.properties` avec tes identifiants MySQL si besoin.

### 2. Variables d’environnement

- Pour le back-end, configure le secret JWT et les accès BDD dans `application.properties`.

---

## Lancement

### 1. Lancer le back-end

```bash
cd back
mvn spring-boot:run
```

L’API sera disponible sur [http://localhost:8080](http://localhost:8080)

### 2. Lancer le front-end

```bash
cd front
npm start
```

L’application sera disponible sur [http://localhost:4200](http://localhost:4200)

---

## Tests

### Front-end

- **Tests unitaires** :  
  ```bash
  npm test
  ```
- **Tests end-to-end (Cypress)** :  
  ```bash
  npm run cypress:open
  # ou en mode headless
  npm run cypress:run
  ```

### Back-end

- **Tests unitaires et d’intégration** :  
  ```bash
  cd back
  mvn test
  ```

---

## Structure des dossiers

```
Testez-Une-application-fullstack/
│
├── back/      # Spring Boot (API, sécurité, tests JUnit)
│   └── src/
│       ├── main/java/com/openclassrooms/starterjwt/
│       └── test/java/com/openclassrooms/starterjwt/
│
├── front/     # Angular (UI, Cypress, Jest)
│   ├── src/
│   └── cypress/
│
└── README.md
```

---

## Fonctionnalités principales

- Authentification sécurisée (JWT)
- Gestion des utilisateurs (inscription, suppression, profil)
- Gestion des enseignants
- Création, modification, suppression de sessions (admin)
- Inscription/désinscription à une session (utilisateur)
- Interface responsive et moderne (Angular Material)
- Gestion des erreurs (404, accès refusé, etc.)
- Couverture de tests complète (front et back)

---

## Auteurs

- Alyssa (Projet OpenClassrooms)
- Inspiré par les bonnes pratiques Angular, Spring Boot et OpenClassrooms

---

## Licence

Projet pédagogique – OpenClassrooms