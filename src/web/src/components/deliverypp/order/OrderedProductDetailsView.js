import React, { useState } from 'react';

import { Card, CardImg, CardBody, CardTitle } from 'reactstrap';

function OrderedProductDetailsView({orderLines}) {

    const [mappedOrderLines, setMappedOrderLines] = useState(orderLines.map(orderLine => ({ ...orderLine, imageHidden: true })));
  
    const toggleImageHidden = (id) => {
      const newMappedOrderLines = mappedOrderLines.map(orderLine => {
        if(orderLine.product.id === id) {
          orderLine.imageHidden = !orderLine.imageHidden;
        }
        return orderLine;
      });
      setMappedOrderLines(newMappedOrderLines);
    }
  
    return (
      <>
        {mappedOrderLines.map(({ quantity, product, imageHidden }) => (
          <Card key={product.id} style={{ marginBottom: 4, marginTop: 4 }} className="shadow p-2 mb-2 bg-white">
            
            {
              imageHidden && <div className="imageIconContainer text-center" title="Mostrar image del producto" onClick={() => toggleImageHidden(product.id)}><i className="fas fa-image" style={{fontSize: 24, color: 'grey'}}></i></div>
            }
  
            {
              !imageHidden && (
                <CardImg
                  top
                  width="100%"
                  src={product.imageUrl}
                  alt={product.description}
                  onClick={() => toggleImageHidden(product.id)}
                  title="Ocultar imagel del producto"
                  className="cardImgElem"
                />
              )
            }
            
            <CardBody>
              <CardTitle className="text-center font-weight-bold">{product.description}</CardTitle>
                <form>

                    <div className="form-group row border-bottom">
                        <label htmlFor="idPrice" className="col-6 col-form-label">Precio</label>
                        <label htmlFor="idPrice" className="col-sm-6 col-form-label">RD${product.price}</label>
                    </div>

                    <div className="form-group row border-bottom">
                        <label htmlFor="quantityProp" className="col-sm-6 col-form-label">Cantidad</label>
                        <label htmlFor="quantityProp" className="col-sm-6 col-form-label">{quantity}</label>
                    </div>
 
                    <div className="form-group row border-bottom">
                        <label htmlFor="totalProp" className="col-sm-6 col-form-label font-weight-bold">Total</label>
                        <label htmlFor="totalProp" className="col-sm-6 col-form-label font-weight-bold text-success">RD${product.price * quantity}</label>
                    </div>
                
                </form>

            </CardBody>
          </Card>
        ))}
      </>
    )
  }

  export default OrderedProductDetailsView;