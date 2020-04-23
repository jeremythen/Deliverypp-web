import React, { useState, useEffect } from "react";

import ProductCard from "./ProductCard";
import ProductTable from "./ProductTable";

import "./ProductView.css";

import axios from "axios";

import Toolbar from '../../common/Toolbar';

function ProductView() {
  const [cardView, setCardView] = useState(true);
  const [selectedProductId, setSelectedProductId] = useState(-1);

  const [products, setProducts] = useState([]);

  const fetchData = async () => {
    const response = await axios.get("http://localhost:8080/api/product");
  
    const deliveryppResponse = response.data;

    if (deliveryppResponse.status === "SUCCESS") {
      setProducts(deliveryppResponse.response);
    } else {
      console.error("Error getting products.");
    }
  };

  useEffect(() => {
    fetchData();
  }, [products.length === 0]);

  const onProductCardClick = product => {
    console.log('in onProductCardClick product', product)
    setSelectedProductId(product.id);

    console.log('in onProductCardClick id: ', product.id)
  }

  const getProductCards = () => {
    return products.map((product) => {
        const selected = product.id == selectedProductId ? 'selected' : '';
        return <ProductCard key={product.id} {...product} onClick={onProductCardClick} selected={selected} />;
    });
  };

  const getProductTable = () => {
    return <ProductTable products={products} />;
  };

  const handleProductEdit = () => {
      console.log('in handleProductEdit for product id: ', selectedProductId)
  }

  const handleAddProduct = () => {
    console.log('in handleAddProduct for product id: ', selectedProductId)
  }

  const handleDeleteProduct = async () => {

    const response = await axios.delete(`http://localhost:8080/api/product/${selectedProductId}`);

    console.log('response', response);
    console.log('response data', response.data);

    fetchData();

    console.log('in handleDeleteProduct for product id: ', selectedProductId)
  }

  return (
    <div className="ProductView">
      {false && (
        <div>
          <button className="btn btn-primary" onClick={() => setCardView(true)}>
            Card View
          </button>
          <button
            className="btn btn-secondary"
            onClick={() => setCardView(false)}
          >
            Card Table
          </button>
        </div>
      )}

      <Toolbar
        onEditClick={handleProductEdit}
        onAddClick={handleAddProduct}
        onDeleteClick={handleDeleteProduct}
        disabled={selectedProductId == -1}
    />

      <div>
        {cardView ? (
          <div className="ProductCardContainer">{getProductCards()}</div>
        ) : (
          <div className="ProductTableContainer">{getProductTable()}</div>
        )}
      </div>
    </div>
  );
}

export default ProductView;
