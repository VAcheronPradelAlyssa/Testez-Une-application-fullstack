describe('User session spec', () => {
  it('Bouton retour ramène à la page précédente', () => {
    // Mock login (pas de délai ici)
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      }
    }).as('login');

    // Mock avec délai sur les autres requêtes
    cy.intercept('GET', '/api/session', (req) => {
      req.on('response', (res) => {
        res.setDelay(1000); // 1 seconde de délai
      });
      req.reply({
        statusCode: 200,
        body: [
          {
            id: 1,
            name: "Test",
            date: new Date(),
            teacher_id: 1,
            description: "Test description",
            users: [],
            createdAt: new Date(),
            updatedAt: new Date()
          }
        ]
      });
    }).as('sessions');

    cy.intercept('GET', '/api/session/1', (req) => {
      req.on('response', (res) => {
        res.setDelay(1000);
      });
      req.reply({
        statusCode: 200,
        body: {
          id: 1,
          name: "Test",
          date: new Date(),
          teacher_id: 1,
          description: "Test description",
          users: [],
          createdAt: new Date(),
          updatedAt: new Date()
        }
      });
    }).as('sessionDetail');

    cy.intercept('GET', '/api/teacher/1', (req) => {
      req.on('response', (res) => {
        res.setDelay(1000);
      });
      req.reply({
        statusCode: 200,
        body: {
          id: 1,
          lastName: "Doe",
          firstName: "John",
          createdAt: new Date(),
          updatedAt: new Date()
        }
      });
    }).as('getTeacher1');

    // Connexion
    cy.visit('/login');
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("test!1234");
    cy.get('form').submit();
    cy.wait('@login');
    cy.url().should('include', '/sessions');

    // Aller sur le détail de la session
    cy.contains('Test').parents('mat-card').first().within(() => {
      cy.get('button').contains("Detail").click();
    });
    cy.wait('@sessionDetail');
    cy.wait('@getTeacher1');

    // Cliquer sur le bouton retour (back)
    cy.get('button').find('mat-icon').contains('arrow_back').click();

    // Vérifier qu'on revient à la page des sessions
    cy.url().should('include', '/sessions');
  });
});