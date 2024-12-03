describe('Counter Modification Test', () => {
  it('should modify the counter value correctly', () => {
    cy.visit('http://localhost:8100/')

    cy.get('input[name="username"]').type('admin')
    cy.get('input[name="password"]').type('admin')
    cy.get('#ingresar-button').click()

    cy.url().should('include', '/home')

    cy.contains('ion-button', 'Reset').click()
    cy.get('#counter').should('have.text', '0')

    cy.contains('ion-button', '+1').click()
    cy.get('#counter').should('have.text', '1')

    cy.contains('ion-button', '+10').click()
    cy.get('#counter').should('have.text', '11')

  })
})