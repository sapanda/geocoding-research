package solutions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class SmartyStreets implements Solution{
	//these is a free trial auth-id and auth-token
	//limited to 250 lookups per month after September 12, 2014
private String authId = "9110bb3d-0e00-4746-aa42-8cc58cd1f56d";
private String authToken = "s0rBT9aLR6MCPIEB9Cyh";
	
	 @Override
	 public String normalize(String address) {
		 String normAddress = "";

	        // Make the query
	        String url = "https://api.smartystreets.com/street-address";

	        Reference ref = new Reference(url);
	        ref.addQueryParameter("street", address);
	        ref.addQueryParameter("auth-id", authId);
	        ref.addQueryParameter("auth-token", authToken);
	        System.out.println(ref);

	        Representation rep = new ClientResource(ref).get();

	        try {
	            // Parse the Data
	            JsonRepresentation jr = new JsonRepresentation(rep);
	            JSONArray jarr = jr.getJsonArray();

	            // TODO: Deal with multiple return values
	            if (jarr.length() > 0) {
	                JSONObject jobj = jarr.getJSONObject(0).getJSONObject("components");
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
	        String url = "http://nominatim.openstreetmap.org/reverse";

	        Reference ref = new Reference(url);
	        ref.addQueryParameter("lat", String.valueOf(latlong.latitude));
	        ref.addQueryParameter("lon", String.valueOf(latlong.longitude));
	        ref.addQueryParameter("format", "json");
	        ref.addQueryParameter("addressdetails", "1");

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
	        //for normalization
	        if (jobj.has("primary_number")){
	        	address+=jobj.getString("primary_number")+" ";
	        }
	        if (jobj.has("street_predirection")) {
	            address += jobj.getString("street_predirection") + " ";
	        }
	        if (jobj.has("street_name")) {
	            address += jobj.getString("street_name") + " ";
	        }
	        if (jobj.has("street_suffix")) {
	            address += jobj.getString("street_suffix") + ", ";
	        }
	        if (jobj.has("city_name")) {
	            address += jobj.getString("city_name") + ", ";
	        }
	        if (jobj.has("state_abbreviation")) {
	            address += jobj.getString("state_abbreviation") + ", ";
	        }
	        if (jobj.has("zipcode")) {
	            address += jobj.getString("zipcode") + " ";
	        }
	        
	        //for reverse geocoding
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
