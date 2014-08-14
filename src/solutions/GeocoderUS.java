package solutions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeocoderUS implements Solution {

    @Override
    public String normalize(String address) {
        String returnAddress ="";

        try {
            // Make the query
            URI uri = new URIBuilder("http://rpc.geocoder.us/service/json")
                .setParameter("address", address)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            if (json.length() > 0) {
                JSONArray jarr = new JSONArray(json);

                // Only return the first response
                if (jarr.length() > 0) {
                    JSONObject jobj = jarr.getJSONObject(0);
                    returnAddress = parseAddress(jobj);
                }
            }

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnAddress;
    }

    @Override
    public LatLong geocode(String address) {
        LatLong latlong = new LatLong(0, 0);

        try {
            // Make the query
            URI uri = new URIBuilder("http://rpc.geocoder.us/service/json")
                .setParameter("address", address)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            if (json.length() > 0) {
                JSONArray jarr = new JSONArray(json);

                // Only return the first response
                if (jarr.length() > 0) {
                    JSONObject jobj = jarr.getJSONObject(0);
                    latlong.latitude = jobj.getDouble("lat");
                    latlong.longitude = jobj.getDouble("long");
                }
            }

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
        //does not have reverse geocoding
        return null;
    }

    private String parseAddress(JSONObject jobj) throws JSONException {
        String address = "";

        if (jobj.has("number")) {
            address += jobj.getString("number") + " ";
        }
        if (jobj.has("prefix")) {
            address += jobj.getString("prefix") + " ";
        }
        if (jobj.has("street")) {
            address += jobj.getString("street") + ", ";
        }
        if (jobj.has("city")) {
            address += jobj.getString("city") + ", ";
        }
        if (jobj.has("state")) {
            address += jobj.getString("state") + ", ";
        }
        if (jobj.has("zip")) {
            address += jobj.getString("zip");
        }

        return address;
    }
}
