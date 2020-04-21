import React from 'react';
import './App.css';

import DeliveryppTab from './components/deliverypp/DeliveryppTab';
import NavBar from './components/main/NavBar';
import Footer from './components/main/Footer';
import Main from './components/main/Main';


function App() {
  return (
    <div className="App border">
      <NavBar />
      <DeliveryppTab />
      <Footer />
    </div>
  );
}

export default App;
