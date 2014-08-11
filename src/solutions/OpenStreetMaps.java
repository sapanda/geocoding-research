package solutions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class OpenStreetMaps implements Solution {

    @Override
    public String normalize(String address) {
        // TODO: Implement
        return "";
    }

    @Override
    public LatLong geocode(String address) {
        LatLong retVal = new LatLong(0, 0);

        // Make the query
        String url = "http://nominatim.openstreetmap.org/search";

        Reference ref = new Reference(url);
        ref.addQueryParameter("q", address);
        ref.addQueryParameter("format", "json");

        Representation rep = new ClientResource(ref).get();

        try {
            // Parse the Data
            JsonRepresentation jr = new JsonRepresentation(rep);
            JSONArray jarr = jr.getJsonArray();

            for (int i = 0; i < jarr.length(); ++i){
                JSONObject jobj = jarr.getJSONObject(i);
                retVal.latitude = jobj.getDouble("lat");
                retVal.longitude = jobj.getDouble("lon");

                // TODO: Deal with multiple return values
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    @Override
    public String reverseGeocode(LatLong latlong) {
        // TODO: Implement
        return "";
    }
}
