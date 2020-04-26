import axios from "axios";

import deliverypp from '../deliverypp';

const basePath = deliverypp.getPath();

const { SUCCESS, ERROR } = deliverypp.STATUS;

const AuthService = {
    handleResponse(response) {
        console.info('handleResponse: ', response);
        if(response && response.data) {

            const deliveryppResponse = response.data;

            return deliveryppResponse;

        }
    },
    async register(user) {

        const response = await axios.post(`${basePath}/api/register`, user);

        const responseData = this.handleResponse(response);

        return responseData;

    },
    async login(user) {

        const response = await axios.post(`${basePath}/api/login`, user);

        const responseData = this.handleResponse(response);

        return responseData;
    },
    async getUserByToken(token) {

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.get(`${basePath}/api/auth/${token}`, headers);

        const responseData = this.handleResponse(response);

        return responseData;

    },
    async logout(token) {

        const headers = {
            Authorization: `Bearer ${token}`
        };

        const response = await axios.get(`${basePath}/api/auth/${token}`, headers);

        const responseData = this.handleResponse(response);

        return responseData;

    }
    
};

export default AuthService;