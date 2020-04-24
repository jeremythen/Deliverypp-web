import axios from "axios";

import deliverypp from '../deliverypp';

const basePath = deliverypp.getPath();

const { SUCCESS, ERROR } = deliverypp.STATUS;

const OrderService = {
    async fetch({method, path, data}) {
        try {

            const response = await axios[method](`${basePath}/${path}`, data);

            const responseData = this.handleResponse(response);
    
            return responseData;

        } catch(e) {
            console.error('Error fetching order data: ', e);
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
    getOrders() {

        const responseData = this.fetch({
            method: 'get',
            path: 'api/order',
            data: {}
        });

        return responseData;

    },
    
    getOrderById(orderId) {

        const responseData = this.fetch({
            method: 'get',
            path: `api/order/${orderId}`,
            data: {}
        });

        return responseData;
    
    },
    
    addOrder(order) {

        const responseData = this.fetch({
            method: 'post',
            path: `api/order`,
            data: order
        });

        return responseData;

    },
    
    deleteOrderById(orderId) {
        const responseData = this.fetch({
            method: 'delete',
            path: `api/order/${orderId}`,
            data: {}
        });

        return responseData;
    }
};

export default OrderService;