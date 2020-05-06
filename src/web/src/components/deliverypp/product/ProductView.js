import React, { useState, useEffect } from "react";

import ProductCard from "./ProductCard";
import ProductTable from "./ProductTable";

import "./ProductView.css";

import Toolbar from '../../common/Toolbar';

import ProductService from '../../../services/ProductService';

import Modal from '../../common/Modal';

import {
    Card, CardImg, CardText, CardBody,
    CardTitle, Input, Label
  } from 'reactstrap';


function ProductView({ showAlert }) {
  const [cardView, setCardView] = useState(true);
  const [selectedProductId, setSelectedProductId] = useState(-1);

  const [products, setProducts] = useState([]);
  const [filterableProducts, setFilterableProducts] = useState([]);

  const [showModal, setShowModal] = useState(false);

  const [action, setAction] = useState('add');

  const [selectedProduct, setSelectedProduct] = useState({description: '', price: 0, imageUrl: '', category: ''});

  const [filter, setFilter] = useState('');

  const getProducts = async () => {

    const responseData = await ProductService.getProducts();
    if(responseData && responseData.success) {
      const products = responseData.response;
      setProducts(products);
        setFilterableProducts(products);
        
    } else {
      console.log('Error getting products')
      showAlert({ color: 'warning', message: 'Error obteniendo productos.'});
    }

  };

  const createProduct = async () => {

    const responseData = await ProductService.addProduct(selectedProduct);

    if(responseData && responseData.success) {

      const newProduct = responseData.response;

      const newProducts = [newProduct, ...products];

      setProducts(newProducts);
      filterProducts(newProducts, filter);
      showAlert({ color: 'success', message: 'Producto agregado.'});
      setShowModal(false);
    } else {
      console.log('Error getting products')
      showAlert({ color: 'warning', message: 'Error agregando producto.'});
    }
    
  };
  const updateProduct = async () => {

    const responseData = await ProductService.updateProduct(selectedProduct);

    if(responseData && responseData.success) {

      const newProduct = responseData.response;

      const newProducts = [...products];

      newProducts.forEach(product => {
        if(product.id === newProduct.id) {
          product.description = newProduct.description;
          product.price = newProduct.price;
          product.category = newProduct.category;
          product.imageUrl = newProduct.imageUrl;
          return false;
        }
      });

      setProducts(newProducts);
      filterProducts(newProducts, filter);
      showAlert({ color: 'success', message: 'Producto agregado.'});
      setShowModal(false);
    } else {
      console.log('Error getting products')
      showAlert({ color: 'warning', message: 'Error agregando producto.'});
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
    return filterableProducts.map(product => {
        const selected = product.id === selectedProductId ? 'selected' : '';
        return <ProductCard key={product.id} product={product} onClick={onProductCardClick} selected={selected} />;
    });
  };

  const getProductTable = () => {
    return <ProductTable products={filterableProducts} />;
  };

  const showEditProductForm = () => {

    setAction('edit');

    setShowModal(true);

  }

  const showAddProductForm = () => {

    setAction('add');
    setSelectedProduct({description: '', price: 0, imageUrl: '', category: ''});
    setShowModal(true);

  }

  const handleDeleteProduct = async () => {

    const deleteProductConfirm = window.confirm('Seguro quiere borrar el producto seleccionado?');

    if(deleteProductConfirm) {

      const responseData = await ProductService.deleteProductById(selectedProductId);

      if(responseData && responseData.success) {
        showAlert({ color: 'success', message: 'Producto eliminado.'});
        const filteredProducts = products.filter(product => product.id !== selectedProductId);
        setProducts(filteredProducts);
        filterProducts(filteredProducts, filter);
      } else {
        console.log('Error deleting product.')
        showAlert({ color: 'warning', message: 'Error eliminando producto.'});
      }
    }

  };

  const filterProducts = (products, value) => {
    if(value) {

      const filteredProducts = products.filter(product => {

          const keys = Object.keys(product);

          for(let key of keys) {

              if(key === 'category' || key === 'price' || key === 'description') {
                  let propValue = product[key];
                  if(propValue) {
                      propValue = '' + propValue;
                      if(propValue.includes(value)) {
                          return true;
                      }
                  }
              }
              
          }

          return false;

      });
      setFilterableProducts(filteredProducts);

  } else {
      setFilterableProducts(products);
  }
  }

  const handleSearch = (event) => {

    const value = event.target.value;

    setFilter(value);
    filterProducts(products, value);

  }

  const onSave = () => {

    switch(action) {
      case 'add':
        createProduct();
        break;
      case 'edit':
        updateProduct();
        break;
      default:
        console.error('Wrong operation.');
        break;
    }

  }

  const onCancel = () => {
    setShowModal(false);
  }

  const toggle = () => {
    setShowModal(!showModal);
  }

  const getActionDescription = () => {
      switch(action) {
        case 'add':
            return 'Agregar un producto';
        case 'edit':
            return 'Editar un producto';
        default:
          console.error('Wrong operation.');
          break;
      }
  }

  const onProductUpdate = product => {

    setSelectedProduct(product);

  }

  return (
    <div className="ProductView">
    <Modal
        onSave={onSave}
        onCancel={onCancel}
        showModal={showModal}
        toggle={toggle}
        title={getActionDescription()}
    >
        <ProductActionForm imageUrl={'product.png'} onProductUpdate={onProductUpdate} product={selectedProduct} />
    </Modal>

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
            onSearch={handleSearch}
            onEditClick={showEditProductForm}
            onAddClick={showAddProductForm}
            onDeleteClick={handleDeleteProduct}
            disabled={selectedProductId === -1}
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

function ProductActionForm(props) {


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

    return (
        <div>
        <Card>
            <CardImg top width="100%" src={imageUrl} />
            <CardBody>
                <CardTitle>
                    <Input type="text" name="category" id="category" placeholder="Categoría" value={props.product.category} onChange={onCategoryChange} />
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
                    <Input type="number" name="price" id="price" placeholder="Precio"  value={props.product.price} onChange={onPriceChange}/>
                </CardText>
            </CardBody>
        </Card>
    </div>
    )
}

export default ProductView;
