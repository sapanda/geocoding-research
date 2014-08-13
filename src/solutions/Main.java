package solutions;

public class Main {
	public static void main(String[] args) {
		GeocoderUS m = new GeocoderUS();
		// String address = "1000 Central St Chicago, IL 60201";
		String address = "2744 1/2 N. Hampden Ct, Chicago, 60614";

		System.out.println("Normalize: " + m.normalize(address));

		LatLong latlong = m.geocode(address);

		System.out.println("Geocode: " + latlong);
		System.out.println("Reverse Geocode: " + m.reverseGeocode(latlong));
	}
}
