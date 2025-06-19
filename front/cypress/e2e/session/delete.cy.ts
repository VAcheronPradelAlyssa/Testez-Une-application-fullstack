describe('Delete Session', () => {
  const teachers = [
    {
      id: 1,
      lastName: "Doe",
      firstName: "John",
      createdAt: new Date(),
      updatedAt: new Date(),
    },
    {
      id: 2,
      lastName: "Dupont",
      firstName: "Louis",
      createdAt: new Date(),
      updatedAt: new Date(),
    }
  ];

  it('L\'admin supprime une session et il n\'en reste qu\'une', () => {
    // ─── 1. FAKE BACKEND ─────────────────────────────────────
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/teacher/1', { body: teachers[0] }).as('getTeacher1');
    cy.intercept('GET', '/api/teacher/2', { body: teachers[1] }).as('getTeacher2');

    // Deux sessions au début
    const sessionsBefore = [
      {
        id: 1,
        name: "A session name",
        date: new Date(),
        teacher_id: 1,
        description: "A small description",
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        id: 2,
        name: "Another session",
        date: new Date(),
        teacher_id: 2,
        description: "Another description",
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ];
    cy.intercept('GET', '/api/session', { body: sessionsBefore }).as('getSessions');

    // Détail de la session à supprimer
    cy.intercept('GET', '/api/session/1', {
      body: sessionsBefore[0]
    }).as('getSession');

    cy.intercept('DELETE', '/api/session/1', { statusCode: 200 }).as('deleteSession');

    // Après suppression, il ne reste qu'une session
    const sessionsAfter = [
      {
        id: 2,
        name: "Another session",
        date: new Date(),
        teacher_id: 2,
        description: "Another description",
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ];

    // ─── 2. LOGIN ADMIN ─────────────────────────────────────
    cy.loginAsAdmin();
    cy.wait('@getSessions');
    cy.wait(400);

    // ─── 3. CLIQUE SUR “Detail” ───────────────────────────────
    cy.contains('A session name').parents('mat-card').first().within(() => {
      cy.get('button').contains("Detail").click();
    });
    cy.wait('@getSession');
    cy.wait(400);

    // Prépare le mock pour n'afficher qu'une session après suppression
    cy.intercept('GET', '/api/session', { body: sessionsAfter }).as('getSessionsAfterDelete');

    // ─── 4. CLIQUE SUR “Delete” ───────────────────────────────
    cy.get('button span').contains("Delete").click();
    cy.wait('@deleteSession');
    cy.wait('@getSessionsAfterDelete');
    cy.wait(400);

    // Vérifie la redirection et qu'il ne reste qu'une session
    cy.url().should('include', '/sessions');
    cy.contains('Session deleted !').should('be.visible');
    cy.contains('Another session').should('be.visible');
  });
});