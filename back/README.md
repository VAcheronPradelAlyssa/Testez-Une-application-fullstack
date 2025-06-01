# Yoga App – Back-end
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
