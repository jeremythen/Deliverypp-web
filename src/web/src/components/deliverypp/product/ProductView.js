import React, { useState, useEffect } from "react";

import ProductCard from "./ProductCard";
import ProductTable from "./ProductTable";

import "./ProductView.css";

import Toolbar from './Toolbar';

import ProductService from '../../../services/ProductService';

import Modal from '../../common/Modal';

import ProductActionForm from './ProductActionForm';

import Alert from '../../common/Alert';
import { batch } from "react-redux";

function ProductView() {

  const [cardView, setCardView] = useState(() => true);
  const [selectedProductId, setSelectedProductId] = useState(() => -1);
  const [products, setProducts] = useState(() => []);
  const [filterableProducts, setFilterableProducts] = useState(() => []);
  const [showModal, setShowModal] = useState(() => false);
  const [action, setAction] = useState(() => 'add');
  const [selectedProduct, setSelectedProduct] = useState(() => ({description: '', price: 0, imageUrl: '', category: ''}));
  const [filter, setFilter] = useState(() => '');
  const [categoryFilter, setCategoryFilter] = useState(() => '');
  const [alertState, setAlertState] = useState(() => ({ color: 'success', show: false, message: '' }));

  const showAlert = alert => {
    setAlertState(alert);
    setTimeout(() => {
      setAlertState({ color: 'success', show: false, message: '' });
    }, 2000);
  }

  const getProducts = async () => {
    const responseData = await ProductService.getProducts();
    if(responseData && responseData.success) {
      const products = responseData.response;
      setProducts(products);
        setFilterableProducts(products);
    } else {
      console.error('Error getting products')
      showAlert({ color: 'warning', message: 'Error obteniendo productos.'});
    }
  };

  const createProduct = async () => {
    const responseData = await ProductService.addProduct(selectedProduct);
    if(responseData && responseData.success) {
      const newProduct = responseData.response;
      const newProducts = [newProduct, ...products];
      setProducts(newProducts);
      filterByCategoryAndSearchText(categoryFilter, filter, newProducts);
      showAlert({ color: 'success', message: 'Producto agregado.'});
      setShowModal(false);
    } else {
      console.error('Error getting products')
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
      filterByCategoryAndSearchText(categoryFilter, filter, newProducts);
      showAlert({ color: 'success', message: 'Producto agregado.'});
      setShowModal(false);
    } else {
      console.error('Error adding products.');
      showAlert({ color: 'warning', message: 'Error agregando producto.'});
    }

  };

  const cloneProduct = async () => {
    const responseData = await ProductService.cloneProduct(selectedProduct);
    if(responseData && responseData.success) {
      const newProduct = responseData.response;
      const newProducts = [newProduct, ...products];
      setProducts(newProducts);
      filterByCategoryAndSearchText(categoryFilter, filter, newProducts);
      showAlert({ color: 'success', message: 'Producto clonado.'});
      setShowModal(false);
    } else {
      console.error('Error cloning product');
      showAlert({ color: 'warning', message: 'Error clonando producto.'});
    }

  }

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

  const showCloneProductForm = () => {
    setAction('clone');
    setShowModal(true);
  }

  const handleDeleteProduct = async () => {
    const deleteProductConfirm = window.confirm('Seguro quiere borrar el producto seleccionado?');
    if(deleteProductConfirm) {
      const responseData = await ProductService.deleteProductById(selectedProductId);
      if(responseData && responseData.success) {
        showAlert({ color: 'success', message: 'Producto eliminado.'});
        let filteredProducts = products.filter(product => product.id !== selectedProductId);
        setProducts(filteredProducts);
        filterByCategoryAndSearchText(categoryFilter, filter, filteredProducts);
      } else {
        console.error('Error deleting product.')
        showAlert({ color: 'warning', message: 'Error eliminando producto.'});
      }
    }
  };

  const filterProducts = (value, products) => {
    if(value) {
      const filteredProducts = products.filter(product => {
          const keys = Object.keys(product);
          for(let key of keys) {
              if(key === 'category' || key === 'price' || key === 'description') {
                  let propValue = product[key];
                  if(propValue) {
                      propValue = '' + propValue;
                      if(propValue.toLowerCase().includes(value.toLowerCase())) {
                          return true;
                      }
                  }
              }
          }
          return false;
      });
      return filteredProducts;
    }

    return products;

  }

  const onCategoryChange = category => {
    setCategoryFilter(category);
    filterByCategoryAndSearchText(category, filter, products);
  }

  const filterProductByCategory = (category, products) => {
    if(!category) {
      return products;
    }
    const filteredProducts = products.filter(product => product.category === category);
    return filteredProducts;
  }

  const filterByCategoryAndSearchText = (category, searchText) => {
    let filteredProducts = filterProductByCategory(category, products);
    filteredProducts = filterProducts(searchText, filteredProducts);
    setFilterableProducts(filteredProducts);
  }

  const handleSearch = (event) => {
    const value = event.target.value;
    const returnedFromSetFilter = setFilter(value);
    console.error('returnedFromSetFilter', returnedFromSetFilter)
    filterByCategoryAndSearchText(categoryFilter, value, products);
  }

  const onSave = () => {
    switch(action) {
      case 'add':
        createProduct();
        break;
      case 'edit':
        updateProduct();
        break;
      case 'clone':
          cloneProduct();
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
          return 'Agregar producto';
      case 'edit':
          return 'Editar producto';
      case 'clone':
        return 'Clonar producto y ocultar original';
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

      <Alert {...alertState} />

      <Modal
          onSave={onSave}
          onCancel={onCancel}
          showModal={showModal}
          toggle={toggle}
          title={getActionDescription()}
      >
          <ProductActionForm imageUrl={'product.png'} action={action} onProductUpdate={onProductUpdate} product={selectedProduct} />
      </Modal>

      <Toolbar
          onSearch={handleSearch}
          onEditClick={showEditProductForm}
          onAddClick={showAddProductForm}
          onCloneClick={showCloneProductForm}
          onDeleteClick={handleDeleteProduct}
          disabled={selectedProductId === -1}
          onCategoryChange={onCategoryChange}
      />

      <div>
        <div className="ProductCardContainer">{getProductCards()}</div>
      </div>
    </div>
  );
}

export default ProductView;
