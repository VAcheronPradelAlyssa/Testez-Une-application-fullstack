describe('Logout', () => {
  beforeEach(() => {
    cy.login(); // Utilise la commande personnalisée
  });

  it('should log out the user correctly', () => {
    cy.contains('Logout').should('be.visible').click();
    cy.url().should('include', '/');
    cy.contains('Logout').should('not.exist');

    cy.visit('/sessions');
    cy.url().should('include', '/login');
  });
});
