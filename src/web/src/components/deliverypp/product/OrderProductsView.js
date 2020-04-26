import React, { useState, useEffect } from 'react';

import ProductCard from './ProductCard';

import ProductService from '../../../services/ProductService';

import './ProductView.css';

function OrderProductsView() {

    const [products, setProducts ] = useState([]);
    const [filterableProducts, setFilterableProducts ] = useState([]);
    const [selectedProductId, setSelectedProductId] = useState(-1);
    const [selectedProduct, setSelectedProduct] = useState({});

    const getProducts = async () => {

        const products = await ProductService.getProducts();

        if(Array.isArray(products)) {
            setProducts(products);
            setFilterableProducts(products);
        } else {
            console.error('Error getting products.');
        }

    };

    useEffect(() => {
        getProducts();
    }, [products.length === 0]);

    const onProductCardClick = product => {
        setSelectedProduct(product);
        setSelectedProductId(product.id);
    };

    const getProductCards = () => {
        return filterableProducts.map((product) => {
            const selected = product.id == selectedProductId ? 'selected' : '';
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