
import React from 'react';

import { Card, Button } from 'react-bootstrap';

function ProductCard(props) {
    return (
        <div className="ProductCard m-1 p-2">
            <Card style={{ width: '18rem' }}>
                <Card.Img variant="top" src={props.imgSrc} />
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