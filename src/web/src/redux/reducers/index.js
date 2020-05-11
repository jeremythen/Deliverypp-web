import { combineReducers } from "redux";
import categories from "./categories";
import isLoggedIn from "./isLoggedIn";

const rootReducer = combineReducers({ categories, isLoggedIn });

export default rootReducer;
