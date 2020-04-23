import React from 'react';

import { Tabs, Tab } from "react-bootstrap";

import ProductView from './product/ProductView';
import OrderView from './order/OrderView';

function DeliveryppTab() {
  return (
    <div className="container-md p-1 border mb-8">
      <Tabs defaultActiveKey="products" id="uncontrolled-tab-example">
        <Tab eventKey="products" title="Productos">
          <ProductView />
        </Tab>
        <Tab eventKey="orders" title="Ordenes">
          <OrderView />
        </Tab>
      </Tabs>
    </div>
  );
}

export default DeliveryppTab;