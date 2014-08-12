package solutions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class MapQuest implements Solution {

    private final String API_KEY = "Fmjtd%7Cluur25u2ll%2C75%3Do5-9w7ld0";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        // Make the query
        // Note: the API_KEY is placed in the url since it has some weird characters
        String url = "http://www.mapquestapi.com/geocoding/v1/address?key=" + API_KEY;

        Reference ref = new Reference(url);
        ref.addQueryParameter("location", address);

        Representation rep = new ClientResource(ref).get();

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
        // Note: the API_KEY is placed in the url since it has some weird characters
        String url = "http://www.mapquestapi.com/geocoding/v1/address?key=" + API_KEY;

        Reference ref = new Reference(url);
        ref.addQueryParameter("location", address);

        Representation rep = new ClientResource(ref).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);

            JSONArray jarr = jr.getJsonObject().getJSONArray("results");
            if (jarr.length() > 0) {
                jarr = jarr.getJSONObject(0).getJSONArray("locations");
                if (jarr.length() > 0) {
                    JSONObject jobj = jarr.getJSONObject(0).getJSONObject("latLng");
                    latlong.latitude = jobj.getDouble("lat");
                    latlong.longitude = jobj.getDouble("lng");
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
        // Note: the API_KEY is placed in the url since it has some weird characters
        String url = "http://www.mapquestapi.com/geocoding/v1/reverse?key=" + API_KEY;

        Reference ref = new Reference(url);
        ref.addQueryParameter("location", latlong.toString());

        Representation rep = new ClientResource(ref).get();

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
            jarr = jarr.getJSONObject(0).getJSONArray("locations");
            if (jarr.length() > 0) {
                jobj = jarr.getJSONObject(0);

                address = jobj.getString("street") + ", " +
                        jobj.getString("adminArea5") + ", " +
                        jobj.getString("adminArea3") + " " +
                        jobj.getString("postalCode");
            }
        }

        return address;
    }
}
