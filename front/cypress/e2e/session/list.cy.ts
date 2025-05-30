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

  it('affiche la liste des sessions', () => {
    // Mock API
    cy.intercept('POST', '/api/auth/login', {
      body: { id: 1, username: 'userName', admin: true }
    }).as('login');
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/session', { body: sessions }).as('getSessions');

    // Connexion
    cy.visit('/login');
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("test!1234");
    cy.get('form').submit();

    cy.wait('@login');
    cy.wait('@getSessions');
    cy.wait(400);

    
cy.get('mat-card').filter(':contains("Yoga du matin"), :contains("Pilates du soir")');
cy.contains('Yoga du matin').should('be.visible');
cy.contains('Pilates du soir').should('be.visible');

  });
});