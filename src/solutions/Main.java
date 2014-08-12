package solutions;

public class Main {
    public static void main(String[] args) {
        MapQuest m = new MapQuest();
        String address = "130 Harvard Ave E, Seattle, WA 98122";
        LatLong latlong = new LatLong(47.6196523, -122.321789839922);

        System.out.println(m.normalize(address));
        System.out.println(m.geocode(address));
        System.out.println(m.reverseGeocode(latlong));
    }
}
