package solutions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class Geocodio implements Solution {

    private final String API_KEY = "9faaf3334205120e5951a52bc96505ef5b0eb55";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        // Make the query
        String url = "https://api.geocod.io/v1/geocode";

        Reference ref = new Reference(url);
        ref.addQueryParameter("q", address);
        ref.addQueryParameter("api_key", API_KEY);

        Representation rep = new ClientResource(ref).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);

            // Highest accuracy always at the head of the list
            JSONArray jarr = jr.getJsonObject().getJSONArray("results");
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0);
                normAddress = jobj.getString("formatted_address");
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
        String url = "https://api.geocod.io/v1/geocode";

        Reference ref = new Reference(url);
        ref.addQueryParameter("q", address);
        ref.addQueryParameter("api_key", API_KEY);

        Representation rep = new ClientResource(ref).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);

            // Highest accuracy always at the head of the list
            JSONArray jarr = jr.getJsonObject().getJSONArray("results");
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0).getJSONObject("location");

                latlong.latitude = jobj.getDouble("lat");
                latlong.longitude = jobj.getDouble("lng");
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
        String url = "https://api.geocod.io/v1/reverse";

        Reference ref = new Reference(url);
        ref.addQueryParameter("q", latlong.toString());
        ref.addQueryParameter("api_key", API_KEY);

        Representation rep = new ClientResource(ref).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);

            // Highest accuracy always at the head of the list
            JSONArray jarr = jr.getJsonObject().getJSONArray("results");
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0);
                address = jobj.getString("formatted_address");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return address;
    }
}
