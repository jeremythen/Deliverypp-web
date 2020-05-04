import React from "react";

import { Table } from "reactstrap";

import './OrderView.css';

function OrderTable(props) {
  return (
    <div className="OrderTable">
      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>Id</th>
            <th>Usuario</th>
            <th>Email</th>
            <th>Teléfono</th>
            <th>Fecha</th>
            <th>Total</th>
            <th>Estado</th>
            <th>Comentario</th>
          </tr>
        </thead>
        <tbody>
          {props.orders.map((order) => {
            return (
              <tr className="order-row" key={order.id} onClick={() => props.onRowClick(order)}>
                <td>{order.id}</td>
                <td>{order.user.username}</td>
                <td>{order.user.email}</td>
                <td>{order.user.telephone}</td>
                <td>{order.createdAt}</td>
                <td>{order.total}</td>
                <td>{order.status}</td>
                <td>{order.comment}</td>
              </tr>
            )
          })}
        </tbody>
      </Table>
    </div>
  );
}

export default OrderTable;
