import React from 'react';

import { Alert } from 'reactstrap';

function DAlert({ show, message, color }) {
    return (
        <div className="alertContainer">
            <Alert color={color} isOpen={show}>
                { message }
            </Alert>
      </div>  
    )
}

export default DAlert;