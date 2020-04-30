import axios from "axios";

import Deliverypp from '../Deliverypp';

const basePath = Deliverypp.getPath();

const OrderService = {
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
    async getOrders() {

        try {
            const token = localStorage.getItem('deliverypp_user_login_token');

            const headers = {
                Authorization: `Bearer ${token}`
            };

            const response = await axios.get(`${basePath}/api/orders`, { headers });

            return this.handleResponse(response);
    
        } catch(e) {

            return this.generateErrorResponse(e.message);

        }

    },
    
    async getOrderById(orderId) {

        try {

            const token = localStorage.getItem('deliverypp_user_login_token');

            const headers = {
                Authorization: `Bearer ${token}`
            };
    
            const response = await axios.get(`${basePath}/api/orders/${orderId}`, { headers });
    
            return this.handleResponse(response);
    
        } catch(e) {

            return this.generateErrorResponse(e.message);

        }
    
    },
    
    async addOrder(order) {

        try {
            
            const token = localStorage.getItem('deliverypp_user_login_token');

            const headers = {
                Authorization: `Bearer ${token}`
            };
    
            const response = await axios.post(`${basePath}/api/orders`, order, { headers });
    
            return this.handleResponse(response);
    
        } catch(e) {

            return this.generateErrorResponse(e.message);

        }

    },
    
};

export default OrderService;