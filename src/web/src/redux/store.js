import { createStore, applyMiddleware } from "redux";
import rootReducer from "./reducers";

import thunkMiddleware from 'redux-thunk';

import { loadCategories } from './actions';

// window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()

const store = createStore(
    rootReducer,
    applyMiddleware(thunkMiddleware)
);


export default store;


store.dispatch(loadCategories());