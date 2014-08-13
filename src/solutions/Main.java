
package solutions;

public class Main {
    public static void main(String[] args) {
        OpenStreetMaps m = new OpenStreetMaps();
        String address = "5490 South Shore Dr, Chicago IL 60615";

        System.out.println("Normalize: " + m.normalize(address));

        LatLong latlong = m.geocode(address);

        System.out.println("Geocode: " + latlong);
        System.out.println("Reverse Geocode: " + m.reverseGeocode(latlong));
    }
}
