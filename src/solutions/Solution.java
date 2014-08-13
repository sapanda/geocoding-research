package solutions;


public interface Solution {
    public String normalize(String address);
    public LatLong geocode(String address);
    public String reverseGeocode(LatLong latlong);
}