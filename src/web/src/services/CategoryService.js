import axios from "axios";

import Deliverypp from '../Deliverypp';

const basePath = Deliverypp.getPath();

const CategoryService = {
    generateErrorResponse(message) {
        const responseData = {
            success: false,
            status: 'ERROR',
            message: message
        };

        return responseData;
    },
    handleResponse(response) {
        if(response && response.data) {
            const deliveryppResponse = response.data;
    
            return deliveryppResponse;
        }
    },
    async getCategories() {

        try {

            const headers = this.getHeader();

            const response = await axios.get(`${basePath}/api/categories`, { headers });

            return this.handleResponse(response);
    
        } catch(e) {

            return this.generateErrorResponse(e.message);

        }

    },
    
    async addCategory(category) {

        try {
            
            const headers = this.getHeader();

            const response = await axios.post(`${basePath}/api/categories`, category, { headers });
    
            return this.handleResponse(response);
    
        } catch(e) {

            return this.generateErrorResponse(e.message);

        }

    },

    async updateCategory(category) {

        try {
            
            const headers = this.getHeader();
    
            const response = await axios.put(`${basePath}/api/categories`, category, { headers });
    
            return this.handleResponse(response);
    
        } catch(e) {

            return this.generateErrorResponse(e.message);

        }

    },

    async deleteCategory(categoryId) {

        try {
            
            const headers = this.getHeader();

            const response = await axios.put(`${basePath}/api/categories/${categoryId}`, { headers });
    
            return this.handleResponse(response);
    
        } catch(e) {

            return this.generateErrorResponse(e.message);

        }

    },

    getToken() {
        return localStorage.getItem('deliverypp_user_login_token');
    },

    getHeader() {

        const token = this.getToken();

        const headers = {
            Authorization: `Bearer ${token}`
        };

        return headers;
    }
    
};

export default Object.freeze(CategoryService);