import React from 'react';

import { Tabs, Tab } from "react-bootstrap";

import ProductView from './product/ProductView';

function DeliveryppTab() {
  return (
    <div className="container-md">
      <Tabs defaultActiveKey="profile" id="uncontrolled-tab-example">
        <Tab eventKey="products" title="Productos">
          <ProductView />
        </Tab>
        <Tab eventKey="orders" title="Ordenes">
          etc
        </Tab>
      </Tabs>
    </div>
  );
}

export default DeliveryppTab;