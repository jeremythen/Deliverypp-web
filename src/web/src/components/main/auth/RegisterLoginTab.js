import React, { useState } from "react";

import {
  Modal,
  ModalHeader,
  ModalBody,
} from "reactstrap";

import { TabContent, TabPane, Nav, NavItem, NavLink } from "reactstrap";

import LoginForm from './LoginForm';
import RegisterForm from './RegisterForm';

import './RegisterLoginTab.css';

function RegisterLoginTab(props) {

  const [activeTab, setActiveTab] = useState('login');

  const toggle = (tab) => {
    if (activeTab !== tab) setActiveTab(tab);
  };

  let loginIconColor = 'grey';
  let registerIconColor = 'grey';

  if(activeTab === 'login') {
    loginIconColor = props.color;
  } else if(activeTab === 'register') {
    registerIconColor = props.color;
  }

  const handleRegisterLoginResponse = (responseData) => {

    if(responseData) {

        if(responseData.success) {
            props.showAlert({ color: 'warning', message: responseData.message});
        } else if(responseData.success) {
            props.showAlert({ color: 'info', message: responseData.message});
        } else {
            props.showAlert({ color: 'warning', message: 'Hubo un error durante el registro. Trata luego.'});
        }
    
    } else {
        props.showAlert({ color: 'warning', message: 'Hubo un error durante el registro. Trata luego.'});
    }

  };

  const onRegister = (response) => {

    handleRegisterLoginResponse(response);

    setActiveTab('login');

  }

  const onLogin = (responseData) => {

    handleRegisterLoginResponse(responseData);

    if(responseData && responseData.response) {
        const response = responseData.response;
        if(response.token) {
            localStorage.setItem('deliverypp_user_login_token',  response.token);
            props.toggle();
            props.onLogin(response.user);
        }
    }

  }

  return (
    <div className="RegisterLoginTab">
      <Modal isOpen={props.showModal}>
        <ModalHeader toggle={props.toggle}>Entrar</ModalHeader>
        <ModalBody>
          <Nav tabs>
            <NavItem>
              <NavLink
                style={{ cursor: "pointer" }}
                className={ activeTab === 'login' ? 'active' : '' }
                onClick={() => { toggle("login"); }}
              >
                Entrar <i className="fas fa-user" style={{color: loginIconColor}}></i>
              </NavLink>
            </NavItem>
            <NavItem>
              <NavLink
                style={{ cursor: "pointer" }}
                className={ activeTab === 'register' ? 'active' : '' }
                onClick={() => { toggle("register"); }}
              >
                Registrarse <i className="fas fa-sign-in-alt" style={{color: registerIconColor}}></i>
              </NavLink>
            </NavItem>
          </Nav>
          <TabContent activeTab={activeTab}>
            <TabPane tabId="login">
              <LoginForm onLogin={onLogin} color={props.color} />
            </TabPane>
            <TabPane tabId="register">
              <RegisterForm onRegister={onRegister} color={props.color} />
            </TabPane>
          </TabContent>
        </ModalBody>
      </Modal>
    </div>
  );
}

export default RegisterLoginTab;
