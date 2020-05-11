import React from 'react';

function CategoryToolbar( { onEditClick, onAddClick, onDeleteClick } ) {
    return (
        <div className="actionButtonsContainer p-1">
            <button onClick={onEditClick} disabled={true} className="btn btn-secondary mr-1" title="Editar">
            <i className="fa fa-pencil"></i>
            </button>

            <button onClick={onAddClick} disabled={true} className="btn btn-info mr-1" title="Agregar">
            <i className="fa fa-plus"></i>
            </button>

            <button onClick={onDeleteClick} disabled={true} className="btn btn-danger" title="Borrar">
            <i className="fa fa-trash"></i>
            </button>
      </div>
    );
}

export default CategoryToolbar;