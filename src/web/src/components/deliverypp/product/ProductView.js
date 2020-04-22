
import React, { useState, useEffect } from 'react';

import ProductCard from './ProductCard';
import ProductTable from './ProductTable';

import './ProductView.css';

import axios from 'axios';

function ProductView() {

    const [cardView, setCardView] = useState(true);

    const [products, setProducts] = useState([]);

    useEffect(() => {

        const fetchData = async () => {

            const response = await axios.get('http://localhost:8080/api/product');

            const deliveryppResponse = response.data;

            if(deliveryppResponse.status === "SUCCESS") {
                setProducts(deliveryppResponse.response);
            } else {
                console.error('Error getting products.');
            }
        }

        fetchData();

    }, products);

    const getProductCards = () => {
        return products.map(product => {
            return <ProductCard key={product.id} {...product} />
        });
    }

    const getProductTable = () => {
        return <ProductTable products={products} />
    }
    

    return (
        
        <div className="ProductView">
            <div>
                <button className="btn btn-primary" onClick={() => setCardView(true)}>Card View</button>
                <button className="btn btn-secondary" onClick={() => setCardView(false)}>Card Table</button>
            </div>
            <div >
                {
                    cardView ? 
                            <div className="ProductCardContainer">
                                { getProductCards() }
                            </div> 
                        
                        : 
                        
                        <div className="ProductTableContainer">
                            {
                                getProductTable()
                            }
                        </div>
                        
                }
            </div>

        </div>
    );

}


const products = [
    {
        id: 1,
        description: 'Leche evaporada carnation 32 oz',
        category: 'Leche',
        price: 50.0,
        imgSrc: 'https://s3.amazonaws.com/grazecart/saboriza/images/1554136704_5ca23e80bbc99.jpg'
    },
    {
        id: 2,
        description: 'Description 2',
        category: 'Category 2',
        price: 50.0,
        imgSrc: 'https://s3.amazonaws.com/grazecart/saboriza/images/1554136704_5ca23e80bbc99.jpg'
    },
    {
        id: 3,
        description: 'Description 3',
        category: 'Category 3',
        price: 50.0,
        imgSrc: 'https://s3.amazonaws.com/grazecart/saboriza/images/1554136704_5ca23e80bbc99.jpg'
    },
    {
        id: 4,
        description: 'Description 3',
        category: 'Category 3',
        price: 50.0,
        imgSrc: 'https://s3.amazonaws.com/grazecart/saboriza/images/1554136704_5ca23e80bbc99.jpg'
    },
    {
        id: 5,
        description: 'Description 3',
        category: 'Category 3',
        price: 50.0,
        imgSrc: 'https://s3.amazonaws.com/grazecart/saboriza/images/1554136704_5ca23e80bbc99.jpg'
    },
    {
        id: 6,
        description: 'Description 3',
        category: 'Category 3',
        price: 50.0,
        imgSrc: 'https://s3.amazonaws.com/grazecart/saboriza/images/1554136704_5ca23e80bbc99.jpg'
    }
];




export default ProductView;