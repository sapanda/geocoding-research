package solutions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

public class OpenCageGeocoder extends Solution {

    private final String API_KEY = "a50ffda7c1baf4681044f52d95c30ef7";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        // Make the query
        String url = "http://api.opencagedata.com/geocode/v1/json";

        Reference ref = new Reference(url);
        ref.addQueryParameter("q", address);
        ref.addQueryParameter("key", API_KEY);

        Representation rep = getRepresentation(ref);

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            normAddress = parseAddress(jr.getJsonObject());

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
        String url = "http://api.opencagedata.com/geocode/v1/json";

        Reference ref = new Reference(url);
        ref.addQueryParameter("q", address);
        ref.addQueryParameter("key", API_KEY);

        Representation rep = getRepresentation(ref);

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);

            JSONArray jarr = jr.getJsonObject().getJSONArray("results");
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0)
                        .getJSONObject("geometry");
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
        String url = "http://api.opencagedata.com/geocode/v1/json";

        Reference ref = new Reference(url);
        ref.addQueryParameter("q", latlong.toString());
        ref.addQueryParameter("key", API_KEY);

        Representation rep = getRepresentation(ref);

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            address = parseAddress(jr.getJsonObject());

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
