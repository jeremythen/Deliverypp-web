import React from 'react';

import { Label, Input } from 'reactstrap';

import { useSelector } from 'react-redux';

function CategorySelect({ onCategoryChange }) {

    const categories = useSelector(state => state.categories);

    const handleOnChange = (event) => {
        const category = event.target.value;
        onCategoryChange(category);
    }

    return (
      <div className="d-flex flex-row justify-content-center align-items-cente mr-2">
        <Label className="mr-2" htmlFor="categorySelect">Categorías</Label>
        <Input type="select" id="categorySelect" onChange={handleOnChange}>
          <option key={0} value={''}></option>
          {
              categories.map(({ category, id }) => (
                <option key={id}>{category}</option>
              ))
          }
        </Input>
      </div>
    )
  }

  export default CategorySelect;