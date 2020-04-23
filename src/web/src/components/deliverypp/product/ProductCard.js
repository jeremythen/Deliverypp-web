
import React from 'react';

import { Card, Button } from 'react-bootstrap';

import './ProductCard.css';

function ProductCard(props) {
    return (
        <div className={`ProductCard m-1 p-2 ${props.selected}`} onClick={() => props.onClick(props)}>
            <Card style={{ width: '18rem' }}>
                <Card.Img variant="top" src={props.imageUrl} />
                <Card.Body>
                    <Card.Title>{props.category}</Card.Title>
                    <Card.Text>
                        {
                            props.description
                        }
                    </Card.Text>
                    <Card.Text>
                        {
                            `RD$${props.price}`
                        }
                    </Card.Text>
                </Card.Body>
            </Card>
        </div>
    )
}

export default ProductCard;