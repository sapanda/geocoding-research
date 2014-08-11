package solutions;

public interface Solution {
    String normalize(String address);
    LatLong geocode(String address);
    String reverseGeocode(LatLong latlong);
}

class LatLong {
    public double latitude;
    public double longitude;

    public LatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}