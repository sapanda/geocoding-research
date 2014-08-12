package solutions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class Gisgraphy implements Solution {

    @Override
    public String normalize(String address) {
    	//Again,  just parser, not really normalizer
    	String normAddress = "";

        // Make the query
        String url = "http://addressparser.appspot.com/webaddressparser?";

        Reference ref = new Reference(url);
        ref.addQueryParameter("address", address);
        ref.addQueryParameter("country", "US");
        ref.addQueryParameter("format", "json");

        Representation rep = new ClientResource(ref).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            JSONArray jarr = jr.getJsonArray();

            // TODO: Deal with multiple return values
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0).getJSONObject("address");
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
        String url = "http://services.gisgraphy.com//geocoding/geocode?";

        Reference ref = new Reference(url);
        ref.addQueryParameter("address", address);
        ref.addQueryParameter("country", "US");
        ref.addQueryParameter("format", "json");
        System.out.println(ref);

        Representation rep = new ClientResource(ref).get();

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
        String url = "http://services.gisgraphy.com/street/streetsearch?";

        Reference ref = new Reference(url);
        ref.addQueryParameter("lat", String.valueOf(latlong.latitude));
        ref.addQueryParameter("lng", String.valueOf(latlong.longitude));
        ref.addQueryParameter("from","1");
        ref.addQueryParameter("to","1");
        ref.addQueryParameter("format", "json");

        Representation rep = new ClientResource(ref).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);

            JSONObject jobj = jr.getJsonObject().getJSONObject("address");
            address = parseAddress(jobj);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return address;
    }
    private String parseAddress(JSONObject jobj) throws JSONException {
        String address = "";

        if (jobj.has("houseNumber")) {
            address += jobj.getString("houseNumber") + " ";
        }
        if (jobj.has("streetName")) {
            address += jobj.getString("streetName") + ", ";
        }
        if (jobj.has("city")) {
            address += jobj.getString("city") + ", ";
        }
        if (jobj.has("state")) {
            address += jobj.getString("state") + ", ";
        }
        if (jobj.has("zipcode")) {
            address += jobj.getString("zipcode");
        }

        return address;
    }
}
