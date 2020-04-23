import React, { useState, useEffect } from "react";

import OrderTable from "./OrderTable";

import axios from "axios";

import Toolbar from "../../common/Toolbar";

import './OrderView.css';

function OrderView(props) {
  const [orders, setOrders] = useState([]);

  const [selectedOrderId, setSelectedOrderId] = useState(-1);

  useEffect(() => {
    const fetchData = async () => {
      const response = await axios.get("http://localhost:8080/api/order");

      const deliveryppResponse = response.data;

      if (deliveryppResponse.status === "SUCCESS") {
        setOrders(deliveryppResponse.response);
      } else {
        console.error("Error getting products.");
      }
    };

    fetchData();
  }, [orders.length === 0]);

  const handleEditOrder = () => {};

  const handleAddOrder = () => {};

  const handleDeleteOrder = () => {};

  const handleRowClick = order => {
    console.log('handleRowClick order', order)

    setSelectedOrderId(order.id);

  };

  return (
    <div className="OrderView">
      <div>
        <Toolbar
          onEditClick={handleEditOrder}
          onAddClick={handleAddOrder}
          onDeleteClick={handleDeleteOrder}
          disabled={selectedOrderId == -1}
        />
      </div>
      <div className="orderTableContainer mt-1">
        <OrderTable
            orders={orders}
            onRowClick={handleRowClick}
        />
      </div>
    </div>
  );
}

export default OrderView;
