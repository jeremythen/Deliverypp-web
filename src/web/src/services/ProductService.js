import axios from "axios";

import Deliverypp from '../Deliverypp';

const basePath = Deliverypp.getPath();

const ProductService = {
    async handleResponse(response) {
        if(response && response.data) {
            const deliveryppResponse = response.data;
    
            return deliveryppResponse;
        }
    },
    async getProducts() {

        const response = await axios.get(`${basePath}/api/products`);

        return this.handleResponse(response);

    },
    
    async getProductById(productId) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.get(`${basePath}/api/products/${productId}`, { headers });

        return this.handleResponse(response);
    
    },
    
    async addProduct(product) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.post(`${basePath}/api/products`, product, { headers });

        return this.handleResponse(response);

    },
    async updateProduct(product) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.put(`${basePath}/api/products`, product, { headers });

        return this.handleResponse(response);

    },
    async deleteProductById(productId) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.delete(`${basePath}/api/products/${productId}`, { headers });

        return this.handleResponse(response);


    }
};

export default ProductService;