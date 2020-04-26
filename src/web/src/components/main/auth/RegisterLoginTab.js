import React, { useState } from "react";

import {
  Modal,
  ModalHeader,
  ModalBody,
} from "reactstrap";

import { TabContent, TabPane, Nav, NavItem, NavLink } from "reactstrap";

import LoginForm from './LoginForm';
import RegisterForm from './RegisterForm';

import { Alert } from 'reactstrap';

import './RegisterLoginTab.css';

function RegisterLoginTab(props) {

  const [activeTab, setActiveTab] = useState('login');
  const [alertVisible, setAlertVisible] = useState(false);
  const [alertMessage, setAlertMessage] = useState('Registered');
  const [alertColor, setAlertColor] = useState('info');

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

        if(responseData.status === 'ERROR') {
            props.onAlertShow({ color: 'warning', message: responseData.message});
        } else if(responseData.status === 'SUCCESS') {
            props.onAlertShow({ color: 'info', message: responseData.message});
        } else {
            props.onAlertShow({ color: 'warning', message: 'Hubo un error durante el registro. Trata luego.'});
        }
    
    } else {
        props.onAlertShow({ color: 'warning', message: 'Hubo un error durante el registro. Trata luego.'});
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
