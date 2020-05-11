import { ADD_CATEGORY, UPDATE_CATEGORY, DELETE_CATEGORY, ADD_CATEGORIES } from "../actionTypes";

const categoryReducer = (state = [], action) => {
  switch (action.type) {
    case ADD_CATEGORIES: {
        const { categories } = action.payload;

        return categories;
    }
    case ADD_CATEGORY: {
      const { category } = action.payload;

      return [...state.categories, category];
    }
    case DELETE_CATEGORY: {
        const { category } = action.payload;
        const categories = state.categories.filter(({ id }) => id !== category.id);
        
        return categories;
    }
    case UPDATE_CATEGORY: {
        const updatedCategory = action.payload.category;
        const categories = state.categories.map(category => {
            if(category.id === updatedCategory.id) {
                return updatedCategory;
            }
        });

        return categories;
    }
    default:
      return state;
  }
}

export default categoryReducer;