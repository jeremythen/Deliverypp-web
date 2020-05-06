import React, { useState, useEffect } from "react";

import './OrderView.css';

import OrderService from '../../../services/OrderService';

import OrderTable from "./OrderTable";
import ProductDetailsModal from './ProductDetailsModal';
import AllOrdersMapsView from './maps/AllOrdersMapsView';
import OrderToolbar from './OrderToolbar';

const toggleTableSort = { id: false, username: false, email: false, telephone: false, createdAt: false, total: false, comment: false };

function OrderView({ showAlert }) {
  const [orders, setOrders] = useState(() => []);
  const [tableData, setTableData] = useState(() => []);
  const [selectedOrder, setSelectedOrder] = useState(() => ({orderLines: [], locations: []}));
  const [showOrderedProductsModal, setShowOrderedProductsModal] = useState(() => false);
  const [statusFilter, setStatusFilter] = useState(() => '');

  const transformToTableData = orders => {
    return orders.map((
      { user: { username, email, telephone }, status, id, createdAt, total, comment }) => ({ id, status, total, comment, createdAt, username, email, telephone })
    );
  };

  const getOrders = async () => {

    const responseData = await OrderService.getOrders();

    if(responseData && responseData.success) {
      const orders = responseData.response;
        setOrders(orders);
        const tableData = transformToTableData(orders);
        setTableData(tableData);
    } else {
      console.log('Error getting orders.', responseData);
      showAlert({ color: 'warning', message: 'Error getting orders.'});
    }

  };

  useEffect(() => {
    getOrders();
  }, []);

  const handleRowClick = orderId => {
    const order = orders.find(order => order.id === orderId)
    if(order) {
      setSelectedOrder(order);
      setShowOrderedProductsModal(true);
    }
  };

  const filterTableData = event => {
    const value = event.target.value;
    const tableData = transformToTableData(orders);
    if(value) {
        const filteredOrders = tableData.filter(order => {
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
        setTableData(filteredOrders);
    } else {
      setTableData(tableData);
    }
  };

  const onOrderedProductsModalClose = () => {
    setShowOrderedProductsModal(false);
  };

  const toggleShowModal = () => {
    setShowOrderedProductsModal(!showOrderedProductsModal);
  };

  const onOrderStatusChange = (orderId, newStatus) => {

    const newOrders = orders.map(order => {
      if(order.id === orderId) {
        order.status = newStatus;
      }
      return order;
    });

    setOrders(newOrders);

    const mappedTableData = tableData.map(record => {
      if(record.id === orderId) {
        record.status = newStatus;
        const order = orders.find(order => order.id === orderId);
        setSelectedOrder(order);
      }
      return record;
    });

    
    setTableData(mappedTableData);

  };

  const filterByStatus = event => {

    const status = event.target.value;

    const tableData = transformToTableData(orders);

    if(!status) {
      setStatusFilter('');
      return setTableData(tableData);
    }

    const filteredOrders = tableData.filter(order => order.status === status);

    setStatusFilter(status);
    setTableData(filteredOrders);

  };

  const sortColumn = prop => {
 
    const toggleSort = toggleTableSort[prop];

    const compare = (value1, value2) => {
      if(value1 < value2) {
        return 1;
      } else if (value1 > value2) {
        return -1;
      } else {
        return 0;
      }
    }

    const tableData = transformToTableData(orders);

    tableData.sort((orderA, orderB) => {
      
      let value1 = orderA[prop];
      let value2 = orderB[prop];

      if(toggleSort) {
        return compare(value1, value2);
      } else {
        return compare(value2, value1);
      }

    });

    toggleTableSort[prop] = !toggleSort;

    const newOrders = [...tableData];

    setTableData(newOrders);

  };

  const onMarkerClick = (locationId) => {
    const order = orders.find(order => order.location.id === locationId);
    if(order) {
      setSelectedOrder(order);
      setShowOrderedProductsModal(true);
    }
  }

  return (
    <div className="OrderView">
      <AllOrdersMapsView locations={orders.map(order => order.location)} onMarkerClick={onMarkerClick} />
      <ProductDetailsModal
        showModal={showOrderedProductsModal}
        toggleShowModal={toggleShowModal}
        onClose={onOrderedProductsModalClose}
        showAlert={showAlert}
        onOrderStatusChange={onOrderStatusChange}
        order={selectedOrder}
       />
      <div>
        <OrderToolbar
          onSearch={filterTableData}
          orderStatus={statusFilter}
          onOrderStatusChange={filterByStatus}
        />
      </div>
      <div className="Container mt-1">
        <OrderTable
            orders={tableData}
            onRowClick={handleRowClick}
            onColumnClick={sortColumn}
        />
      </div>
    </div>
  );
}
 
export default OrderView;
