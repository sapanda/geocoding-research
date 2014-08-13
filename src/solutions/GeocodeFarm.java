package solutions;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class GeocodeFarm implements Solution {

    private final String API_KEY = "40753122757d6897bb0ffd62a1f3686c9d61f92c";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        try {
            // Make the query
            String url = String.format("http://www.geocodefarm.com/api/forward/json/%s/%s",
                    API_KEY, URLEncoder.encode(address, "UTF-8"));

            String json = util.HttpUtils.httpGetJson(url);

            if (json.length() > 0) {
                // Parse the Data
                JSONObject jobj = new JSONObject(json).getJSONObject("geocoding_results");

                if (jobj.has("ADDRESS")) {
                    jobj = jobj.getJSONObject("ADDRESS");
                    normAddress = jobj.getString("address_returned");
                }
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

        try {
            // Make the query
            String url = String.format("http://www.geocodefarm.com/api/forward/json/%s/%s",
                    API_KEY, URLEncoder.encode(address, "UTF-8"));

            String json = util.HttpUtils.httpGetJson(url);

            if (json.length() > 0) {
                // Parse the Data
                JSONObject jobj = new JSONObject(json).getJSONObject("geocoding_results");
                if (jobj.has("COORDINATES")) {
                    jobj = jobj.getJSONObject("COORDINATES");
                    latlong.latitude = jobj.getDouble("latitude");
                    latlong.longitude = jobj.getDouble("longitude");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // Ignore since GeoCodeFarm throws 404 errors if the location isn't found
        }

        return latlong;
    }

    @Override
    public String reverseGeocode(LatLong latlong) {
        String address = "";

        try {
            // Make the query
            String url = String.format("http://www.geocodefarm.com/api/reverse/json/%s/%s/%s",
                    API_KEY, latlong.latitude, latlong.longitude);

            String json = util.HttpUtils.httpGetJson(url);

            if (json.length() > 0) {
                // Parse the Data
                JSONObject jobj = new JSONObject(json).getJSONObject("geocoding_results");

                if (jobj.has("ADDRESS")) {
                    jobj = jobj.getJSONObject("ADDRESS");
                    address = jobj.getString("address");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // Ignore since GeoCodeFarm throws 404 errors if the location isn't found
        }

        return address;
    }
}
