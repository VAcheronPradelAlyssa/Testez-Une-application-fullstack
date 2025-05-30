/// <reference types="cypress" />

describe('Login spec', () => {
  it('Login successful', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'First',
        lastName: 'Last',
        admin: true,
        token: 'fake-jwt-token',
      },
    }).as('loginRequest');

    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'First',
        lastName: 'Last',
        admin: true,
      },
    }).as('sessionRequest');

    cy.get('input[formControlName=email]').type("example@domain.com");
    cy.get('input[formControlName=password]').type("test!1234");
    cy.get('form').submit();

    cy.wait('@loginRequest');
    cy.url().should('include', '/sessions');
    cy.wait('@sessionRequest');
  });

  it('Login fails with wrong password', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { message: 'An error occurred' }
    }).as('loginFail');

    cy.get('input[formControlName=email]').type("example@domain.com");
    cy.get('input[formControlName=password]').type("wrongpassword");
    cy.get('form').submit();

    cy.wait('@loginFail');
    cy.url().should('include', '/login');
    cy.contains('An error occurred').should('be.visible');
  });
  it('Permet d\'afficher et masquer le mot de passe', () => {
  cy.visit('/login');

  // Le champ password doit être de type "password" au départ
  cy.get('input[formControlName=password]').should('have.attr', 'type', 'password');

  // Cliquer sur l'icône pour afficher le mot de passe
  cy.get('button[aria-label="Hide password"]').click();

  // Le champ password doit maintenant être de type "text"
  cy.get('input[formControlName=password]').should('have.attr', 'type', 'text');

  // Recliquer pour masquer à nouveau
  cy.get('button[aria-label="Hide password"]').click();

  // Le champ password doit redevenir de type "password"
  cy.get('input[formControlName=password]').should('have.attr', 'type', 'password');
});
});