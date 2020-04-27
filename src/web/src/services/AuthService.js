import axios from "axios";

import Deliverypp from '../Deliverypp';

const basePath = Deliverypp.getPath();

const AuthService = {
    generateErrorResponse(error) {

        let message = error.message;

        if(message.includes('401')) {
            message = "Credenciales incorrectas. Verifique que su usuario y contraseña son correctos."
        }

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
    async register(user) {

        try {
            const response = await axios.post(`${basePath}/api/register`, user);

            const responseData = this.handleResponse(response);
    
            return responseData;
    
        } catch(e) {

            return this.generateErrorResponse(e.message);

        }

    },
    async login(user) {

        try {
            const response = await axios.post(`${basePath}/api/login`, user);

            const responseData = this.handleResponse(response);

            return responseData;
    
        } catch(e) {
            
            return this.generateErrorResponse(e);

        }
        
    },
    async getUserByToken(token) {

        try {

            const headers = {
                Authorization: `Bearer ${token}`
            };
    
            const response = await axios.get(`${basePath}/api/auth/${token}`, headers);
    
            const responseData = this.handleResponse(response);
    
            return responseData;
    
        } catch(e) {

            return this.generateErrorResponse(e.message);
            
        }

    },
    async logout(token) {

        try {

            const headers = {
                Authorization: `Bearer ${token}`
            };
    
            const response = await axios.get(`${basePath}/api/auth/${token}`, headers);
    
            const responseData = this.handleResponse(response);
    
            return responseData;
    
        } catch(e) {

            return this.generateErrorResponse(e.message);
            
        }

    }
    
};

export default AuthService;