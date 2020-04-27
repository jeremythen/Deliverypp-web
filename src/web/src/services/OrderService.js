import axios from "axios";

import Deliverypp from '../Deliverypp';

const basePath = Deliverypp.getPath();

const OrderService = {
    handleResponse(response) {
        if(response && response.data) {
            const deliveryppResponse = response.data;
    
            return deliveryppResponse;
        }
    },
    async getOrders() {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.get(`${basePath}/api/order`, { headers });

        return this.handleResponse(response);

    },
    
    async getOrderById(orderId) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.get(`${basePath}/api/order/${orderId}`, { headers });

        return this.handleResponse(response);
    
    },
    
    async addOrder(order) {

        const token = localStorage.getItem('deliverypp_user_login_token');

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.post(`${basePath}/api/order`, order, { headers });

        return this.handleResponse(response);

    },
    
};

export default OrderService;