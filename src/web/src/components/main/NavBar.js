import React from "react";

import { Navbar, NavDropdown, Nav, Form, Button, FormControl  } from 'react-bootstrap';

function NavBar() {
  return (
    <div className="NavBar">
      <Navbar bg="light" expand="lg">
        <Navbar.Brand href="#home">Deliverypp</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="mr-auto">
            <Nav.Link href="#home">Home</Nav.Link>
            <Nav.Link href="#link">Link</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    </div>
  );
}

export default NavBar;
