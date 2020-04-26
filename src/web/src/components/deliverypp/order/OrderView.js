import React, { useState, useEffect } from "react";

import OrderTable from "./OrderTable";

import axios from "axios";

import Toolbar from "../../common/Toolbar";

import OrderService from '../../../services/OrderService';

import './OrderView.css';

function OrderView(props) {
  const [orders, setOrders] = useState([]);
  const [filterableOrders, setFilterableOrders] = useState([]);

  const [selectedOrderId, setSelectedOrderId] = useState(-1);

  const getOrders = async () => {

    const orders = await OrderService.getOrders();

    if(Array.isArray(orders)) {
        setOrders(orders);
        setFilterableOrders(orders);
    } else {
        console.error('Error getting orders.');
    }

  };

  useEffect(() => {
    getOrders();
  }, [orders.length === 0]);

  const handleRowClick = order => {

    setSelectedOrderId(order.id);

  };

  const handleSearch = (event) => {

    const value = event.target.value;

    if(value) {

        const filteredOrders = orders.filter(order => {

            const keys = Object.keys(order);

            for(let key of keys) {

                let propValue = order[key];
                if(propValue) {
                    propValue = '' + propValue;
                    if(propValue.includes(value)) {
                        return true;
                    }
                }

            }

        });

        setFilterableOrders(filteredOrders);

    } else {
        setFilterableOrders(orders);
    }

  }

  return (
    <div className="OrderView">
      <div>
        <Toolbar
        onSearch={handleSearch}
          noActionButtons={true}
        />
      </div>
      <div className="orderTableContainer mt-1">
        <OrderTable
            orders={filterableOrders}
            onRowClick={handleRowClick}
        />
      </div>
    </div>
  );
}

export default OrderView;
