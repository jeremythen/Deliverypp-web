import React, { useState } from 'react';

import { CardText, Button, Modal, ModalHeader, ModalBody, ModalFooter, TabContent, TabPane, Nav, NavItem, NavLink } from 'reactstrap';

import StatusSelect from './StatusSelect';

import OrderedProductDetailsView from './OrderedProductDetailsView';

import OrderMapView from './maps/OrderMapView';

import OrderService from '../../../services/OrderService';

function ProductDetailsModal({ onClose, showModal, toggleShowModal, order, showAlert, onOrderStatusChange }) {

    const { orderLines, id: orderId, location, status: orderStatus, total } = order;

    const [activeTab, setActiveTab] = useState(() => 'orderProductsDetails');
  
    const toggle = tab => {
        if(activeTab !== tab) setActiveTab(tab);
    };
  
    const handleStatusChange = async event => {
      const newStatus = event.target.value;
      const responseData = await OrderService.updateStatus(orderId, newStatus);
      if(responseData && responseData.success) {
        onOrderStatusChange(orderId, newStatus);
      } else {
        console.error('Error updating status.')
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

                <NavItem>
                  <NavLink style={{ cursor: "pointer" }} className={activeTab === "customer" ? "active" : ""} onClick={() => toggle("customer") }>Cliente</NavLink>
                </NavItem>

              </Nav>

              <TabContent activeTab={activeTab}>

                <TabPane tabId="orderProductsDetails">
                    <TotalDisplay total={total} />
                    <div>
                        <OrderedProductDetailsView orderLines={orderLines} />
                    </div>
                    <TotalDisplay total={total} />
                
                </TabPane>

                <TabPane tabId="orderProductsLocation">
                    <div className="mt-2">
                        <OrderMapView location={location} />
                    </div>
                  
                </TabPane>

                <TabPane tabId="customer">
                  <CustomerView user={order.user} />
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

const TotalDisplay = ({ total }) => {
    return (

        <div className="form-group row p-1">
            <label htmlFor="generalTotalProp" className="col-sm-4 col-form-label font-weight-bold">Total</label>
            <label htmlFor="generalTotalProp" className="col-sm-4 col-form-label font-weight-bold text-success">RD${total}</label>
        </div>

    )
}

function CustomerView({ user }) {

    const { id, username, name, lastName, email, telephone } = user;

    return (
        <div className="shadow bg-white mt-2">
            <form className="p-4">

                <div className="form-group row border-bottom">
                    <label htmlFor="idProp" className="col-sm-6 col-form-label font-weight-bold">#:</label>
                    <label htmlFor="idProp" className="col-sm-6 col-form-label font-weight-bold">{id}</label>
                </div>

                <div className="form-group row border-bottom">
                    <label htmlFor="usernameProp" className="col-sm-6 col-form-label">Usuario:</label>
                    <label htmlFor="usernameProp" className="col-sm-6 col-form-label">{username}</label>
                </div>

                <div className="form-group row border-bottom">
                    <label htmlFor="nameProp" className="col-sm-6 col-form-label">Nombre: </label>
                    <label htmlFor="nameProp" className="col-sm-6 col-form-label">{name} {lastName}</label>
                </div>

                <div className="form-group row border-bottom">
                    <label htmlFor="emailProp" className="col-sm-6 col-form-label">Email: </label>
                    <label htmlFor="emailProp" className="col-sm-6 col-form-label">{email}</label>
                </div>

                <div className="form-group row border-bottom">
                    <label htmlFor="telephoneProp" className="col-sm-6 col-form-label">Teléfono: </label>
                    <label htmlFor="telephoneProp" className="col-sm-6 col-form-label">{telephone}</label>
                </div>

            </form>
        </div>
        
    )

}

export default ProductDetailsModal;