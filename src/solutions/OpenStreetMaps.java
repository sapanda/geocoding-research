package solutions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OpenStreetMaps implements Solution {

    @Override
    public String normalize(String address) {
        String normAddress = "";

        try {
            // Make the query
            URI uri = new URIBuilder("http://nominatim.openstreetmap.org/search")
                .setParameter("q", address)
                .setParameter("format", "json")
                .setParameter("addressdetails", "1")
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONArray jarr = new JSONArray(json);

            // TODO: Deal with multiple return values
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0)
                        .getJSONObject("address");
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
            URI uri = new URIBuilder("http://nominatim.openstreetmap.org/search")
                .setParameter("q", address)
                .setParameter("format", "json")
                .setParameter("addressdetails", "1")
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONArray jarr = new JSONArray(json);

            // TODO: Deal with multiple return values
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0);
                latlong.latitude = jobj.getDouble("lat");
                latlong.longitude = jobj.getDouble("lon");
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
            URI uri = new URIBuilder("http://nominatim.openstreetmap.org/reverse")
                .setParameter("q", address)
                .setParameter("format", "json")
                .setParameter("addressdetails", "1")
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONObject jobj = new JSONObject(json);

            if (jobj.has("address")) {
                address = parseAddress(jobj.getJSONObject("address"));
            }

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
            address += jobj.getString("state") + ", ";
        }
        if (jobj.has("postcode")) {
            address += jobj.getString("postcode");
        }

        return address;
    }
}
