import React from "react";

import './Toolbar.css';

function Toolbar(props) {
  return (
    <div className="Toolbar p-2 border rounded">
      <div className="searchInputContainer p-1">
          <div className="input-group">
            <div className="input-group-prepend">
              <span className="input-group-text" id="inputGroupPrepend2">
                <i className="fa fa-search"></i>
              </span>
            </div>
            <input
                onChange={props.onSearch}
                type="text"
                className="form-control"
                placeholder="Buscar"
                aria-describedby="inputGroupPrepend2"
                required
            />
          </div>
      </div>

      <div className="actionButtonsContainer p-1">
        <button onClick={props.onEditClick} disabled={props.disabled} className="btn btn-secondary mr-1" title="Editar Producto">
          <i className="fa fa-pencil"></i>
        </button>

        <button onClick={props.onAddClick} className="btn btn-info mr-1" title="Agregar Product">
          <i className="fa fa-plus"></i>
        </button>

        <button onClick={props.onDeleteClick} disabled={props.disabled} className="btn btn-danger" title="Borrar Product">
          <i className="fa fa-trash"></i>
        </button>
      </div>

    </div>
  );
}

export default Toolbar;