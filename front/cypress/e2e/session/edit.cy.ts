/// <reference types="cypress" />

describe('Edition de session par un admin', () => {
  const teachers = [
    {
      id: 1,
      lastName: "Doe",
      firstName: "John",
      createdAt: new Date(),
      updatedAt: new Date(),
    },
    {
      id: 2,
      lastName: "Dupont",
      firstName: "Louis",
      createdAt: new Date(),
      updatedAt: new Date(),
    }
  ];

  const sessionAvant = {
    id: 1,
    name: "Yoga du matin",
    date: new Date(),
    teacher_id: 1,
    description: "Séance douce",
    users: [],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const sessionApres = {
    ...sessionAvant,
    name: "Yoga modifié"
  };

  beforeEach(() => {
    // Mock login admin (si besoin)
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'admin',
        email: 'yoga@studio.com',
        admin: true,
        token: 'fake-jwt-token'
      }
    }).as('loginRequest');

    // Mock endpoint qui vérifie le rôle admin
    cy.intercept('GET', '/api/auth/me', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'admin',
        email: 'yoga@studio.com',
        admin: true
      }
    }).as('me');

    // Mock teachers
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');

    // Mock sessions (listing)
    cy.intercept('GET', '/api/session', { body: [sessionAvant] }).as('getSessions');

    // Mock session detail (avant modif)
    cy.intercept('GET', '/api/session/1', { body: sessionAvant }).as('getSessionDetail');
  });

  it('L\'admin édite une session avec succès', () => {
    // Connexion admin via commande custom
    cy.loginAsAdmin();
    cy.wait('@getSessions');

    // Clique sur "Edit" de la session
    cy.contains('Yoga du matin').parents('mat-card').first().within(() => {
      cy.get('button').contains('Edit').click();
    });
    cy.wait('@getSessionDetail');
    cy.url().should('include', '/sessions/update/1');

    // Modifie le nom de la session
    cy.get('input[formControlName=name]').clear().type('Yoga modifié');

    // Prépare le mock de la requête PUT (modification)
    cy.intercept('PUT', '/api/session/1', {
      statusCode: 200,
      body: sessionApres
    }).as('updateSession');

    // Prépare le mock du GET /api/session après modif (listing mis à jour)
    cy.intercept('GET', '/api/session', { body: [sessionApres] }).as('getSessionsAfterUpdate');

    // Soumet le formulaire
    cy.get('button[type=submit]').click();
    cy.wait('@updateSession');
    cy.wait('@getSessionsAfterUpdate');

    // Vérifie le retour sur la liste et la présence du nouveau nom
    cy.url().should('include', '/sessions');
    cy.contains('Session updated !').should('be.visible');
    cy.contains('Yoga modifié').should('be.visible');
  });

  it('Le bouton Save reste désactivé si un champ requis est manquant', () => {
    cy.loginAsAdmin();
    cy.wait('@getSessions');

    // Clique sur "Edit" de la session
    cy.contains('Yoga du matin').parents('mat-card').first().within(() => {
      cy.get('button').contains('Edit').click();
    });
    cy.wait('@getSessionDetail');
    cy.url().should('include', '/sessions/update/1');

    // Vide le champ nom (requis)
    cy.get('input[formControlName=name]').clear();
    cy.get('button[type=submit]').should('be.disabled');

    // Remet un nom, vide la date (requis)
    cy.get('input[formControlName=name]').type('Yoga du matin');
    cy.get('input[formControlName=date]').clear();
    cy.get('button[type=submit]').should('be.disabled');

    // Remet une date, vide la description (requis)
    cy.get('input[formControlName=date]').type('2025-06-01');
    cy.get('textarea[formControlName=description]').clear();
    cy.get('button[type=submit]').should('be.disabled');
  });
});