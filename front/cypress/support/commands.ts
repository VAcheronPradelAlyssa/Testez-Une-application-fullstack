// -- Déclaration TypeScript pour les commandes personnalisées --
declare namespace Cypress {
  interface Chainable {
    /**
     * Effectue un login simulé via formulaire et intercepts mockés
     */
    login(): Chainable<void>;
  }
}

// -- Commande personnalisée : cy.login() --
Cypress.Commands.add('login', () => {
  cy.intercept('POST', '/api/auth/login', {
    statusCode: 200,
    body: {
      id: 1,
      username: 'userName',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true,
      token: 'fake-jwt-token'
    },
  }).as('loginRequest');

  cy.intercept('GET', '/api/session', {
    statusCode: 200,
    body: [
      {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      }
    ],
  }).as('sessionRequest');

  cy.visit('/login');

  cy.get('input[formControlName=email]')
    .should('be.visible')
    .type('yoga@studio.com');

  cy.get('input[formControlName=password]')
    .should('be.visible')
    .type('test!1234');

  cy.get('form').submit();

  cy.wait('@loginRequest');
  cy.wait('@sessionRequest');

  cy.url().should('include', '/sessions');
});