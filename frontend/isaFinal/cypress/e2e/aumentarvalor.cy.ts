describe('Counter Modification Test', () => {
  it('should modify the counter value correctly', () => {
    cy.visit('http://localhost:8100/')

    cy.get('input[name="username"]').type('admin')
    cy.get('input[name="password"]').type('admin')
    cy.get('#ingresar-button').click()

    cy.url().should('include', '/home')

    // Assuming the initial value is 0
    cy.contains('ion-button', 'Reset').click()
    cy.get('#value-display').should('have.text', '0')

    // Click the button to increase the value by 1
    cy.contains('ion-button', '+1').click()
    cy.get('#value-display').should('have.text', '1')

    // Click the button to increase the value by 10
    cy.contains('ion-button', '+10').click()
    cy.get('#value-display').should('have.text', '11')

    // Click the button to decrease the value by 1
    cy.contains('ion-button', '-1').click()
    cy.get('#value-display').should('have.text', '10')

    // Click the button to decrease the value by 10
    cy.contains('ion-button', '-10').click()
    cy.get('#value-display').should('have.text', '0')

    // Click the button to reset the value
    cy.contains('ion-button', 'Reset').click()
    cy.get('#value-display').should('have.text', '0')
  })
})