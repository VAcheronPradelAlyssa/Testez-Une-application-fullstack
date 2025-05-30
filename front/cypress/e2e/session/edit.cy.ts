/// <reference types="cypress" />

describe('Session edit e2e test', () => {
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

  it('Edit a session', () => {
    // ─── 1. FAKE BACKEND ─────────────────────────────────────
    cy.intercept('POST', '/api/auth/login', {
      body: { id: 1, username: 'userName', admin: true }
    }).as('login');

    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');

    // Initial GET /api/session (avant modif)
    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: "A session name",
          date: new Date(),
          teacher_id: 1,
          description: "A small description",
          users: [],
          createdAt: new Date(),
          updatedAt: new Date()
        }
      ]
    }).as('getSessions');

    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: "Test",
        date: new Date(),
        teacher_id: 1,
        description: "A small description",
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    }).as('getSession');

    cy.intercept('PUT', '/api/session/1', { statusCode: 200 }).as('updateSession');

    // ─── 2. VISIT + LOGIN ────────────────────────────────────
    cy.visit('/login');
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("test!1234");
    cy.get('form').submit();

    cy.wait('@login');
    cy.wait('@getSessions');

    // ─── 3. CLIQUE SUR “Edit” ───────────────────────────────
    cy.contains('button', 'Edit').should('be.visible').click();
    cy.wait('@getSession');

    cy.url().should('include', '/sessions/update/1');

    cy.get('mat-form-field input[formControlName=name]').should('be.visible').clear().type("Test");

    // Avant de soumettre, prépare le mock du GET /api/session pour le retour
    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: "Test", // le nouveau nom !
          date: new Date(),
          teacher_id: 1,
          description: "A small description",
          users: [],
          createdAt: new Date(),
          updatedAt: new Date()
        }
      ]
    }).as('getSessionsAfterUpdate');

    cy.get('button[type=submit]').click();
    cy.wait('@updateSession');
    cy.wait('@getSessionsAfterUpdate');

    // Vérifie que le nouveau nom apparaît dans la liste
    cy.url().should('include', '/sessions');
    cy.contains('Test').should('be.visible');
  });
});