package solutions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Gisgraphy implements Solution {

    @Override
    public String normalize(String address) {
        String normAddress = "";

        try {
            // Make the query
            URI uri = new URIBuilder("http://addressparser.appspot.com/webaddressparser")
                .setParameter("address", address)
                .setParameter("country", "US")
                .setParameter("format", "json")
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONArray jarr = new JSONObject(json).getJSONArray("result");

            // TODO: Deal with multiple return values
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0);
                normAddress = parseAddress(jobj);
            }

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return normAddress;
    }

    @Override
    public LatLong geocode(String address) {
        LatLong latlong = new LatLong(0, 0);

        try {
            // Make the query
            URI uri = new URIBuilder("http://services.gisgraphy.com/geocoding/geocode")
                .setParameter("address", address)
                .setParameter("country", "US")
                .setParameter("format", "json")
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONArray jarr = new JSONObject(json).getJSONArray("result");

            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0);
                latlong.latitude = jobj.getDouble("lat");
                latlong.longitude = jobj.getDouble("lng");
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
        String address = "";

        try {
            // Make the query
            URI uri = new URIBuilder("http://services.gisgraphy.com/street/streetsearch")
                .setParameter("lat", String.valueOf(latlong.latitude))
                .setParameter("lng", String.valueOf(latlong.longitude))
                .setParameter("from","1")
                .setParameter("to","1")
                .setParameter("format", "json")
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONArray jarr = new JSONObject(json).getJSONArray("result");
            JSONObject jobj = jarr.getJSONObject(0);
            address = parseAddress(jobj);

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // The above doesn't return anything useful, just really vague info
        // like that it's a "residential" street or in the "Southern Chicago Heights"
        // So I won't actually use the data I get from it

        return null;
    }

    private String parseAddress(JSONObject jobj) throws JSONException {
        String address = "";

        if (jobj.has("houseNumber")) {
            address += jobj.getString("houseNumber") + " ";
        }
        if (jobj.has("streetName")) {
            address += jobj.getString("streetName") + " ";
        }
        if (jobj.has("streetType")) {
            address += jobj.getString("streetType") + ", ";
        }
        if (jobj.has("city")) {
            address += jobj.getString("city") + ", ";
        }
        //has no state
        if (jobj.has("state")) {
            address += jobj.getString("state") + ", ";
        }
        if (jobj.has("zipCode")) {
            address += jobj.getString("zipCode");
        }

        return address;
    }
}
