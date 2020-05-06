

import React from 'react';

import { Label, Input } from 'reactstrap';

import StatusClassMapper from '../../../util/StatusClassMapper';

function StatusSelect({ orderStatus, onOrderStatusChange }) {
    return (
      <div className="d-flex flex-row justify-content-center align-items-cente mr-2">
        <Label className="mr-2" htmlFor="statusSelect">Estado</Label>
        <Input style={{borderWidth: 2}} className={`border-${StatusClassMapper.getClassStatusClass(orderStatus)}`} type="select" id="statusSelect" value={orderStatus} onChange={onOrderStatusChange}>
          <option value={''}></option>
          <option value={'ORDERED'}>Ordenado</option>
          <option value={'PAID'}>Pagado</option>
          <option value={'ACQUIRING'}>Adquiriendo</option>
          <option value={'ACQUIRED'}>Adquirido</option>
          <option value={'TRANSIT'}>En Tránsito</option>
          <option value={'DELIVERED'}>Entregado</option>
        </Input>
      </div>
    )
  }

  export default StatusSelect;