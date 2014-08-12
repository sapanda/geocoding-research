package solutions;

import java.io.IOException;
import java.util.ArrayList;

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
        // TODO: Deal with the fact that it doesn't normalize
    	//This just parses
    	//Standardizes to a very small degree as it returns all information it can find
    	//Meaning it might return more information than you gave it
    	 String url = "http://rpc.geocoder.us/service/json?";

         Reference ref = new Reference(url);
         ref.addQueryParameter("address",address);
         System.out.println("url: "+ref);
        
         Representation rep = new ClientResource(ref).get();
         try {
             // Parse the Data
             JsonRepresentation jr = new JsonRepresentation(rep);
             JSONArray jarr = jr.getJsonArray();

             // TODO: Deal with multiple return values
             if (jarr.length() > 0) {
                 JSONObject jobj = jarr.getJSONObject(0).getJSONObject("address");
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
    	LatLong latlong = new LatLong(0, 0);
        String url = "http://rpc.geocoder.us/service/json?";

        Reference ref = new Reference(url);
        ref.addQueryParameter("address",address);
        System.out.println("url: "+ref);

//      ClientResource resource = new ClientResource(ref);
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
//        String[] parts;
//        try{
//        	String repS = rep.getText();
//        	repS = repS.replaceAll("[\\t\\n\\r]",",");
//        	parts = repS.split(",");
//            
//        }
//        
//        catch(Exception e){
//        	//Error parsing the latitude and longitude
//        	return retVal;
//        }
//       
//        for(int i = 0,j=0; i < parts.length-1;i+=6,j++){
//        	double lat = Double.parseDouble(parts[i]);
//            double longitude = Double.parseDouble(parts[i+1]);
//
//            LatLong tempRet = new LatLong(lat, longitude);
//            retVal.add(j,tempRet);
//            
//        }
 

        return latlong;
    }

    @Override
    public String reverseGeocode(LatLong latlong) {
    	//TODO deal with the fact that it cannot reverse Geocode
    	return "";
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
        if (jobj.has("postcode")) {
            address += jobj.getString("postcode");
        }

        return address;
    }
}
