
import React from 'react';

import { Card, CardImg, CardText, CardBody, CardTitle } from 'reactstrap';
  
import './ProductCard.css';

function ProductCard({ product, selected, onClick }) {

    const { imageUrl, description, category, price } = product;

    return (
        <div className={`ProductCard m-1 p-2 ${selected}`} onClick={() => onClick(product)}>
            <Card style={{ width: '18rem' }}>
                <CardImg top width="100%" src={imageUrl} alt={description} />
                <CardBody>
                    <CardTitle className="text-muted font-italic">{category}</CardTitle>
                    <CardText>
                        {
                            description
                        }
                    </CardText>
                    <CardText className="font-weight-bold text-success">
                        {
                            `RD$${price}`
                        }
                    </CardText>
                </CardBody>
            </Card>
        </div>
    )
}

export default ProductCard;