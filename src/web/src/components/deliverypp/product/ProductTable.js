import React from "react";

import { Table } from "react-bootstrap";

function ProductTable(props) {

  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>Id</th>
          <th>Categoría</th>
          <th>Descripción</th>
          <th>Total</th>
        </tr>
      </thead>
      <tbody>
        {props.products.map((product) => {
          return <tr key={product.id} onClick={() => props.onClick(product)}>
                    <td>{product.id}</td>
                    <td>{product.category}</td>
                    <td>{product.description}</td>
                    <td>{product.price}</td>
                </tr>;
        })}
      </tbody>
    </Table>
  );
}

export default ProductTable;
