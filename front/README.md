# Yoga App – Front-end

Frontend de l’application de gestion de cours de yoga  
**Angular 19 • Angular Material • Cypress • Jest**

---

## Sommaire

- [Présentation](#présentation)
- [Architecture Front-end](#architecture-front-end)
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

Ce front-end fournit une interface moderne pour la gestion des cours, professeurs, utilisateurs, inscriptions et administration des comptes de yoga.  
Il s’appuie sur Angular Material pour l’UI et interagit avec une API sécurisée via JWT.

---

## Architecture Front-end

- **Framework** : Angular 19
- **UI** : Angular Material
- **Gestion d’état & services** : RxJS, HttpClient
- **Tests unitaires** : Jest
- **Tests end-to-end** : Cypress

---

## Prérequis

- **Node.js** : v20.x
- **npm** : v10.x
- **Angular CLI** : v19.x

---

## Installation

1. **Cloner le dépôt**

   ```bash
   git clone <url-du-repo>
   cd Testez-Une-application-fullstack/frontend
   ```

2. **Installer les dépendances**

   ```bash
   npm install
   ```

---

## Configuration

Configurer l’URL de l’API dans le fichier :

```text
src/environments/environment.ts
```

Exemple de configuration :

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

---

## Lancement

```bash
ng serve
```
L’application sera accessible sur [http://localhost:4200](http://localhost:4200).

---

## Tests

- **Unitaires (Jest)** :

  ```bash
  npm run test
  ```

- **End-to-end (Cypress)** :

  ```bash
  npm run cy:open
  ```
  ou
  ```bash
  npx cypress open
  ```

---

## Structure des dossiers

```text
frontend/
├── src/
│   ├── app/
│   │   ├── components/         # Composants Angular
│   │   ├── services/           # Services (API, Auth, etc.)
│   │   ├── pages/              # Pages principales
│   │   ├── guards/             # Guards de navigation
│   │   └── ...                 # Autres modules
│   ├── assets/                 # Images, styles, etc.
│   ├── environments/           # Fichiers d’environnement
│   └── ...                     # Autres fichiers Angular
```

---

## Fonctionnalités principales (front-end)

- Authentification par JWT
- Gestion des cours, utilisateurs, professeurs, inscriptions/désinscriptions
- Interface responsive et moderne (Material)
- Interaction sécurisée avec l’API backend

---

## Auteurs

Projet pédagogique – OpenClassrooms
