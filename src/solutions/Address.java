package solutions;

public class Address {

    public String houseNumber = "";
    public String street = "";
    public String district = "";
    public String city = "";
    public String county = "";
    public String state = "";
    public String country = "";
    public String postalCode = "";

    @Override
    public String toString() {
        String retVal = "";
        if (houseNumber.length() > 0) {
            retVal += houseNumber + " ";
        }
        if (street.length() > 0) {
            retVal += street + ", ";
        }
        // Don't print the district
        if (city.length() > 0) {
            retVal += city + ", ";
        }
        // Don't print the county
        if (state.length() > 0) {
            retVal += state + " ";
        }
        if (postalCode.length() > 0) {
            retVal += postalCode;
        }
        if (country.length() > 0) {
            retVal += ", " + country;
        }
        return retVal;
    }
}
