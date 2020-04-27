import React, { useState, useEffect } from 'react';

import ProductCard from './ProductCard';

import ProductService from '../../../services/ProductService';

import './ProductView.css';

function OrderProductsView({ showAlert }) {

    const [products, setProducts ] = useState([]);
    const [filterableProducts, setFilterableProducts ] = useState([]);
    const [selectedProductId, setSelectedProductId] = useState(-1);
    const [selectedProduct, setSelectedProduct] = useState({});

    const getProducts = async () => {

        const responseData = await ProductService.getProducts();

        if(responseData.success) {
            const products = responseData.response;
            setProducts(products);
              setFilterableProducts(products);
              
          } else {
            console.log('Error getting products')
            showAlert({ color: 'warning', message: 'Error obteniendo productos.'});
          }

    };

    useEffect(() => {
        getProducts();
    }, []);

    const onProductCardClick = product => {
        setSelectedProduct(product);
        setSelectedProductId(product.id);
    };

    const getProductCards = () => {
        return filterableProducts.map((product) => {
            const selected = product.id === selectedProductId ? 'selected' : '';
            return <ProductCard key={product.id} {...product} onClick={onProductCardClick} selected={selected} />;
        });
      };

    return (
        <div className="ProductCardContainer">

            {
                getProductCards()
            }
            
        </div>
    )

}

export default OrderProductsView;