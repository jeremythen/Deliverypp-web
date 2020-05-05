import React, { useState, useEffect } from "react";

import OrderTable from "./OrderTable";

import OrderService from '../../../services/OrderService';

import { Card, CardImg, CardText, CardBody, CardTitle, Button, Modal, ModalHeader, ModalBody, ModalFooter, TabContent, TabPane, Nav, NavItem, NavLink, FormGroup, Label, Input } from 'reactstrap';

import {Map, Marker, GoogleApiWrapper} from 'google-maps-react';

import './OrderView.css';

import StatusClassMapper from './StatusClassMapper';

const toggleTableSort = {id: false, username: false, email: false, telephone: false, createdAt: false, total: false, comment: false};

function OrderView({ showAlert }) {
  const [orders, setOrders] = useState([]);
  const [tableData, setTableData] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState({orderLines: [], locations: []});
  const [showOrderedProductsModal, setShowOrderedProductsModal] = useState(false);
  const [statusFilter, setStatusFilter] = useState('');

  const transformToTableData = orders => {
    return orders.map(({ user: { username, email, telephone }, status, id, createdAt, total, comment }) => ({ id, status, total, comment, createdAt, username, email, telephone  }));
  }

  const getOrders = async () => {

    const responseData = await OrderService.getOrders();

    if(responseData && responseData.success) {
      const orders = responseData.response;
        setOrders(orders);
        const tableData = transformToTableData(orders);
        setTableData(tableData);
    } else {
      console.log('Error getting orders.')
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

  const handleSearch = (event) => {
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

  }

  const onOrderedProductsModalClose = () => {
    setShowOrderedProductsModal(false);
  }

  const toggle = () => {
    setShowOrderedProductsModal(!showOrderedProductsModal);
  }

  const onOrderStatusChange = (orderId, newStatus) => {

    const newOrders = orders.map(order => {
      if(order.id === orderId) {
        order.status = newStatus;
      }
      return order;
    })

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

  }

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

  }

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

  }

  return (
    <div className="OrderView">
      <WrappedAllLocationsMapContainer locations={orders.map(order => order.location)} />
      <ProductDetailsModal
        showModal={showOrderedProductsModal}
        toggleShowModal={toggle}
        onClose={onOrderedProductsModalClose}
        orderLines={selectedOrder.orderLines}
        orderId={selectedOrder.id}
        location={selectedOrder.location}
        orderStatus={selectedOrder.status}
        showAlert={showAlert}
        onOrderStatusChange={onOrderStatusChange}
       />
      <div>
        <OrderToolbar
          onSearch={handleSearch}
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

function ProductDetailsModal({ onClose, showModal, toggleShowModal, orderLines, orderId, location, orderStatus, showAlert, onOrderStatusChange }) {

  console.log('orderId, orderStatus', orderId, orderStatus)

  const [activeTab, setActiveTab] = useState('orderProductsDetails');

  const toggle = tab => {
      if(activeTab !== tab) setActiveTab(tab);
  };

  const handleStatusChange = async event => {
    const newStatus = event.target.value;
    const responseData = await OrderService.updateStatus(orderId, newStatus);
    if(responseData && responseData.success) {
      onOrderStatusChange(orderId, newStatus);
    } else {
      console.log('Error updating status.')
      showAlert({ color: 'warning', message: 'Error actualizando el estado.'});
    }

  }

  return (
    <div>
      <Modal isOpen={showModal}>
        <ModalHeader toggle={toggleShowModal}>
          <CardText>Order #: {orderId}</CardText>
          <CardText>Productos ordenados</CardText>
          <StatusSelect orderStatus={orderStatus} onOrderStatusChange={handleStatusChange} />
        </ModalHeader>
        <ModalBody>
            <Nav tabs>
              <NavItem>
                <NavLink style={{ cursor: "pointer" }} className={activeTab === "orderProductsDetails" ? "active" : ""} onClick={() => toggle("orderProductsDetails") }>Productos</NavLink>
              </NavItem>

              <NavItem>
                <NavLink style={{ cursor: "pointer" }} className={activeTab === "orderProductsLocation" ? "active" : ""} onClick={() => toggle("orderProductsLocation") }>Localización</NavLink>
              </NavItem>
            </Nav>
            <TabContent activeTab={activeTab}>
              <TabPane tabId="orderProductsDetails">
              <OrderedProductDetailsView orderLines={orderLines} />
              </TabPane>
              <TabPane tabId="orderProductsLocation">
                <WrappedMap location={location} />
              </TabPane>
            </TabContent>
        </ModalBody>
        <ModalFooter>
          <Button color="success" onClick={onClose}>
            Cerrar
          </Button>{" "}
        </ModalFooter>
      </Modal>
    </div>
  );
}

function OrderToolbar({ onSearch, orderStatus, onOrderStatusChange }) {

  return (
    <div className="Toolbar p-2 border rounded">
      <div className="searchInputContainer p-1">
          <div className="input-group">
            <div className="input-group-prepend">
              <span className="input-group-text" id="inputGroupPrepend2">
                <i className="fa fa-search"></i>
              </span>
            </div>
            <input
                onChange={onSearch}
                type="text"
                className="form-control"
                placeholder="Buscar"
                aria-describedby="inputGroupPrepend2"
                required
            />
            <div className="ml-2">
              <StatusSelect orderStatus={orderStatus} onOrderStatusChange={onOrderStatusChange} />
            </div>
           
          </div>
      </div>


      
    </div>
  );
}

function StatusSelect({ orderStatus, onOrderStatusChange }) {
  return (
    <div className="d-flex flex-row justify-content-center align-items-cente mr-2">
      <Label className="mr-2" htmlFor="statusSelect">Estado</Label>
      <Input style={{borderWidth: 2}} className={`border-${StatusClassMapper.getClassStatusClass(orderStatus)}`} type="select" id="statusSelect" value={orderStatus} onChange={onOrderStatusChange}>
        <option value={''}></option>
        <option value={'ORDERED'}>Ordenado</option>
        <option value={'PAID'}>Pagado</option>
        <option value={'ACQUIRING'}>Adquiriendo</option>
        <option value={'ACQUIRED'}>Adquirido</option>
        <option value={'TRANSIT'}>En Tránsito</option>
        <option value={'DELIVERED'}>Entregado</option>
      </Input>
    </div>
  )
}

function OrderedProductDetailsView({orderLines}) {

  const [mappedOrderLines, setMappedOrderLines] = useState(orderLines.map(orderLine => ({ ...orderLine, imageHidden: true })));

  const toggleImageHidden = (id) => {
    console.log('id', id)
    const newMappedOrderLines = mappedOrderLines.map(orderLine => {
      if(orderLine.product.id === id) {
        orderLine.imageHidden = !orderLine.imageHidden;
      }
      return orderLine;
    });
    setMappedOrderLines(newMappedOrderLines);
  }

  return (
    <>
      {mappedOrderLines.map(({ quantity, product, imageHidden }) => (
        <Card key={product.id} style={{ marginBottom: 4, marginTop: 4 }}>
          
          {
            imageHidden && <div className="imageIconContainer text-center" title="Mostrar image del producto" onClick={() => toggleImageHidden(product.id)}><i className="fas fa-image" style={{fontSize: 24, color: 'grey'}}></i></div>
          }

          {
            !imageHidden && (
              <CardImg
                top
                width="100%"
                src={product.imageUrl}
                alt={product.description}
                onClick={() => toggleImageHidden(product.id)}
                title="Ocultar imagel del producto"
                className="cardImgElem"
              />
            )
          }
          
          <CardBody>
            <CardTitle>{product.description}</CardTitle>
            <CardText>{`Precio RD$${product.price}`}</CardText>
            <CardText>{`Cantidad ${quantity}`}</CardText>
            <CardText>{`Total RD$${product.price * quantity}`}</CardText>
          </CardBody>
        </Card>
      ))}
    </>
  )
}


const style = {
  position: 'relative',  
  width: '100%',
  height: '100%'
}

function MapContainer({ google, location }) {

  return (
    <div style={{height: 400, padding: 0, position: 'relative'}}>
      <Map
        google={google}
        zoom={14}
        style={style}
        initialCenter={{
          lat: location.latitude,
          lng: location.longitude
        }}>
        <Marker name={'Current location'} />
      </Map>
    </div>
    
  );
}

const WrappedMap = GoogleApiWrapper({
  apiKey: ('AIzaSyC1wC2ImcL1s4wITbDbM-g3nLK01fJHGkc')
})(MapContainer);

function AllLocationsMapContainer({ google, locations }) {

  return (
    <div style={{height: 400, padding: 0, position: 'relative'}}>
      <Map
        google={google}
        zoom={11}
        style={style}
        initialCenter={{
          lat: 18.486057,
          lng: -69.931213
        }}
      >
        {
          locations.map(location => (
            <Marker
              key={location.id}
              title={'The marker`s title will appear as a tooltip.'}
              name={'SOMA'}
              position={{lat: location.latitude, lng: location.longitude}}
            />
          ))
        }
      </Map>
    </div>
  )

}

const WrappedAllLocationsMapContainer = GoogleApiWrapper({
  apiKey: ('AIzaSyC1wC2ImcL1s4wITbDbM-g3nLK01fJHGkc')
})(AllLocationsMapContainer);
 
export default OrderView;
