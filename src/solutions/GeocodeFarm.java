package solutions;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class GeocodeFarm implements Solution {

    private final String API_KEY = "40753122757d6897bb0ffd62a1f3686c9d61f92c";

    public static void main(String[] args) {
        GeocodeFarm osm = new GeocodeFarm();
        String address = "130 Harvard Ave E, Seattle, WA 98122";
        LatLong latlong = new LatLong(47.6196523, -122.321789839922);

        System.out.println(osm.normalize(address));
        System.out.println(osm.geocode(address));
        System.out.println(osm.reverseGeocode(latlong));
    }

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
