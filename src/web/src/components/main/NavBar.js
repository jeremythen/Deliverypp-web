import React from "react";

import {
    Collapse,
    Navbar,
    NavbarToggler,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
  } from 'reactstrap';


function NavBar() {
  return (
    <div className="NavBar">
      <Navbar color="light" light expand="md">
        <NavbarBrand href="/" color="#940205">
            <span style={{color: '#940205'}}>Deliverypp</span>
        </NavbarBrand>
        <Nav className="mr-auto" navbar>
        <NavItem>
            <NavLink href="/about">Acerca de nosotros</NavLink>
        </NavItem>
        <NavItem>
            <NavLink href="products">Productos</NavLink>
        </NavItem>
        </Nav>
      </Navbar>

    </div>
  );
}

export default NavBar;
