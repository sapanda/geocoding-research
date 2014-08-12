package solutions;

import java.util.ArrayList;

public interface Solution {
    String[] normalize(String address);
    ArrayList<LatLong> geocode(String address);
    String[] reverseGeocode(LatLong latlong);
}

class LatLong {
    public double latitude;
    public double longitude;

    public LatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}