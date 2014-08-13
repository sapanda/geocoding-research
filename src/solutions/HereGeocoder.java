package solutions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HereGeocoder implements Solution {

    private final String APP_ID = "0uONArqcbXgaxh7XCxry";
    private final String APP_CODE = "jwtLcQC4vjyOUUjEGnkkiQ";

    @Override
    public String normalize(String address) {
        String normAddress = "";

        try {
            // Make the query
            URI uri = new URIBuilder("http://geocoder.api.here.com/6.2/geocode.json")
                .setParameter("app_code", APP_CODE)
                .setParameter("app_id", APP_ID)
                .setParameter("gen", "5")
                .setParameter("searchtext", address)
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
            URI uri = new URIBuilder("http://geocoder.api.here.com/6.2/geocode.json")
                .setParameter("app_code", APP_CODE)
                .setParameter("app_id", APP_ID)
                .setParameter("gen", "5")
                .setParameter("searchtext", address)
                .build();

            String json = util.HttpUtils.httpGetJson(uri);

            // Parse the Data
            JSONArray jarr = new JSONObject(json).getJSONObject("Response").getJSONArray("View");
            if (jarr.length() > 0) {
                jarr = jarr.getJSONObject(0).getJSONArray("Result");
                if (jarr.length() > 0) {
                    jarr = jarr.getJSONObject(0)
                            .getJSONObject("Location")
                            .getJSONArray("NavigationPosition");

                    if (jarr.length() > 0) {
                        JSONObject jobj = jarr.getJSONObject(0);

                        latlong.latitude = jobj.getDouble("Latitude");
                        latlong.longitude = jobj.getDouble("Longitude");
                    }
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
            URI uri = new URIBuilder("http://reverse.geocoder.api.here.com/6.2/reversegeocode.json")
                .setParameter("app_code", APP_CODE)
                .setParameter("app_id", APP_ID)
                .setParameter("gen", "5")
                .setParameter("mode", "retrieveAddresses")
                .setParameter("prox", latlong.toString())
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

        JSONArray jarr = jobj.getJSONObject("Response").getJSONArray("View");
        if (jarr.length() > 0) {
            jarr = jarr.getJSONObject(0).getJSONArray("Result");
            if (jarr.length() > 0) {
                address = jarr.getJSONObject(0)
                        .getJSONObject("Location")
                        .getJSONObject("Address")
                        .getString("Label");
            }
        }

        return address;
    }

}
