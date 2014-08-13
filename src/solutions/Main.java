
package solutions;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        GeocoderUS m = new GeocoderUS();
        String address = "2320 N Damen, Chicago, ILLINOIS IL";

        System.out.println("Normalize: " + m.normalize(address));

        LatLong latlong = m.geocode(address);

        System.out.println("Geocode: " + latlong);
        System.out.println("Reverse Geocode: " + m.reverseGeocode(latlong));
    }
}
