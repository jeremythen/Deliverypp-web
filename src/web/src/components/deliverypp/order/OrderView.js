import React, { useState, useEffect } from "react";

import OrderTable from "./OrderTable";

import Toolbar from "../../common/Toolbar";

import OrderService from '../../../services/OrderService';

import { Card, CardImg, CardText, CardBody, CardTitle, Button, Modal, ModalHeader, ModalBody, ModalFooter, TabContent, TabPane, Nav, NavItem, NavLink } from 'reactstrap';

import {Map, Marker, GoogleApiWrapper} from 'google-maps-react';

import './OrderView.css';

function OrderView({ showAlert }) {
  const [orders, setOrders] = useState([]);
  const [filterableOrders, setFilterableOrders] = useState([]);

  const [selectedOrder, setSelectedOrder] = useState({orderLines: [], locations: []});
  const [showOrderedProductsModal, setShowOrderedProductsModal] = useState(false);

  const getOrders = async () => {

    const responseData = await OrderService.getOrders();

    if(responseData && responseData.success) {
      const orders = responseData.response;
        setOrders(orders);
        setFilterableOrders(orders);
    } else {
      console.log('Error getting orders.')
      showAlert({ color: 'warning', message: 'Error getting orders.'});
    }

  };

  useEffect(() => {
    getOrders();
  }, []);

  const handleRowClick = order => {
    setSelectedOrder(order);
    setShowOrderedProductsModal(true);
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

  const onOrderedProductsModalClose = () => {
    setShowOrderedProductsModal(false);
  }

  const toggle = () => {
    setShowOrderedProductsModal(!showOrderedProductsModal);
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
       />
      <div>
        <Toolbar
        onSearch={handleSearch}
          noActionButtons={true}
        />
      </div>
      <div className="Container mt-1">
        <OrderTable
            orders={filterableOrders}
            onRowClick={handleRowClick}
        />
      </div>
    </div>
  );
}

function ProductDetailsModal({ onClose, showModal, toggleShowModal, orderLines, orderId, location }) {

  const [activeTab, setActiveTab] = useState('orderProductsDetails');

  const toggle = tab => {
      if(activeTab !== tab) setActiveTab(tab);
  };

  return (
    <div>
      <Modal isOpen={showModal}>
        <ModalHeader toggle={toggleShowModal}>
          <CardText>Order #: {orderId}</CardText>
          <CardText>Productos ordenados</CardText>
        </ModalHeader>
        <ModalBody>
            <Nav tabs>
              <NavItem>
                <NavLink
                  style={{ cursor: "pointer" }}
                  className={activeTab === "orderProductsDetails" ? "active" : ""}
                  onClick={() => {
                    toggle("orderProductsDetails");
                  }}
                >
                  Productos
                </NavLink>
              </NavItem>

              <NavItem>
                <NavLink
                  style={{ cursor: "pointer" }}
                  className={activeTab === "orderProductsLocation" ? "active" : ""}
                  onClick={() => {
                    toggle("orderProductsLocation");
                  }}
                >
                  Localización
                </NavLink>
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
            imageHidden && <div className="imageIconContainer text-center" title="Mostrar image del producto" onClick={() => toggleImageHidden(product.id)}><i class="fas fa-image" style={{fontSize: 24, color: 'grey'}}></i></div>
          }

          {
            !imageHidden && <CardImg
                              top
                              width="100%"
                              src={product.imageUrl}
                              alt={product.description}
                              onClick={() => toggleImageHidden(product.id)}
                              title="Ocultar imagel del producto"
                              className="cardImgElem"
                            />
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
