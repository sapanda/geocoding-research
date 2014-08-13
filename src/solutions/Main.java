
package solutions;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MapLarge m = new MapLarge();
        String address = "130 Harvard Ave E, Seattle, WA 98122";

        System.out.println("Normalize: " + m.normalize(address));

        LatLong latlong = m.geocode(address);

        System.out.println("Geocode: " + latlong);
        System.out.println("Reverse Geocode: " + m.reverseGeocode(latlong));
    }
}
