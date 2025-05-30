/// <reference types="cypress" />

describe('E2E Test for Existing User Profile (mock)', () => {
  const email = 'amelie.durand@example.com';
  const password = 'motdepasse123';

  beforeEach(() => {
    // Mock login API
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 5,
        token: 'mocked-jwt-token',
        username: email
      }
    }).as('loginRequest');

    // Mock GET user API
    cy.intercept('GET', '/api/user/5', {
      statusCode: 200,
      body: {
        id: 5,
        firstName: 'Amélie',
        lastName: 'Durand',
        email: email,
        admin: false,
        createdAt: '2024-03-10T10:00:00Z',
        updatedAt: '2024-05-10T15:42:00Z'
      }
    }).as('getUser');

    cy.intercept('GET', '/api/session', { body: [] }).as('getSessions');
  });

  it('Should display user information on /me page (mocked)', () => {
    // Visit login page and perform login
    cy.visit('/login');
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(password);
    cy.get('form').submit();

    // Wait for login, set session information in storage as expected by your app
    cy.wait('@loginRequest').then(() => {
      window.localStorage.setItem('sessionInformation', JSON.stringify({
        id: 5,
        token: 'mocked-jwt-token',
        username: email
      }));
    });

    // Check redirect to /sessions
    cy.url().should('include', '/sessions');

    // Go to /me by clicking the Account link (adapt selector if needed)
    cy.contains('span.link', 'Account').click();
    cy.url().should('include', '/me');

    // Wait for user details and check displayed information
    cy.wait('@getUser');
    cy.get('p').contains('Name: Amélie DURAND');
    cy.get('p').contains('Email: amelie.durand@example.com');
  });

  it('should go back when clicking the back button', () => {
    cy.visit('/login');
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(password);
    cy.get('form').submit();
    cy.wait('@loginRequest');
    cy.contains('span.link', 'Account').click();
    cy.url().should('include', '/me');
    cy.get('button').contains(/back/i).click();
    // Vérifie que l'utilisateur revient sur /sessions (ou la page précédente)
    cy.url().should('include', '/sessions');
  });

  it('should delete the account and log out', () => {
    cy.intercept('DELETE', '/api/user/5', { statusCode: 200 }).as('deleteUser');
    cy.visit('/login');
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(password);
    cy.get('form').submit();
    cy.wait('@loginRequest');
    cy.contains('span.link', 'Account').click();
    cy.url().should('include', '/me');
    cy.get('button').contains(/delete/i).click();
    cy.wait('@deleteUser');
    cy.contains('Your account has been deleted !').should('be.visible');
const baseUrl = Cypress.config().baseUrl?.replace(/\/$/, '') || '';
cy.url().should('eq', baseUrl + '/');    // Vérifie qu'on ne peut plus accéder à /me
    cy.visit('/me');
    cy.url().should('include', '/login');
  });
  it('affiche "You are admin" si l\'utilisateur est admin', () => {
  // Mock login API
  cy.intercept('POST', '/api/auth/login', {
    statusCode: 200,
    body: {
      id: 42,
      token: 'mocked-jwt-token',
      username: 'admin@example.com'
    }
  }).as('loginRequest');

  // Mock GET user API avec admin: true
  cy.intercept('GET', '/api/user/42', {
    statusCode: 200,
    body: {
      id: 42,
      firstName: 'Admin',
      lastName: 'User',
      email: 'admin@example.com',
      admin: true,
      createdAt: '2024-03-10T10:00:00Z',
      updatedAt: '2024-05-10T15:42:00Z'
    }
  }).as('getUserAdmin');

  cy.intercept('GET', '/api/session', { body: [] }).as('getSessions');

  // Login
  cy.visit('/login');
  cy.get('input[formControlName=email]').type('admin@example.com');
  cy.get('input[formControlName=password]').type('motdepasse123');
  cy.get('form').submit();
  cy.wait('@loginRequest').then(() => {
    window.localStorage.setItem('sessionInformation', JSON.stringify({
      id: 42,
      token: 'mocked-jwt-token',
      username: 'admin@example.com'
    }));
  });

  cy.url().should('include', '/sessions');
  cy.contains('span.link', 'Account').click();
  cy.url().should('include', '/me');
  cy.wait('@getUserAdmin');

  // Vérifie que le texte "You are admin" est affiché
  cy.get('p.my2').contains('You are admin').should('be.visible');
});
});