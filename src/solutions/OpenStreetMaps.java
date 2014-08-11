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

    public static void main(String[] args) {
        OpenStreetMaps osm = new OpenStreetMaps();
        String address = "130 Harvard Ave E, Seattle, WA 98122";
        LatLong latlong = new LatLong(47.6196523, -122.321789839922);

        System.out.println(osm.normalize(address));
        System.out.println(osm.geocode(address));
        System.out.println(osm.reverseGeocode(latlong));
    }

    @Override
    public String normalize(String address) {
        String normAddress = "";

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

    private String parseAddress(JSONObject jobj) {
        String address = "";

        try {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return address;
    }
}
