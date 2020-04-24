import axios from "axios";

import deliverypp from '../deliverypp';

const basePath = deliverypp.getPath();

const { SUCCESS, ERROR } = deliverypp.STATUS;

const ProductService = {
    async fetch({method, path, data}) {
        try {

            const response = await axios[method](`${basePath}/${path}`, data);

            const responseData = this.handleResponse(response);
    
            return responseData;

        } catch(e) {
            console.error('Error fetching product data: ', e);
        }
    },
    handleResponse(response) {
        console.info('handleResponse: ', response);
        if(response && response.data) {
            const deliveryppResponse = response.data;
    
            if(deliveryppResponse.status === SUCCESS) {
                return deliveryppResponse.response;
            } else {
                console.error(deliveryppResponse.message);
                return null;
            }
        }
    },
    getProducts() {

        console.log('in getProducts, ')

        const responseData = this.fetch({
            method: 'get',
            path: 'api/product',
            data: {}
        });

        console.log('in getProducts, ', responseData)

        return responseData;

    },
    
    getProductById(productId) {

        const responseData = this.fetch({
            method: 'get',
            path: `api/product/${productId}`,
            data: {}
        });

        return responseData;
    
    },
    
    addProduct(product) {

        const responseData = this.fetch({
            method: 'post',
            path: `api/product`,
            data: product
        });

        return responseData;

    },
    
    deleteProductById(productId) {
        const responseData = this.fetch({
            method: 'delete',
            path: `api/product/${productId}`,
            data: {}
        });

        return responseData;
    }
};

export default ProductService;