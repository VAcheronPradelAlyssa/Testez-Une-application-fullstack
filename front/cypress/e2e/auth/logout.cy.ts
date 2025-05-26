describe('Logout', () => {
  beforeEach(() => {
    cy.visit('/login');
    cy.contains('Login').should('be.visible');

    // Mock du login
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

    // Mock de la session utilisateur
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    }).as('sessionRequest');

    cy.get('input[formControlName=email]').should('be.visible').type("yoga@studio.com", { delay: 100 });
    cy.wait(300); // pause pour voir la saisie
    cy.get('input[formControlName=password]').should('be.visible').type("test!1234", { delay: 100 });
    cy.wait(300);
    cy.get('form').submit();

    cy.wait('@loginRequest');
    cy.wait('@sessionRequest');
    cy.url().should('include', '/sessions');
  });

  it('should log out the user correctly', () => {
    cy.wait(500); // pause avant le clic
    cy.contains('Logout')
      .should('be.visible')
      .invoke('css', 'box-shadow', '0 0 0 3px #ff0000') // effet visuel
      .click();
    cy.url().should('include', '/');
  });
});