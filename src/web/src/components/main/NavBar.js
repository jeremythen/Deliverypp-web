import React from "react";

import {
    Navbar,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    Button,
  } from 'reactstrap';


function NavBar({ onSignInClick, onLogOutClick, isUserLoggedIn, user}) {
  return (
    <div className="NavBar">
      <Navbar color="light" light expand="md">
        <NavbarBrand href="/">
            <span style={{color: '#940205'}}>Deliverypp</span>
        </NavbarBrand>
        <Nav className="mr-auto" navbar></Nav>
        {
          /*
            <Nav className="mr-auto" navbar>
              <NavItem>
                  <NavLink href="/products">Productos</NavLink>
              </NavItem>
            </Nav>
          */
        }
        
        {
            isUserLoggedIn ?
                  <div>
                      <span className="mr-1">{user.name}</span>
                      <Button outline title="Salir" onClick={onLogOutClick} className="mr-1"><i className="fas fa-sign-out-alt"></i></Button>  
                  </div>
                :
                  <div>
                      <Button outline title="Entrar" onClick={onSignInClick} className="mr-1"><i className="fas fa-sign-in-alt"></i></Button>
                  </div>
        }

      </Navbar>

    </div>
  );
}

export default NavBar;
