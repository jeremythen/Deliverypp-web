import axios from "axios";

import deliverypp from '../deliverypp';

const basePath = deliverypp.getPath();

const { SUCCESS, ERROR } = deliverypp.STATUS;

const ProductService = {
    async handleResponse(response) {
        console.info('handleResponse: ', response);
        if(response && response.data) {
            const deliveryppResponse = response.data;
    
            return deliveryppResponse;
        }
    },
    async getProducts() {

        const response = await axios.get(`${basePath}/api/product`);

        return this.handleResponse(response);

    },
    
    async getProductById(productId) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.get(`${basePath}/api/product/${productId}`, { headers });

        return this.handleResponse(response);
    
    },
    
    async addProduct(product) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.post(`${basePath}/api/product`, product, { headers });

        return this.handleResponse(response);

    },
    async updateProduct(product) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.put(`${basePath}/api/product`, product, { headers });

        return this.handleResponse(response);

    },
    async deleteProductById(productId) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.delete(`${basePath}/api/product/${productId}`, { headers });

        return this.handleResponse(response);


    }
};

export default ProductService;