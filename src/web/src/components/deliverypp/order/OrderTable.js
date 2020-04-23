import React, { useState, useEffect } from "react";

import { Table } from "react-bootstrap";

function OrderTable(props) {

  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>Id</th>
          <th>Total</th>
          <th>Estado</th>
          <th>Comentario</th>
        </tr>
      </thead>
      <tbody>
        {props.orders.map((order) => {
          return <tr key={order.id} onClick={() => props.onRowClick(order)}>
                    <td>{order.id}</td>
                    <td>{order.total}</td>
                    <td>{order.status}</td>
                    <td>{order.comment}</td>
                </tr>;
        })}
      </tbody>
    </Table>
  );
}

export default OrderTable;
