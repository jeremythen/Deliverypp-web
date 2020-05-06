import React, { useState } from "react";

import { Table } from "reactstrap";

import './OrderView.css';

import StatusClassMapper from '../../../util/StatusClassMapper';

const StatusTranslateMapper = {
  ORDERED: 'Ordenado',
  PAID: 'Pagado',
  ACQUIRING: 'Adquiriendo',
  ACQUIRED: 'Adquirido',
  TRANSIT: 'Tránsito',
  DELIVERED: 'Entregado'
};

function OrderTable(props) {

  const [activeRow, setActiveRow] = useState(null);

  const handleOnRowClick = (order) => {
    setActiveRow(order);
    props.onRowClick(order.id)
  }

  const onColumnClick = (prop) => {
    props.onColumnClick(prop);
  }

  return (
    <div className="OrderTable">
      <Table bordered hover responsive>
        <thead>
          <tr>
            <th onClick={() => onColumnClick('id')}>Id</th>
            <th onClick={() => onColumnClick('status')}>Estado</th>
            <th onClick={() => onColumnClick('username')}>Usuario</th>
            <th onClick={() => onColumnClick('email')}>Email</th>
            <th onClick={() => onColumnClick('telephone')}>Teléfono</th>
            <th onClick={() => onColumnClick('createdAt')}>Fecha</th>
            <th onClick={() => onColumnClick('total')}>Total</th>
            <th onClick={() => onColumnClick('comment')}>Comentario</th>
          </tr>
        </thead>
        <tbody>
          {props.orders.map((order) => {
            return (
              <tr className={`order-row ${activeRow && activeRow.id === order.id ? 'selected' : ''}`} key={order.id} onClick={() => handleOnRowClick(order)} >
                <td>{order.id}</td>
                <td className={`text-${StatusClassMapper.getClassStatusClass(order.status)}`}>{ StatusTranslateMapper[order.status] }</td>
                <td>{order.username}</td>
                <td>{order.email}</td>
                <td>{order.telephone}</td>
                <td>{order.createdAt}</td>
                <td>{order.total}</td>
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
