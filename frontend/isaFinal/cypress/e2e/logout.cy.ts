describe('Logout Test', () => {
  it('should login with valid credentials and log out', () => {
    cy.visit('http://localhost:8100/login')

    cy.get('input[name="username"]').type('admin')
    cy.get('input[name="password"]').type('admin')
    cy.get('#ingresar-button').click()

    cy.url().should('include', '/home')

    cy.window().then((window) => {
      const token = window.localStorage.getItem('jwt')
      expect(token).to.exist
    })
    cy.get("ion-button").contains("Salir").should('be.visible').click();
    cy.url().should('include', '/login');


    cy.window().then((window) => {
      const token = window.localStorage.getItem('jwt')
      expect(token).not.to.exist
    })

  })
})