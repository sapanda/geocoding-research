package solutions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapQuest implements Solution {

    private final String API_KEY = "Fmjtd%7Cluur25u2ll%2C75%3Do5-9w7ld0";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        try {
            // Make the query
            // Note: the API_KEY is placed in the url since it has some weird characters
            String url = "http://www.mapquestapi.com/geocoding/v1/address?key=" + API_KEY;
            URI uri = new URIBuilder(url)
                .setParameter("location", address)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            normAddress = parseAddress(new JSONObject(json));

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
            // Note: the API_KEY is placed in the url since it has some weird characters
            String url = "http://www.mapquestapi.com/geocoding/v1/address?key=" + API_KEY;
            URI uri = new URIBuilder(url)
                .setParameter("location", address)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONArray jarr = new JSONObject(json).getJSONArray("results");
            if (jarr.length() > 0) {
                jarr = jarr.getJSONObject(0).getJSONArray("locations");
                if (jarr.length() > 0) {
                    JSONObject jobj = jarr.getJSONObject(0).getJSONObject("latLng");
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
            // Note: the API_KEY is placed in the url since it has some weird characters
            String url = "http://www.mapquestapi.com/geocoding/v1/reverse?key=" + API_KEY;
            URI uri = new URIBuilder(url)
                .setParameter("location", latlong.toString())
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            address = parseAddress(new JSONObject(json));

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return address;
    }

    private String parseAddress(JSONObject jobj) throws JSONException {
        Address address = new Address();

        JSONArray jarr = jobj.getJSONArray("results");
        if (jarr.length() > 0) {
            jarr = jarr.getJSONObject(0).getJSONArray("locations");
            if (jarr.length() > 0) {
                jobj = jarr.getJSONObject(0);

                if (jobj.has("street")) {
                    address.street = jobj.getString("street");
                }
                if (jobj.has("adminArea6")) {
                    address.district = jobj.getString("adminArea6");
                }
                if (jobj.has("adminArea5")) {
                    address.city = jobj.getString("adminArea5");
                }
                if (jobj.has("adminArea4")) {
                    address.county = jobj.getString("adminArea4");
                }
                if (jobj.has("adminArea3")) {
                    address.state = jobj.getString("adminArea3");
                }
                if (jobj.has("postalCode")) {
                    address.postalCode = jobj.getString("postalCode");
                }
                if (jobj.has("adminArea1")) {
                    address.country = jobj.getString("adminArea1");
                }
            }
        }

        return address.toString();
    }
}
