import axios from "axios";

import Deliverypp from '../Deliverypp';

const basePath = Deliverypp.getPath();

const ProductService = {
    generateErrorResponse(message) {
        const responseData = {
            success: false,
            status: 'ERROR',
            message: message
        };

        return responseData;
    },
    async handleResponse(response) {
        if(response && response.data) {
            const deliveryppResponse = response.data;
    
            return deliveryppResponse;
        }
    },
    async getProducts() {

        try {
            const response = await axios.get(`${basePath}/api/products`);
            return this.handleResponse(response);
        } catch(e) {
            return this.generateErrorResponse(e.message);
        }

    },
    
    async getProductById(productId = {}) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        try {
            const response = await axios.get(`${basePath}/api/products/${productId}`, { headers });

            return this.handleResponse(response);
        } catch(e) {
            return this.generateErrorResponse(e.message);
        }

    },
    
    async addProduct(product = {}) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        try {
            const response = await axios.post(`${basePath}/api/products`, product, { headers });

            return this.handleResponse(response);
        } catch(e) {
            return this.generateErrorResponse(e.message);
        }

    },
    async updateProduct(product = {}) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        try {
            const response = await axios.put(`${basePath}/api/products`, product, { headers });

            return this.handleResponse(response);
        } catch(e) {
            return this.generateErrorResponse(e.message);
        }

    },
    async cloneProduct(product = {}) {

        product.id = 0;

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        try {
            const response = await axios.post(`${basePath}/api/products/clone`, product, { headers });

            return this.handleResponse(response);
        } catch(e) {
            return this.generateErrorResponse(e.message);
        }
   
    },
    async deleteProductById(productId = -1) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        try {
            const response = await axios.delete(`${basePath}/api/products/${productId}`, { headers });

            return this.handleResponse(response);
        } catch(e) {
            return this.generateErrorResponse(e.message);
        }

    }
};

export default ProductService;