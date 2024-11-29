describe('Login Test', () => {
  it('should login with valid credentials', () => {
    cy.visit('http://localhost:8100/login')

    cy.get('input[name="username"]').type('admin')
    cy.get('input[name="password"]').type('admin')
    cy.get('#ingresar-button').click()

    cy.url().should('include', '/home')

    cy.window().then((window) => {
      const token = window.localStorage.getItem('jwt')
      expect(token).to.exist
    })
  })
})