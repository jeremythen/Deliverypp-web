import React, { useState } from 'react';

import ProductView from './product/ProductView';
import OrderView from './order/OrderView';

import { TabContent, TabPane, Nav, NavItem, NavLink } from 'reactstrap';

import OrderProductsView from './product/OrderProductsView';

function DeliveryppTab(props) {

    const [activeTab, setActiveTab] = useState('product');

    const toggle = tab => {
        if(activeTab !== tab) setActiveTab(tab);
    };

    const getAdminTabNavItems = () => {
        return (
            <>
                <NavItem>
                    <NavLink style={{cursor: 'pointer'}} className={ activeTab === 'product' ? 'active' : '' } onClick={() => { toggle('product'); }}>Productos</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink style={{cursor: 'pointer'}} className={ activeTab === 'orders' ? 'active' : '' } onClick={() => { toggle('orders'); }} >Ordenes</NavLink>
                </NavItem>
            </>  
        );
    };

    const getAdminTabPanes = () => {
        return (
            <>
                <TabPane tabId="product">
                    <ProductView showAlert={props.showAlert} />
                </TabPane>
                <TabPane tabId="orders">
                    <OrderView showAlert={props.showAlert} />
                </TabPane>
            </>
        )
    }

  return (
    <div className="container-md p-1 border mb-8">

      <Nav tabs>
        {
          false && (
            <NavItem>
              <NavLink style={{cursor: 'pointer'}} className={ activeTab === 'available_products' ? 'active' : '' } onClick={() => { toggle('available_products'); }}>Productos Disponibles</NavLink>
            </NavItem>
          )
        }
        
        {
            props.isUserLoggedIn && props.user.role === 'ADMIN' && getAdminTabNavItems()
        }
      </Nav>
      <TabContent activeTab={activeTab}>
        {
          false && (
            <TabPane tabId="available_products">
              <OrderProductsView showAlert={props.showAlert} />
            </TabPane>
          )
        }
        {
            props.isUserLoggedIn && props.user.role === 'ADMIN' && getAdminTabPanes()
        }
      </TabContent>

    </div>
  );
}

export default DeliveryppTab;