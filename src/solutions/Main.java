package solutions;

public class Main {
    public static void main(String[] args) {
        HereGeocoder m = new HereGeocoder();
        String address = "130 Harvard Ave E, Seattle, WA 98122";
        LatLong latlong = new LatLong(47.6197319, -122.3221893);

        System.out.println(m.normalize(address));
        System.out.println(m.geocode(address));
        System.out.println(m.reverseGeocode(latlong));
    }
}
