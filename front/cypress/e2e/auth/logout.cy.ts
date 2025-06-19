describe('Logout', () => {
  beforeEach(() => {
    // Utilise la commande login classique (pas admin)
    cy.login('yoga@studio.com', 'test!1234');
  });

  it('should log out the user correctly', () => {    // Cliquez sur le bouton de déconnexion
    cy.contains('Logout').should('be.visible').click();

    // Vérifie la redirection vers la page d'accueil ou de login
    cy.url().should('match', /\/($|login)/);

    // Vérifie que le bouton Logout n'existe plus
    cy.contains('Logout').should('not.exist');

    // Vérifie que la session est supprimée du localStorage
    cy.window().then((win) => {
      expect(win.localStorage.getItem('sessionInformation')).to.be.null;
    });

    // Vérifie qu'on ne peut plus accéder à une page protégée
    cy.visit('/sessions');
    cy.url().should('include', '/login');
  });
});
