package solutions;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

public class MapLarge extends Solution {
	// 14 day api key
	private final String apiKey = "geo31847";

	@Override
	public String normalize(String address) {
		// can only do this if we first parse the address
		return null;
	}

	@Override
	public LatLong geocode(String address) {
		LatLong latlong = new LatLong(0, 0);

		// Make the query
		String url = "http://geocoder.maplarge.com/geocoder/json?";

		Reference ref = new Reference(url);
		ref.addQueryParameter("address", address);
		ref.addQueryParameter("key", apiKey);
		System.out.println(ref);
		Representation rep = getRepresentation(ref);

		try {
			// Parse the Data
			JsonRepresentation jr = new JsonRepresentation(rep);
			JSONObject jobj = jr.getJsonObject();
			latlong.latitude = jobj.getDouble("lat");
			latlong.longitude = jobj.getDouble("lng");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return latlong;
	}

	@Override
	public String reverseGeocode(LatLong latlong) {
		return null;
	}

}
