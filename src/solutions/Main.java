package solutions;

public class Main {
	public static void main(String[] args) {
		final MapLarge m = new MapLarge();
		final String address = "5490 South Shore Dr, Chicago, IL 60615";

		System.out.println("Normalize: " + m.normalize(address));

		final LatLong latlong = m.geocode(address);

		System.out.println("Geocode: " + latlong);
		System.out.println("Reverse Geocode: " + m.reverseGeocode(latlong));
	}
}
