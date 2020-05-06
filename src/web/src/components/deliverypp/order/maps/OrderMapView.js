
import React from 'react';

import { Map, Marker, GoogleApiWrapper } from 'google-maps-react';

function OrderMapView({ google, location }) {
    return (
      <div style={{height: 400, padding: 0, position: 'relative'}}>
        <Map
          google={google}
          zoom={14}
          initialCenter={{
            lat: location.latitude,
            lng: location.longitude
          }}>
          <Marker name={'Current location'} />
        </Map>
      </div>
    );
}
  
const WrappedOrderMapView = GoogleApiWrapper({
apiKey: ('AIzaSyC1wC2ImcL1s4wITbDbM-g3nLK01fJHGkc')
})(OrderMapView);

export default WrappedOrderMapView;