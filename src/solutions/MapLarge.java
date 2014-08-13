package solutions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class MapLarge implements Solution {
    // 14 day api key
    private final String API_KEY = "geo31847";

    @Override
    public String normalize(String address) {
        // can only do this if we first parse the address
        return null;
    }

    @Override
    public LatLong geocode(String address) {
        LatLong latlong = new LatLong(0, 0);

        try {
            // Make the query
            URI uri = new URIBuilder("http://geocoder.maplarge.com/geocoder/json")
                .setParameter("address", address)
                .setParameter("key", API_KEY)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONObject jobj = new JSONObject(json);
            latlong.latitude = jobj.getDouble("lat");
            latlong.longitude = jobj.getDouble("lng");

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return latlong;
    }

    @Override
    public String reverseGeocode(LatLong latlong) {
        return null;
    }

}
