package solutions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class GeocoderUS implements Solution {

    @Override
    public String normalize(String address) {
        String returnAddress ="";
        // TODO: Deal with the fact that it doesn't normalize, just parses
        // Standardizes to a very small degree as it returns all information it can find
        // Meaning it might return more information than you gave it
        // Only return the first response
        String url = "http://rpc.geocoder.us/service/json";

        Reference ref = new Reference(url);
        ref.addQueryParameter("address",address);
        System.out.println("url: "+ref);

        Representation rep = new ClientResource(ref).get();
        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            JSONArray jarr = jr.getJsonArray();

            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0);
                returnAddress = parseAddress(jobj);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnAddress;
    }

    @Override
    public LatLong geocode(String address) {
        //only returns first response
        LatLong latlong = new LatLong(0, 0);
        String url = "http://rpc.geocoder.us/service/json";

        Reference ref = new Reference(url);
        ref.addQueryParameter("address",address);
        System.out.println("url: "+ref);

        Representation rep = new ClientResource(ref).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            JSONArray jarr = jr.getJsonArray();

            if (jarr.length() > 0) {
                JSONObject jobj = jarr.getJSONObject(0);
                latlong.latitude = jobj.getDouble("lat");
                latlong.longitude = jobj.getDouble("long");
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
        //does not have reverse geocoding
        return null;
    }
    private String parseAddress(JSONObject jobj) throws JSONException {
        String address = "";

        if (jobj.has("number")) {
            address += jobj.getString("number") + " ";
        }
        if (jobj.has("street")) {
            address += jobj.getString("street") + ", ";
        }
        if (jobj.has("city")) {
            address += jobj.getString("city") + ", ";
        }
        if (jobj.has("state")) {
            address += jobj.getString("state") + ", ";
        }
        if (jobj.has("zip")) {
            address += jobj.getString("zip");
        }

        return address;
    }
}
