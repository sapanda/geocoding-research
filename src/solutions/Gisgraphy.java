package solutions;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

public class Gisgraphy extends Solution {

	@Override
	public String normalize(String address) {
		String normAddress = "";

		// Make the query
		final String url = "http://addressparser.appspot.com/webaddressparser?";

		final Reference ref = new Reference(url);
		ref.addQueryParameter("address", address);
		ref.addQueryParameter("country", "US");
		ref.addQueryParameter("format", "json");

		final Representation rep = getRepresentation(ref);

		try {
			// Parse the Data
			final JsonRepresentation jr = new JsonRepresentation(rep);
			final JSONArray jarr = jr.getJsonObject().getJSONArray("result");

			// TODO: Deal with multiple return values
			if (jarr.length() > 0) {
				final JSONObject jobj = jarr.getJSONObject(0);
				normAddress = parseAddress(jobj);
			}

		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final JSONException e) {
			e.printStackTrace();
		}

		return normAddress;
	}

	@Override
	public LatLong geocode(String address) {
		final LatLong latlong = new LatLong(0, 0);

		// Make the query
		// only returns the first response
		final String url = "http://services.gisgraphy.com/geocoding/geocode";

		final Reference ref = new Reference(url);
		ref.addQueryParameter("address", address);
		ref.addQueryParameter("country", "US");
		ref.addQueryParameter("format", "json");

		final Representation rep = getRepresentation(ref);

		try {
			// Parse the Data
			final JsonRepresentation jr = new JsonRepresentation(rep);
			final JSONArray jarr = jr.getJsonObject().getJSONArray("result");

			if (jarr.length() > 0) {
				final JSONObject jobj = jarr.getJSONObject(0);
				latlong.latitude = jobj.getDouble("lat");
				latlong.longitude = jobj.getDouble("lng");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final JSONException e) {
			e.printStackTrace();
		}

		return latlong;
	}

	@Override
	public String reverseGeocode(LatLong latlong) {
		if (true) {
			// The above doesn't return anything useful, just really vague info
			// like that it's a "residential" street or in the
			// "Southern Chicago Heights"
			// So I won't actually use the data I get from it
			return null;
		}
		String address = "";

		// Make the query
		// only returns the first response
		final String url = "http://services.gisgraphy.com/street/streetsearch?";

		final Reference ref = new Reference(url);
		ref.addQueryParameter("lat", String.valueOf(latlong.latitude));
		ref.addQueryParameter("lng", String.valueOf(latlong.longitude));
		ref.addQueryParameter("from", "1");
		ref.addQueryParameter("to", "1");
		ref.addQueryParameter("format", "json");

		final Representation rep = getRepresentation(ref);

		try {
			// Parse the Data
			final JsonRepresentation jr = new JsonRepresentation(rep);

			final JSONArray jarr = jr.getJsonObject().getJSONArray("result");
			final JSONObject jobj = jarr.getJSONObject(0);
			address = parseAddress(jobj);

		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final JSONException e) {
			e.printStackTrace();
		}

		return address;
	}

	private String parseAddress(JSONObject jobj) throws JSONException {
		String address = "";

		if (jobj.has("houseNumber")) {
			address += jobj.getString("houseNumber") + " ";
		}
		if (jobj.has("streetName")) {
			address += jobj.getString("streetName") + " ";
		}
		if (jobj.has("streetType")) {
			address += jobj.getString("streetType") + ", ";
		}
		if (jobj.has("city")) {
			address += jobj.getString("city") + ", ";
		}
		// has no state
		if (jobj.has("state")) {
			address += jobj.getString("state") + ", ";
		}
		if (jobj.has("zipCode")) {
			address += jobj.getString("zipCode");
		}

		return address;
	}
}
