# Yoga App – Back-end

Backend de l’application de gestion de cours de yoga  
**Spring Boot 2.6.1 • Spring Security • JWT • JPA • MySQL • JUnit**

---

## Sommaire

- [Présentation](#présentation)
- [Architecture Back-end](#architecture-back-end)
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

Ce back-end fournit une API REST sécurisée pour la gestion des cours, professeurs, utilisateurs et inscriptions à des sessions de yoga.  
L’authentification se fait par JWT. Les accès sont protégés par Spring Security.

---

## Architecture Back-end

- **Framework** : Spring Boot 2.6.1
- **Sécurité** : Spring Security, JWT
- **ORM** : Spring Data JPA
- **Base de données** : MySQL
- **Tests unitaires** : JUnit (MockMvc, etc.)

---

## Prérequis

- **Java** : 11
- **Maven** : 3.6+
- **MySQL** : 8.x ou compatible

---

## Installation

1. **Cloner le dépôt**

   ```bash
   git clone <url-du-repo>
   cd Testez-Une-application-fullstack/backend
   ```

2. **Configurer la base de données**

   - Crée une base MySQL (par exemple : `yoga_db`)
   - Configure le fichier `src/main/resources/application.properties` avec tes identifiants et URL MySQL.

3. **Installer les dépendances**

   ```bash
   mvn clean install
   ```

---

## Configuration

Dans `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yoga_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=ton_user
spring.datasource.password=ton_mot_de_passe
spring.jpa.hibernate.ddl-auto=update
jwt.secret=SecretJWT
```

Adapte selon ton environnement.

---

## Lancement

```bash
mvn spring-boot:run
```
ou :
```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

L’API REST démarre sur [http://localhost:8080](http://localhost:8080).

---

## Tests

Lance les tests unitaires :

```bash
mvn test
```

Les tests utilisent JUnit et MockMvc.

---

## Structure des dossiers

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/com/openclassrooms/starterjwt/
│   │   │   ├── controllers/     # Contrôleurs REST
│   │   │   ├── models/          # Entités JPA
│   │   │   ├── repositories/    # Repositories JPA
│   │   │   ├── services/        # Services métiers
│   │   │   └── security/        # Sécurité (JWT, Spring Security)
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/openclassrooms/starterjwt/   # Tests JUnit
```

---

## Fonctionnalités principales (back-end)

- Authentification & gestion des utilisateurs (JWT)
- Gestion des cours, professeurs, utilisateurs, inscriptions/désinscriptions
- Sécurisation des routes (Spring Security)
- API RESTful documentée

---

## Auteurs

Projet pédagogique – OpenClassrooms
