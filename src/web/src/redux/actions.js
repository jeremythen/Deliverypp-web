import { GET_CATEGORIES, ADD_CATEGORY, DELETE_CATEGORY, UPDATE_CATEGORY, ADD_CATEGORIES } from "./actionTypes";

import CategoryService from '../services/CategoryService';

//http://localhost:8080

import store from './store';


export const loadCategories = () => {
    return dispatch => {
        CategoryService.getCategories()
        .then(response => {
            if(response.success) {
                dispatch(addCategories(response.response));
            }
        });
    }
}

export const addCategories = (categories) => ({
    type: ADD_CATEGORIES,
    payload: {
        categories
    }
})

export const getCategories = () => ({
    type: GET_CATEGORIES
});

export const deleteCategory = category => ({
    type: DELETE_CATEGORY,
    payload: {
        category
    }
});

export const addCategory = category => ({
    type: ADD_CATEGORY,
    payload: {
        category
    }
});

export const updateCategory = category => ({
    type: UPDATE_CATEGORY,
    payload: {
        category
    }
});


