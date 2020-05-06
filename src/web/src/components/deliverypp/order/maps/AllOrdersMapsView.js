
import React from 'react';

import {Map, Marker, GoogleApiWrapper} from 'google-maps-react';

function AllOrdersMapsView({ google, locations, onMarkerClick = () => {} }) {
    return (
      <div style={{height: 400, padding: 0, position: 'relative'}}>
        <Map
          google={google}
          zoom={11}
          initialCenter={{
            lat: 18.486057,
            lng: -69.931213
          }}
        >
          {
            locations.map(location => (
              <Marker
                key={location.id}
                title={'The marker`s title will appear as a tooltip.'}
                name={'SOMA'}
                position={{lat: location.latitude, lng: location.longitude}}
                onClick={() => onMarkerClick(location.id)}
              />
            ))
          }
        </Map>
      </div>
    )  
}
  
const WrappedAllOrdersMapsView = GoogleApiWrapper({
apiKey: ('AIzaSyC1wC2ImcL1s4wITbDbM-g3nLK01fJHGkc')
})(AllOrdersMapsView);

export default WrappedAllOrdersMapsView;