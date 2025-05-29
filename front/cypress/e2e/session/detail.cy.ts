describe('User session spec', () => {
    it('Permet de Participate à une session quand non inscrit puis de se désinscrire', () => {
        // Mock login
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
        
        // Mock sessions
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

        // Mock session detail (non inscrit)
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

        // Mock teacher
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

        // Mock participate
        cy.intercept('POST', '/api/session/1/participate/1', {
            statusCode: 200
        }).as('participate');

        // Mock unParticipate
        cy.intercept('DELETE', '/api/session/1/participate/1', {
            statusCode: 200
        }).as('unParticipate');

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

        // Vérifier que le bouton "Participate" apparaît
        cy.get('button').contains('Participate').should('exist');

        // Après le clic, on change le mock pour simuler l'inscription
        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
                id: 1,
                name: "Test",
                date: new Date(),
                teacher_id: 1,
                description: "Test description",
                users: [1], // L'utilisateur est maintenant inscrit
                createdAt: new Date(),
                updatedAt: new Date()
            }
        }).as('sessionDetailAfterParticipate');

        // Cliquer sur "Participate" et vérifier l'appel API
        cy.get('button').contains('Participate').click();
        cy.wait('@participate');
        cy.wait('@sessionDetailAfterParticipate');

        // Vérifier que le bouton a changé en "Do not participate"
        cy.get('button').contains('Do not participate').should('exist');

        // Après le clic sur "Do not participate", on re-mock la session sans l'utilisateur
        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
                id: 1,
                name: "Test",
                date: new Date(),
                teacher_id: 1,
                description: "Test description",
                users: [], // L'utilisateur n'est plus inscrit
                createdAt: new Date(),
                updatedAt: new Date()
            }
        }).as('sessionDetailAfterUnParticipate');

        // Cliquer sur "Do not participate" et vérifier l'appel API
        cy.get('button').contains('Do not participate').click();
        cy.wait('@unParticipate');
        cy.wait('@sessionDetailAfterUnParticipate');

        // Vérifier que le bouton "Participate" réapparaît
        cy.get('button').contains('Participate').should('exist');
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