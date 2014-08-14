package solutions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BingMaps implements Solution {

    private final String API_KEY = "Aq6kRrGRShf5y_mBjd4KGSiwO9sS2y3MpmYQGohA2zRHNdl9ZKkM1jtAuPNjZbu6";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        try {
            // Make the query
            URI uri = new URIBuilder("http://dev.virtualearth.net/REST/v1/Locations")
                .setParameter("q", address)
                .setParameter("key", API_KEY)
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
            URI uri = new URIBuilder("http://dev.virtualearth.net/REST/v1/Locations")
                .setParameter("q", address)
                .setParameter("key", API_KEY)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONArray jarr = new JSONObject(json).getJSONArray("resourceSets");
            if (jarr.length() > 0) {
                jarr = jarr.getJSONObject(0).getJSONArray("resources");
                if (jarr.length() > 0) {
                    jarr = jarr.getJSONObject(0)
                            .getJSONObject("point")
                            .getJSONArray("coordinates");
                    latlong.latitude = jarr.getDouble(0);
                    latlong.longitude = jarr.getDouble(1);
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
            String url = "http://dev.virtualearth.net/REST/v1/Locations/" + latlong.toString();
            URI uri = new URIBuilder(url)
                .setParameter("key", API_KEY)
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

        JSONArray jarr = jobj.getJSONArray("resourceSets");
        if (jarr.length() > 0) {
            jarr = jarr.getJSONObject(0).getJSONArray("resources");
            if (jarr.length() > 0) {
                jobj = jarr.getJSONObject(0).getJSONObject("address");

                if (jobj.has("addressLine")) {
                    address.street = jobj.getString("addressLine");
                }
                if (jobj.has("locality")) {
                    address.city = jobj.getString("locality");
                }
                if (jobj.has("adminDistrict2")) {
                    address.county = jobj.getString("adminDistrict2");
                }
                if (jobj.has("adminDistrict")) {
                    address.state = jobj.getString("adminDistrict");
                }
                if (jobj.has("postalCode")) {
                    address.postalCode = jobj.getString("postalCode");
                }
                if (jobj.has("countryRegion")) {
                    address.country = jobj.getString("countryRegion");
                }
            }
        }

        return address.toString();
    }
}
