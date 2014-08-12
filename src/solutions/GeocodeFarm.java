package solutions;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class GeocodeFarm implements Solution {

    private final String API_KEY = "40753122757d6897bb0ffd62a1f3686c9d61f92c";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        // Make the query
        String url = String.format("http://www.geocodefarm.com/api/forward/json/%s/%s",
                API_KEY, address);

        Representation rep = new ClientResource(url).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            JSONObject jobj = jr.getJsonObject().getJSONObject("geocoding_results");

            if (jobj.has("ADDRESS")) {
                jobj = jobj.getJSONObject("ADDRESS");
                normAddress = jobj.getString("address_returned");
            }
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

        // Make the query
        String url = String.format("http://www.geocodefarm.com/api/forward/json/%s/%s",
                API_KEY, address);

        Representation rep = new ClientResource(url).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            JSONObject jobj = jr.getJsonObject().getJSONObject("geocoding_results");

            if (jobj.has("COORDINATES")) {
                jobj = jobj.getJSONObject("COORDINATES");
                latlong.latitude = jobj.getDouble("latitude");
                latlong.longitude = jobj.getDouble("longitude");
            }
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

        // Make the query
        String url = String.format("http://www.geocodefarm.com/api/reverse/json/%s/%s/%s",
                API_KEY, latlong.latitude, latlong.longitude);

        Representation rep = new ClientResource(url).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            JSONObject jobj = jr.getJsonObject().getJSONObject("geocoding_results");

            if (jobj.has("ADDRESS")) {
                jobj = jobj.getJSONObject("ADDRESS");
                address = jobj.getString("address");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return address;
    }
}