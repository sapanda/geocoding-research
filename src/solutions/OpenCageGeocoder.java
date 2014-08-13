package solutions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OpenCageGeocoder implements Solution {

    private final String API_KEY = "a50ffda7c1baf4681044f52d95c30ef7";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        try {
            // Make the query
            URI uri = new URIBuilder("http://api.opencagedata.com/geocode/v1/json")
                .setParameter("q", address)
                .setParameter("key", API_KEY)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            normAddress = parseAddress(new JSONObject(json));

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
            URI uri = new URIBuilder("http://api.opencagedata.com/geocode/v1/json")
                .setParameter("q", address)
                .setParameter("key", API_KEY)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONArray jarr = new JSONObject(json).getJSONArray("results");
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0)
                        .getJSONObject("geometry");
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
            URI uri = new URIBuilder("http://api.opencagedata.com/geocode/v1/json")
                .setParameter("q", latlong.toString())
                .setParameter("key", API_KEY)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            address = parseAddress(new JSONObject(json));

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return address;
    }

    private String parseAddress(JSONObject jobj) throws JSONException {
        String address = "";

        JSONArray jarr = jobj.getJSONArray("results");
        if (jarr.length() > 0) {
            jobj = jarr.getJSONObject(0).getJSONObject("components");

            if (jobj.has("house_number")) {
                address += jobj.getString("house_number") + " ";
            }
            if (jobj.has("road")) {
                address += jobj.getString("road") + ", ";
            }
            if (jobj.has("city")) {
                address += jobj.getString("city") + ", ";
            }
            if (jobj.has("state")) {
                address += jobj.getString("state") + " ";
            }
            if (jobj.has("postcode")) {
                address += jobj.getString("postcode");
            }

            if (address.length() == 0) {
                address = jarr.getJSONObject(0).getString("formatted");
            }
        }

        return address;
    }
}
