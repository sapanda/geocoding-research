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
    	//only returns the first response
    	String normAddress = "";

        // Make the query
        String url = "http://addressparser.appspot.com/webaddressparser?";

        Reference ref = new Reference(url);
        ref.addQueryParameter("address", address);
        ref.addQueryParameter("country", "US");
        ref.addQueryParameter("format", "json");
        System.out.println(ref);

        Representation rep = new ClientResource(ref).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            JSONArray jarr = jr.getJsonObject().getJSONArray("result");

            // TODO: Deal with multiple return values
            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0);
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
    	//only returns the first response
        String url = "http://services.gisgraphy.com//geocoding/geocode";

        Reference ref = new Reference(url);
        ref.addQueryParameter("address", address);
        ref.addQueryParameter("country", "US");
        ref.addQueryParameter("format", "json");
        System.out.println(ref);

        Representation rep = new ClientResource(ref).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            JSONArray jarr = jr.getJsonObject().getJSONArray("result");

            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0);
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
    	//only returns the first response
        String url = "http://services.gisgraphy.com/street/streetsearch?";

        Reference ref = new Reference(url);
        ref.addQueryParameter("lat", String.valueOf(latlong.latitude));
        ref.addQueryParameter("lng", String.valueOf(latlong.longitude));
        ref.addQueryParameter("from","1");
        ref.addQueryParameter("to","1");
        ref.addQueryParameter("format", "json");
        System.out.println(ref);

        Representation rep = new ClientResource(ref).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);

            JSONArray jarr = jr.getJsonObject().getJSONArray("result");
            JSONObject jobj = jarr.getJSONObject(0);
            address = parseAddress(jobj);
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        //The above doesn't return anything useful, just really vague info
        //like that it's a "residential" street or in the "Southern Chicago Heights"
        //So I won't actually use the data I get from it

        return null;
    }
    private String parseAddress(JSONObject jobj) throws JSONException {
        String address = "";

        if (jobj.has("houseNumber")) {
            address += jobj.getString("houseNumber") + " ";
        }
        if (jobj.has("streetName")) {
            address += jobj.getString("streetName") + " ";
        }
        if (jobj.has("streetType")) {
            address += jobj.getString("streetType") + ", ";
        }
        if (jobj.has("city")) {
            address += jobj.getString("city") + ", ";
        }
        //has no state
        if (jobj.has("state")) {
            address += jobj.getString("state") + ", ";
        }
        if (jobj.has("zipCode")) {
            address += jobj.getString("zipCode");
        }

        return address;
    }
}
