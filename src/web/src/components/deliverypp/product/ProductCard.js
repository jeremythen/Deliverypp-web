
import React from 'react';

import {
    Card, CardImg, CardText, CardBody,
    CardTitle
  } from 'reactstrap';
  
import './ProductCard.css';

function ProductCard(props) {
    return (
        <div className={`ProductCard m-1 p-2 ${props.selected}`} onClick={() => props.onClick(props)}>
            <Card style={{ width: '18rem' }}>
                <CardImg top width="100%" src={props.imageUrl} alt={props.description} />
                <CardBody>
                    <CardTitle>{props.category}</CardTitle>
                    <CardText>
                        {
                            props.description
                        }
                    </CardText>
                    <CardText>
                        {
                            `RD$${props.price}`
                        }
                    </CardText>
                </CardBody>
            </Card>
        </div>
    )
}

export default ProductCard;