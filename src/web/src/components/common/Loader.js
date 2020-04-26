import React from 'react';

import { Spinner } from 'reactstrap';

import './Loader.css';

function Loader(props) {
    return (
        <div className="Loader">
            <Spinner color="light"  style={{ width: '4rem', height: '4rem' }} />
            <span style={{color: '#fff'}}> Espere...</span>
        </div>
    );
}

export default Loader;