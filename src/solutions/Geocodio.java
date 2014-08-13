package solutions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Geocodio implements Solution {

    private final String API_KEY = "9faaf3334205120e5951a52bc96505ef5b0eb55";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        try {
            // Make the query
            URI uri = new URIBuilder("https://api.geocod.io/v1/geocode")
                .setParameter("q", address)
                .setParameter("api_key", API_KEY)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            if (json.length() > 0) {
                JSONArray jarr = new JSONObject(json).getJSONArray("results");

                if (jarr.length() > 0) {
                    JSONObject jobj = jarr.getJSONObject(0);
                    normAddress = jobj.getString("formatted_address");
                }
            }

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
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

        try {
            // Make the query
            URI uri = new URIBuilder("https://api.geocod.io/v1/geocode")
                .setParameter("q", address)
                .setParameter("api_key", API_KEY)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            if (json.length() > 0) {
                JSONArray jarr = new JSONObject(json).getJSONArray("results");
                if (jarr.length() > 0) {
                    JSONObject jobj = jarr.getJSONObject(0).getJSONObject(
                            "location");

                    latlong.latitude = jobj.getDouble("lat");
                    latlong.longitude = jobj.getDouble("lng");
                }
            }

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
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

        try {
            // Make the query
            URI uri = new URIBuilder("https://api.geocod.io/v1/reverse")
                .setParameter("q", latlong.toString())
                .setParameter("api_key", API_KEY)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            if (json.length() > 0) {
                JSONArray jarr = new JSONObject(json).getJSONArray("results");

                if (jarr.length() > 0) {
                    JSONObject jobj = jarr.getJSONObject(0);
                    address = jobj.getString("formatted_address");
                }
            }

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return address;
    }
}
