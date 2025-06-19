describe('Detail Session - affichage infos et boutons selon le rôle', () => {
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

  const session = {
    id: 1,
    name: "Test",
    date: new Date(),
    teacher_id: 1,
    description: "A small description",
    users: [],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  function interceptCommon() {
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/session', { body: [session] }).as('getSessions');
    cy.intercept('GET', '/api/session/1', { body: session }).as('getSession');
  }

  it('affiche les détails et le bouton Participate pour un non-admin', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: { id: 2, username: 'nonAdminUser', admin: false }
    }).as('login');
    interceptCommon();

    cy.login('nonadmin@studio.com', 'test!1234');
    cy.wait('@getSessions');

    cy.contains('Test').parents('mat-card').first().within(() => {
      cy.get('button').contains("Detail").click();
    });
    cy.wait('@getSession');

    cy.url().should('include', '/sessions/detail/1');
    cy.contains('Test').should('be.visible');
    cy.contains('A small description').should('be.visible');
    cy.contains('0 attendees').should('be.visible');
    cy.contains('Create at:').should('be.visible');
    cy.contains('Last update:').should('be.visible');
    cy.contains('Participate').should('be.visible');
    cy.contains('Delete').should('not.exist');
  });

  it('affiche les détails et le bouton Delete pour un admin', () => {
    interceptCommon();

    cy.loginAsAdmin();
    cy.wait('@getSessions');

    cy.contains('Test').parents('mat-card').first().within(() => {
      cy.get('button').contains("Detail").click();
    });
    cy.wait('@getSession');

    cy.url().should('include', '/sessions/detail/1');
    cy.contains('Test').should('be.visible');
    cy.contains('A small description').should('be.visible');
    cy.contains('0 attendees').should('be.visible');
    cy.contains('Create at:').should('be.visible');
    cy.contains('Last update:').should('be.visible');
    cy.contains('Delete').should('be.visible');
    cy.contains('Participate').should('not.exist');
  });

  it('permet à un utilisateur de participer puis de se désinscrire d\'une session', () => {
    const sessionNonInscrit = {
      id: 1,
      name: "Test",
      date: new Date(),
      teacher_id: 1,
      description: "A small description",
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    };
    const sessionInscrit = {
      ...sessionNonInscrit,
      users: [2] // l'utilisateur est inscrit
    };

    cy.intercept('POST', '/api/auth/login', {
      body: { id: 2, username: 'nonAdminUser', admin: false }
    }).as('login');
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/session', { body: [sessionNonInscrit] }).as('getSessions');
    cy.intercept('GET', '/api/session/1', { body: sessionNonInscrit }).as('getSession');

    cy.login('nonadmin@studio.com', 'test!1234');
    cy.wait('@getSessions');

    cy.contains('Test').parents('mat-card').first().within(() => {
      cy.get('button').contains("Detail").click();
    });
    cy.wait('@getSession');

    // Mock la participation (POST) et la session modifiée (utilisateur inscrit)
    cy.intercept('POST', '/api/session/1/participate/2', { statusCode: 200 }).as('participate');
    cy.intercept('GET', '/api/session/1', { body: sessionInscrit }).as('getSessionAfterParticipate');

    cy.get('button').contains('Participate').click();
    cy.wait('@participate');
    cy.wait('@getSessionAfterParticipate');

    cy.get('button').contains('Do not participate').should('be.visible');

    // Mock la désinscription (DELETE) et la session modifiée (utilisateur non inscrit)
    cy.intercept('DELETE', '/api/session/1/participate/2', { statusCode: 200 }).as('unparticipate');
    cy.intercept('GET', '/api/session/1', { body: sessionNonInscrit }).as('getSessionAfterUnparticipate');

    cy.get('button').contains('Do not participate').click();
    cy.wait('@unparticipate');
    cy.wait('@getSessionAfterUnparticipate');

    cy.get('button').contains('Participate').should('be.visible');
  });
  it('Permet de revenir en arrière avec le bouton retour', () => {
    // Mocks nécessaires
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
    cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [{
            id: 1,
            name: "Test",
            date: new Date(),
            teacher_id: 1,
            description: "Test description",
            users: [],
            createdAt: new Date(),
            updatedAt: new Date()
        }]
    }).as('sessions');
    cy.intercept('GET', '/api/session/1', {
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
    }).as('sessionDetail');
    cy.intercept('GET', '/api/teacher/1', {
        statusCode: 200,
        body: {
            id: 1,
            lastName: "Doe",
            firstName: "John",
            createdAt: new Date(),
            updatedAt: new Date()
        }
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