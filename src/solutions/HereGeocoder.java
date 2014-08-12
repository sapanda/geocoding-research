package solutions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class HereGeocoder implements Solution {

    private final String APP_ID = "0uONArqcbXgaxh7XCxry";
    private final String APP_CODE = "jwtLcQC4vjyOUUjEGnkkiQ";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        // Make the query
        String url = "http://geocoder.api.here.com/6.2/geocode.json";

        Reference ref = new Reference(url);
        ref.addQueryParameter("app_code", APP_CODE);
        ref.addQueryParameter("app_id", APP_ID);
        ref.addQueryParameter("gen", "5");
        ref.addQueryParameter("searchtext", address);

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
        String url = "http://geocoder.api.here.com/6.2/geocode.json";

        Reference ref = new Reference(url);
        ref.addQueryParameter("app_code", APP_CODE);
        ref.addQueryParameter("app_id", APP_ID);
        ref.addQueryParameter("gen", "5");
        ref.addQueryParameter("searchtext", address);

        Representation rep = new ClientResource(ref).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);

            JSONArray jarr = jr.getJsonObject().getJSONObject("Response").getJSONArray("View");
            if (jarr.length() > 0) {
                jarr = jarr.getJSONObject(0).getJSONArray("Result");
                if (jarr.length() > 0) {
                    jarr = jarr.getJSONObject(0).getJSONObject("Location").getJSONArray("NavigationPosition");
                    if (jarr.length() > 0) {
                        JSONObject jobj = jarr.getJSONObject(0);

                        latlong.latitude = jobj.getDouble("Latitude");
                        latlong.longitude = jobj.getDouble("Longitude");
                    }
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
        String url = "http://reverse.geocoder.api.here.com/6.2/reversegeocode.json";

        Reference ref = new Reference(url);
        ref.addQueryParameter("app_code", APP_CODE);
        ref.addQueryParameter("app_id", APP_ID);
        ref.addQueryParameter("gen", "5");
        ref.addQueryParameter("mode", "retrieveAddresses");
        ref.addQueryParameter("prox", latlong.toString());

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

        JSONArray jarr = jobj.getJSONObject("Response").getJSONArray("View");
        if (jarr.length() > 0) {
            jarr = jarr.getJSONObject(0).getJSONArray("Result");
            if (jarr.length() > 0) {
                address = jarr.getJSONObject(0)
                        .getJSONObject("Location")
                        .getJSONObject("Address")
                        .getString("Label");
            }
        }

        return address;
    }

}
