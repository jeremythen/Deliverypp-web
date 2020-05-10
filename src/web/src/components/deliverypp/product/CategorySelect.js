import React, { useEffect, useState } from 'react';

import { Label, Input } from 'reactstrap';

import CategoryService from '../../../services/CategoryService';

function CategorySelect({ onCategoryChange = f => {} }) {

    const [categories, setCategories] = useState([]);

    const getOrders = async () => {

        const response = await CategoryService.getCategories();
        console.log('resposne', response)
        if(response.success) {
            setCategories(response.response);
        } else {
            console.error(response.message);
        }

    }

    useEffect(() => {
        getOrders();
    }, []);

    const handleOnChange = () => {

        //

        onCategoryChange();

    }

    return (
      <div className="d-flex flex-row justify-content-center align-items-cente mr-2">
        <Label className="mr-2" htmlFor="categorySelect">Categorías</Label>
        <Input type="select" id="categorySelect" onChange={handleOnChange}>
          <option value={''}></option>
          {
              categories.map(category => (
                <option value={category.key}>{category.category}</option>
              ))
          }
        </Input>
      </div>
    )
  }

  export default CategorySelect;