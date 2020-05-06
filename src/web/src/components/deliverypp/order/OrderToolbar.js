

import React from 'react';

import StatusSelect from './StatusSelect';

function OrderToolbar({ onSearch, orderStatus, onOrderStatusChange }) {
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
                  onChange={onSearch}
                  type="text"
                  className="form-control"
                  placeholder="Buscar"
                  aria-describedby="inputGroupPrepend2"
                  required
              />
              <div className="ml-2">
                <StatusSelect orderStatus={orderStatus} onOrderStatusChange={onOrderStatusChange} />
              </div>
            </div>
        </div>
      </div>
    );
}

export default OrderToolbar;