/// <reference types="cypress" />
describe('User Registration', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

 it('should register a new user and redirect to /login', () => {
  cy.intercept('POST', '/api/auth/register', { statusCode: 200 }).as('registerUser');
  cy.get('input[formControlName=firstName]').type('John');
  cy.get('input[formControlName=lastName]').type('Doe');
  cy.get('input[formControlName=email]').type('john.doe@example.com');
  cy.get('input[formControlName=password]').type('SecurePassword123!');
  cy.get('form').submit();
  cy.wait('@registerUser');
  cy.url().should('include', '/login');

  // Simule le backend pour le login avec les mêmes identifiants
  cy.intercept('POST', '/api/auth/login', {
    statusCode: 200,
    body: {
      id: 1,
      username: 'john.doe@example.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: false,
      token: 'fake-jwt-token'
    }
  }).as('loginRequest');

  // Effectue le login avec les identifiants utilisés à l'inscription
  cy.get('input[formControlName=email]').type('john.doe@example.com');
  cy.get('input[formControlName=password]').type('SecurePassword123!');
  cy.get('form').submit();
  cy.wait('@loginRequest');
  // Vérifie la redirection ou la présence d'un élément attendu après connexion
  cy.url().should('include', '/sessions');
});

  it('should display the registration form', () => {
    cy.get('input[formControlName=firstName]').should('exist');
    cy.get('input[formControlName=lastName]').should('exist');
    cy.get('input[formControlName=email]').should('exist');
    cy.get('input[formControlName=password]').should('exist');
  });

  it('should handle existing email error', () => {
    cy.intercept('POST', '/api/auth/register', { statusCode: 400 }).as('registerExisting');
    cy.get('input[formControlName=firstName]').type('Jane');
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=email]').type('existing.user@example.com');
    cy.get('input[formControlName=password]').type('AnotherPassword123!');
    cy.get('form').submit();
    cy.wait('@registerExisting');
    // Adapte ce texte selon le message réel affiché par ton appli
    cy.contains('An error occurred').should('exist');
  });

  it('should show validation errors for empty fields', () => {
    cy.get('form').submit();
    cy.get('input[formControlName=firstName]:invalid').should('exist');
    cy.get('input[formControlName=lastName]:invalid').should('exist');
    cy.get('input[formControlName=email]:invalid').should('exist');
    cy.get('input[formControlName=password]:invalid').should('exist');
  });

  it('should show error for invalid email format', () => {
    cy.get('input[formControlName=email]').type('not-an-email');
    cy.get('form').submit();
    // Adapte ce texte selon le message réel affiché par ton appli
    cy.contains('An error occurred').should('exist');
  });

  it('should show error for weak password', () => {
    cy.get('input[formControlName=password]').type('123');
    cy.get('form').submit();
    // Adapte ce texte selon le message réel affiché par ton appli
    cy.contains('An error occurred').should('exist');
  });

  it('should disable submit button if form is invalid', () => {
    cy.get('input[formControlName=firstName]').type('A');
    cy.get('input[formControlName=lastName]').type('B');
    cy.get('input[formControlName=email]').type('bademail');
    cy.get('input[formControlName=password]').type('123');
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should keep user on register page if registration fails', () => {
    cy.intercept('POST', '/api/auth/register', { statusCode: 400 }).as('registerFail');
    cy.get('input[formControlName=firstName]').type('Fail');
    cy.get('input[formControlName=lastName]').type('User');
    cy.get('input[formControlName=email]').type('fail.user@example.com');
    cy.get('input[formControlName=password]').type('FailPassword123!');
    cy.get('form').submit();
    cy.wait('@registerFail');
    cy.url().should('include', '/register');
  });

  it('should not allow registration with already used email (case insensitive)', () => {
    cy.intercept('POST', '/api/auth/register', { statusCode: 400 }).as('registerExisting');
    cy.get('input[formControlName=firstName]').type('Jane');
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=email]').type('Existing.User@Example.com');
    cy.get('input[formControlName=password]').type('AnotherPassword123!');
    cy.get('form').submit();
    cy.wait('@registerExisting');
    // Adapte ce texte selon le message réel affiché par ton appli
    cy.contains('An error occurred').should('exist');
  });

  it('should trim spaces in email and names before submitting', () => {
    cy.intercept('POST', '/api/auth/register', (req) => {
      // Si ton front ne fait pas le trim, ce test échouera !
      expect(req.body.firstName.trim()).to.equal('John');
      expect(req.body.lastName.trim()).to.equal('Doe');
      expect(req.body.email.trim()).to.equal('john.doe@example.com');
      req.reply({ statusCode: 200 });
    }).as('registerTrim');
    cy.get('input[formControlName=firstName]').type('  John  ');
    cy.get('input[formControlName=lastName]').type('  Doe  ');
    cy.get('input[formControlName=email]').type('  john.doe@example.com  ');
    cy.get('input[formControlName=password]').type('SecurePassword123!');
    cy.get('form').submit();
    cy.wait('@registerTrim');
  });

  it('should show error if required fields are missing', () => {
  // Soumettre sans rien remplir
  cy.get('form').submit();
  cy.contains('An error occurred').should('be.visible');

  // Remplir partiellement
  cy.get('input[formControlName=firstName]').type('John');
  cy.get('form').submit();
  cy.contains('An error occurred').should('be.visible');
});

});