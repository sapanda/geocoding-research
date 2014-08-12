package solutions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

public class OpenStreetMaps extends Solution {

    @Override
    public String normalize(String address) {
        String normAddress = "";

        // Make the query
        String url = "http://nominatim.openstreetmap.org/search";

        Reference ref = new Reference(url);
        ref.addQueryParameter("q", address);
        ref.addQueryParameter("format", "json");
        ref.addQueryParameter("addressdetails", "1");

        Representation rep = getRepresentation(ref);

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            JSONArray jarr = jr.getJsonArray();

            // TODO: Deal with multiple return values
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0)
                        .getJSONObject("address");
                normAddress = parseAddress(jobj);
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
        String url = "http://nominatim.openstreetmap.org/search";

        Reference ref = new Reference(url);
        ref.addQueryParameter("q", address);
        ref.addQueryParameter("format", "json");
        ref.addQueryParameter("addressdetails", "1");

        Representation rep = getRepresentation(ref);

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            JSONArray jarr = jr.getJsonArray();

            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0);
                latlong.latitude = jobj.getDouble("lat");
                latlong.longitude = jobj.getDouble("lon");
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
        String url = "http://nominatim.openstreetmap.org/reverse";

        Reference ref = new Reference(url);
        ref.addQueryParameter("lat", String.valueOf(latlong.latitude));
        ref.addQueryParameter("lon", String.valueOf(latlong.longitude));
        ref.addQueryParameter("format", "json");
        ref.addQueryParameter("addressdetails", "1");

        Representation rep = getRepresentation(ref);

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            JSONObject jobj = jr.getJsonObject();
            if (jobj.has("address")) {
                address = parseAddress(jobj.getJSONObject("address"));
            }

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
