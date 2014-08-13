package solutions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmartyStreets implements Solution {
    // This is a free trial auth-id and auth-token
    // limited to 250 lookups per month after September 12, 2014
    private final String authId = "9110bb3d-0e00-4746-aa42-8cc58cd1f56d";
    private final String authToken = "s0rBT9aLR6MCPIEB9Cyh";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        try {
            // Make the query
            URI uri = new URIBuilder("https://api.smartystreets.com/street-address")
                .setParameter("street", address)
                .setParameter("auth-id", authId)
                .setParameter("auth-token", authToken)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONArray jarr = new JSONArray(json);

            // TODO: Deal with multiple return values
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0).getJSONObject("components");
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
            URI uri = new URIBuilder("https://api.smartystreets.com/street-address")
                .setParameter("street", address)
                .setParameter("auth-id", authId)
                .setParameter("auth-token", authToken)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONArray jarr = new JSONArray(json);

            // TODO: Deal with multiple return values
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0).getJSONObject(
                        "metadata");
                latlong.latitude = jobj.getDouble("latitude");
                latlong.longitude = jobj.getDouble("longitude");
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
        // doesn't do reverse geocoding
        return null;
    }

    private String parseAddress(JSONObject jobj) throws JSONException {
        String address = "";
        // for normalization
        if (jobj.has("primary_number")) {
            address += jobj.getString("primary_number") + " ";
        }
        if (jobj.has("street_predirection")) {
            address += jobj.getString("street_predirection") + " ";
        }
        if (jobj.has("street_name")) {
            address += jobj.getString("street_name") + " ";
        }
        if (jobj.has("street_suffix")) {
            address += jobj.getString("street_suffix") + ", ";
        }
        if (jobj.has("city_name")) {
            address += jobj.getString("city_name") + ", ";
        }
        if (jobj.has("state_abbreviation")) {
            address += jobj.getString("state_abbreviation") + ", ";
        }
        if (jobj.has("zipcode")) {
            address += jobj.getString("zipcode") + " ";
        }

        return address;
    }

}
