import React, { useState, useEffect } from 'react';
import './App.css';

import DeliveryppTab from './components/deliverypp/DeliveryppTab';
import NavBar from './components/main/NavBar';
import Footer from './components/main/Footer';

import RegisterLoginTab from './components/main/auth/RegisterLoginTab';

import { Alert } from 'reactstrap';

import MainLoader from './components/common/MainLoader';

import AuthService from './services/AuthService';

import axios from 'axios';

import OrderProductsView from './components/deliverypp/product/OrderProductsView';

const mainColor = '#940205';

function App() {

  const [showRegisterLoginTab, setShowRegisterLoginTab] = useState(false);
  const [alertVisible, setAlertVisible] = useState(false);
  const [alertMessage, setAlertMessage] = useState('Registered');
  const [alertColor, setAlertColor] = useState('info');
  const [appLoading, setAppLoading] = useState(true);
  const [isUserLoggedIn, setIsUserLoggedIn] = useState(false);
  const [user, setUser] = useState({});

  useEffect(() => {

    const token = localStorage.getItem('deliverypp_user_login_token');

    const getUser = async () => {
      if(token) {
        const responseData = await AuthService.getUserByToken(token);

        if(responseData && responseData.response) {
          const user = responseData.response;
          if(responseData.status === 'SUCCESS') {
            setUser(user);
            setIsUserLoggedIn(true);
          }
        } else {
          setIsUserLoggedIn(false);
        }
        setTimeout(() => { setAppLoading(false); }, 1000);
      } else {
  
        setIsUserLoggedIn(false);
        setTimeout(() => { setAppLoading(false); }, 1000);
        setIsUserLoggedIn(false);

      }
    }

    if(!isUserLoggedIn) {
      getUser();
    }
    
  }, [isUserLoggedIn]);

  const toggleRegisterLoginTab = () => {
    setShowRegisterLoginTab(!showRegisterLoginTab);
  };

  const showAlert = ({ color, message }) => {
    setAlertColor(color);
    setAlertMessage(message);
    setAlertVisible(true);

    setTimeout(() => {
      setAlertColor('info');
      setAlertVisible(false);
      setAlertMessage('');
    }, 3000);
  };

  const onAlertDismiss = () => {
    setAlertVisible(false);
  };

  const onLogOutClick = () => {

    setAppLoading(true);

    const token = localStorage.getItem('deliverypp_user_login_token');

    AuthService.logout(token);

    localStorage.removeItem('deliverypp_user_login_token');

    setUser({});
    setIsUserLoggedIn(false);

    setTimeout(() => { setAppLoading(false); }, 1000);

  };

  const onLogin = (user) => {

    setAppLoading(true);

    setUser(user);
    setIsUserLoggedIn(true);

    setTimeout(() => { setAppLoading(false); }, 1000);

  };

  if(appLoading) {
    return (
      <MainLoader />
    )
  } else {
    return (
      <div className="App">

        <div className="alertContainer">
          <Alert color={alertColor} isOpen={alertVisible} toggle={onAlertDismiss}>
              {alertMessage}
          </Alert>
        </div>  

        <NavBar
          isUserLoggedIn={isUserLoggedIn}
          user={user}
          onSignInClick={() => setShowRegisterLoginTab(true)}
          onLogOutClick={onLogOutClick}
          color={mainColor}
        />
        
        {
          showRegisterLoginTab && <RegisterLoginTab showModal={showRegisterLoginTab} toggle={toggleRegisterLoginTab} color={mainColor} showAlert={showAlert} onLogin={onLogin} />
        }

        <DeliveryppTab showAlert={showAlert} color={mainColor} isUserLoggedIn={isUserLoggedIn} user={user} />

        <Footer color={mainColor} />
      </div>
    );
  }

}

export default App;
