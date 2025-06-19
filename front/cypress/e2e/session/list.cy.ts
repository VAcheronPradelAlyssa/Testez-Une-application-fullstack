describe('Session list', () => {
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

  const sessions = [
    {
      id: 1,
      name: "Yoga du matin",
      date: new Date(),
      teacher_id: 1,
      description: "Session matinale",
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      name: "Pilates du soir",
      date: new Date(),
      teacher_id: 2,
      description: "Session du soir",
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  it('affiche la liste des sessions pour un admin', () => {
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/session', { body: sessions }).as('getSessions');

    cy.loginAsAdmin();
    cy.wait('@getSessions');
    cy.wait(400);

    cy.contains('Yoga du matin').should('be.visible');
    cy.contains('Pilates du soir').should('be.visible');
  });

  it('affiche la liste des sessions pour un non-admin', () => {
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/session', { body: sessions }).as('getSessions');

    cy.loginAsUser();
    cy.wait('@getSessions');
    cy.wait(400);

    cy.contains('Yoga du matin').should('be.visible');
    cy.contains('Pilates du soir').should('be.visible');
  });
});