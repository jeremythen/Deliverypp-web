import React from 'react';

import { useSelector } from 'react-redux';

import { Card, CardImg, CardText, CardBody, CardTitle, Input, Label } from 'reactstrap';

function ProductActionForm(props) {

    const categories = useSelector(state => state.categories);

    const onImageUrlChange = (e) => {
      let value = e.target.value;
      if(!value) {
          value = 'product.png';
      }
      const product = {...props.product};
      product.imageUrl = value;
      props.onProductUpdate(product);
    }

    const onCategoryChange = (e) => {
      const value = e.target.value;
      const product = {...props.product};
      product.category = value;
      props.onProductUpdate(product);
    }

    const onDescriptionChange = (e) => {
      const value = e.target.value;
      const product = {...props.product};
      product.description = value;
      props.onProductUpdate(product);
    }

    const onPriceChange = (e) => {
      const value = e.target.value;
      const product = {...props.product};
      product.price = value;
      props.onProductUpdate(product);
    }

    const imageUrl = props.product.imageUrl ? props.product.imageUrl : 'product.png';
    const priceEditable = props.action === 'edit';

    return (
        <div>
        <Card>
            <CardImg top width="100%" src={imageUrl} />
            <CardBody>
                <CardTitle>
                    <Input type="select" name="categorySelect" id="categorySelect" value={props.product.category} onChange={onCategoryChange}>
                        <option>{''}</option>
                        {
                            categories.map(({ key, category }) => (
                                <option>{category}</option>
                            ))
                        }
                    </Input>
                </CardTitle>
                <CardText>
                    <Label for="imageUrl">URL de la imagen</Label>
                    <Input type="url" name="imageUrl" id="imageUrl"  value={imageUrl} onChange={onImageUrlChange}/>
                </CardText>
                <CardText>
                    <Label for="description">Descripción</Label>
                    <Input type="textarea" name="description" id="description" value={props.product.description} onChange={onDescriptionChange} />
                </CardText>
                <CardText>
                    <Label for="price">Precio</Label>
                    <Input type="number" name="price" id="price" placeholder="Precio" disabled={priceEditable} value={props.product.price} onChange={onPriceChange}/>
                </CardText>
            </CardBody>
        </Card>
    </div>
    )
}

export default ProductActionForm;