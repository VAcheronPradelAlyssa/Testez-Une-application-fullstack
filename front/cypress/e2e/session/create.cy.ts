describe('Create Session Page', () => {
  const teachers = [{ id: 1, firstName: 'John', lastName: 'Doe' }];

  beforeEach(() => {
    cy.intercept('GET', '/api/teacher*', { body: teachers }).as('getTeachers');
    cy.intercept('POST', '/api/auth/login', {
      body: { id: 1, username: 'user', firstName: 'X', lastName: 'Y', admin: true }
    }).as('login');
    cy.intercept('GET', '/api/session', { body: [] }).as('getSessions');

    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('form').submit();
    cy.url().should('include', '/sessions');
    cy.contains('Create').click();
    cy.wait('@getTeachers');
  });

  it('should display the create session form and disable save initially', () => {
    cy.get('h1').contains('Create session');
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should enable Save when all fields are filled and send POST request', () => {
    cy.intercept('POST', '/api/session', {
      statusCode: 201,
      body: { id: 1, name: 'Morning Yoga' }
    }).as('createSession');

    cy.get('input[formControlName=name]').type('Morning Yoga');
    cy.get('input[formControlName=date]').type('2025-06-01');
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').contains('John').click();
    cy.get('textarea[formControlName=description]').type('Relaxing morning yoga session.');

    cy.get('button[type=submit]').should('not.be.disabled').click();

    cy.wait('@createSession').its('request.body').should((body) => {
      expect(body.name).to.equal('Morning Yoga');
      expect(body.description).to.contain('Relaxing');
    });
  });

  it('should keep Save disabled if any single field is empty', () => {
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=name]').type('Only Name');
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=date]').type('2025-06-01');
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').contains('John').click();
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('textarea[formControlName=description]').should('have.value', '');
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should enable Save if description too long but show error on submit', () => {
    cy.intercept('POST', '/api/session', {
      statusCode: 400,
      body: { message: 'Description too long' }
    }).as('createSessionBad');

    cy.get('input[formControlName=name]').type('Session Name');
    cy.get('input[formControlName=date]').type('2025-06-01');
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').contains('John').click();

    const longDesc = 'A'.repeat(1001);
    cy.get('textarea[formControlName=description]').type(longDesc);

    cy.get('button[type=submit]').should('not.be.disabled').click();

    cy.wait('@createSessionBad').its('response.statusCode').should('eq', 400);
  });
  it('redirige les non-admins vers /sessions', () => {
  // Simule un utilisateur non admin
  window.localStorage.setItem('sessionInformation', JSON.stringify({
    id: 2,
    admin: false
  }));

  cy.visit('/sessions/create');
  cy.url().should('include', '/login');
});
});