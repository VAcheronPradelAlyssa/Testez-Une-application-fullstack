describe('Detail Session for Non-Admin User', () => {
  const teachers = [
    {
      id: 1,
      lastName: "Doe",
      firstName: "John",
      createdAt: new Date(),
      updatedAt: new Date(),
    }
  ];

  const session = {
    id: 1,
    name: "Test",
    date: new Date(),
    teacher_id: 1,
    description: "A small description",
    users: [], // initialement vide
    createdAt: new Date(),
    updatedAt: new Date()
  };

  it('Utilisateur non admin peut voir le détail d\'une session', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: { id: 2, username: 'nonAdminUser', admin: false }
    }).as('login');

    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/teacher/1', { body: teachers[0] }).as('getTeacher1');
    cy.intercept('GET', '/api/session', { body: [session] }).as('getSessions');
    cy.intercept('GET', '/api/session/1', { body: session }).as('getSession');

    // Connexion
    cy.visit('/login');
    cy.get('input[formControlName=email]').type("nonadmin@studio.com");
    cy.get('input[formControlName=password]').type("test!1234");
    cy.get('form').submit();

    cy.wait('@login');
    cy.wait('@getSessions');

    // Aller sur la page de détail de la session
    cy.contains('Test').parents('mat-card').first().within(() => {
      cy.get('button').contains("Detail").click();
    });

    cy.wait('@getSession');

    // Vérifications affichage
    cy.url().should('include', '/sessions/detail/1');
    cy.contains('Test').should('be.visible');
    cy.contains('A small description').should('be.visible');
    cy.contains('0 attendees').should('be.visible');
    cy.contains('Participate').should('be.visible');
    cy.contains('Do not participate').should('not.exist');
    cy.contains('Delete').should('not.exist');
  });
});