describe('Page Not Found', () => {
  it('affiche la page 404 pour une URL inconnue', () => {
    cy.visit('/une-page-inexistante', { failOnStatusCode: false });

    // Vérifie que le message 404 ou "Not Found" s'affiche
    cy.contains('Page not found !').should('be.visible');
    cy.contains(/not found/i).should('be.visible');
  });
 
  it('redirige vers /login si la session expire', () => {
    cy.loginAsUser();
    // Simule la suppression du token/session
    cy.window().then(win => win.localStorage.removeItem('sessionInformation'));
    cy.visit('/sessions');
    cy.url().should('include', '/login');
  });
});

describe('Accès interdit à la création de session pour un non-admin', () => {
  beforeEach(() => {
    // Mock login user (non-admin)
    cy.loginAsUser();
  });

  it('redirige vers /login si un non-admin tente d\'accéder à /sessions/create', () => {
    cy.visit('/sessions/create');
    cy.url().should('include', '/login');
    // Optionnel : vérifie le message d'erreur ou d'accès refusé
    cy.contains(/access denied|unauthorized|login/i).should('be.visible');
  });
});