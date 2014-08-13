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
        String address = "";

        JSONArray jarr = jobj.getJSONArray("resourceSets");
        if (jarr.length() > 0) {
            jarr = jarr.getJSONObject(0).getJSONArray("resources");
            if (jarr.length() > 0) {
                address = jarr.getJSONObject(0)
                        .getJSONObject("address")
                        .getString("formattedAddress");
            }
        }

        return address;
    }
}
