describe('Create Session', () => {
  const teachers = [
    { id: 1, firstName: 'John', lastName: 'Doe' },
    { id: 2, firstName: 'Louis', lastName: 'Dupont' }
  ];

  beforeEach(() => {
    cy.visit('/login');
    cy.contains('Login').should('be.visible');

    // Mock des enseignants
    cy.intercept('GET', '/api/teacher*', { body: teachers }).as('getTeachers');

    // Mock du login
    cy.intercept('POST', '/api/auth/login', {
      body: { id: 1, username: 'user', firstName: 'X', lastName: 'Y', admin: true }
    }).as('login');

    // Mock du GET session pour éviter le 404 dès l'ouverture de la page de création
    cy.intercept('GET', '/api/session', { body: [] }).as('getSessions');

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('form').submit();

    cy.url().should('include', '/sessions');
    cy.contains('Create').click();
  });

  it('should create a new session successfully', () => {
    cy.get('input[formControlName=name]').type('New Session');
    cy.get('input[formControlName=date]').type('2025-06-01');

    // Ouvre la liste des enseignants
    cy.get('mat-select[formControlName=teacher_id]').click();

    // Vérifie que les options sont bien là
    cy.get('mat-option').should('have.length', teachers.length);

    // Sélectionne la première option (John)
    cy.get('mat-option').contains(teachers[0].firstName).click();

    cy.get('textarea[formControlName=description]').type('Description of the new session');

    // Mock du POST session
    cy.intercept('POST', '/api/session', {
      statusCode: 201,
      body: {
        id: 1,
        name: 'New Session',
        date: '2025-06-01',
        teacher_id: teachers[0].id,
        description: 'Description of the new session'
      }
    }).as('createSession');

    cy.get('button[type="submit"]').contains('Save').click();

    // Attends la création et la redirection
    cy.wait('@createSession');
    cy.wait('@getSessions');
    cy.url().should('include', '/sessions');
  });
});