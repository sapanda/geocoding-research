package solutions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

public class BingMaps extends Solution {

    private final String API_KEY = "Aq6kRrGRShf5y_mBjd4KGSiwO9sS2y3MpmYQGohA2zRHNdl9ZKkM1jtAuPNjZbu6";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        // Make the query
        String url = "http://dev.virtualearth.net/REST/v1/Locations";

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
        String url = "http://dev.virtualearth.net/REST/v1/Locations";

        Reference ref = new Reference(url);
        ref.addQueryParameter("q", address);
        ref.addQueryParameter("key", API_KEY);

        Representation rep = getRepresentation(ref);

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);

            JSONArray jarr = jr.getJsonObject().getJSONArray("resourceSets");
            if (jarr.length() > 0) {
                jarr = jarr.getJSONObject(0).getJSONArray("resources");
                if (jarr.length() > 0) {
                    jarr = jarr.getJSONObject(0)
                            .getJSONObject("point")
                            .getJSONArray("coordinates");
                    latlong.latitude = jarr.getDouble(0);
                    latlong.longitude = jarr.getDouble(1);
                }
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
        String url = "http://dev.virtualearth.net/REST/v1/Locations/" + latlong.toString();

        Reference ref = new Reference(url);
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

        JSONArray jarr = jobj.getJSONArray("resourceSets");
        if (jarr.length() > 0) {
            jarr = jarr.getJSONObject(0).getJSONArray("resources");
            if (jarr.length() > 0) {
                address = jarr.getJSONObject(0)
                        .getJSONObject("address")
                        .getString("formattedAddress");
            }
        }

        return address;
    }
}
