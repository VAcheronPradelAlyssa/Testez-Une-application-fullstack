// Déclaration des types pour les commandes personnalisées
declare global {
  namespace Cypress {
    interface Chainable<Subject = any> {
      login(email: string, password: string, admin?: boolean): Chainable<any>;
      loginAsAdmin(): Chainable<any>;
      loginAsUser(): Chainable<any>;
    }
  }
}

Cypress.Commands.add('login', (email: string, password: string, admin: boolean = false) => {
  cy.intercept('POST', '/api/auth/login', {
    statusCode: 200,
    body: {
      id: admin ? 1 : 2,
      username: email,
      email,
      firstName: admin ? 'Admin' : 'User',
      lastName: admin ? 'User' : 'Test',
      admin,
      token: 'fake-jwt-token'
    }
  }).as('loginRequest');

  cy.visit('/login');
  cy.get('input[formControlName=email]').should('be.visible').type(email);
  cy.get('input[formControlName=password]').should('be.visible').type(password);
  cy.get('form').submit();
  cy.wait('@loginRequest');
  cy.url().should('include', '/sessions');
});

Cypress.Commands.add('loginAsAdmin', () => {
  cy.intercept('POST', '/api/auth/login', {
    statusCode: 200,
    body: {
      id: 1,
      username: 'admin',
      email: 'yoga@studio.com',
      firstName: 'Admin',
      lastName: 'User',
      admin: true,
      token: 'fake-jwt-token'
    }
  }).as('loginRequest');

  cy.visit('/login');
  cy.get('input[formControlName=email]').should('be.visible').type('yoga@studio.com');
  cy.get('input[formControlName=password]').should('be.visible').type('test!1234');
  cy.get('form').submit();
  cy.wait('@loginRequest');
  cy.url().should('include', '/sessions');
});

Cypress.Commands.add('loginAsUser', () => {
  cy.login('user@studio.com', 'test!1234', false);
});

export {};