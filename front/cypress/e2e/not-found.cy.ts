describe('Page Not Found', () => {
  it('affiche la page 404 pour une URL inconnue', () => {
    cy.visit('/une-page-inexistante', { failOnStatusCode: false });

    // Vérifie que le message 404 ou "Not Found" s'affiche
    cy.contains('Page not found !').should('be.visible');
    cy.contains(/not found/i).should('be.visible');
  });
});