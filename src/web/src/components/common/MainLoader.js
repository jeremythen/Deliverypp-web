import React from 'react';

import { Spinner } from 'reactstrap';

import './Loader.css';

function MainLoader(props) {
    return (
        <div className="Loader">
            <Spinner type="grow" color="dark"  style={{ width: '4rem', height: '4rem' }} />
            <span style={{color: '#fff'}}> Espere...</span>
        </div>
    );
}

export default MainLoader;