package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import solutions.BingMaps;
import solutions.GeocodeFarm;
import solutions.GeocoderUS;
import solutions.Gisgraphy;
import solutions.HereGeocoder;
import solutions.LatLong;
import solutions.MapLarge;
import solutions.MapQuest;
import solutions.OpenCageGeocoder;
import solutions.OpenStreetMaps;
import solutions.SmartyStreets;
import solutions.Solution;

public class SolutionTest {

    private final List<String> addressList;
    private final Solution[] solutions = new Solution[] {
            new BingMaps(),
            new GeocodeFarm(),
            new GeocoderUS(),
//            new Geocodio(), // Untestable as it results in huge timeouts
            new Gisgraphy(),
            new HereGeocoder(),
            new MapLarge(),
            new MapQuest(),
            new OpenCageGeocoder(),
            new OpenStreetMaps(),
            new SmartyStreets(),
   };

    public SolutionTest() throws IOException, URISyntaxException {
        // Load the list of addresses
        addressList = new ArrayList<String>();
        String address;

        final File f = new File(getClass().getResource("Addresses.txt").toURI());
        final BufferedReader file = new BufferedReader(new FileReader(f));
        while ((address = file.readLine()) != null) {
            addressList.add(address);
        }
        file.close();
    }

    @Test
    public void testAllOnSingleAddress() {
        for (final Solution sln : solutions) {
            System.out.println("-----------------------------");
            System.out.println(sln.getClass().getSimpleName());
            System.out.println("-----------------------------");

            String address = "3000 MacArthur Blvd, Chicago, IL 60062";

            System.out.println("Normalize: " + sln.normalize(address));

            LatLong latlong = sln.geocode(address);

            System.out.println("Geocode: " + latlong);
            System.out.println("Reverse Geocode: " + sln.reverseGeocode(latlong));
            System.out.println();
        }
    }

    @Test
    @Ignore
    public void testAllFromFile() {
        for (final Solution sln : solutions) {

            try {
                final String className = sln.getClass().getSimpleName();

                final PrintWriter output = new PrintWriter("results/" + className + ".csv");
                output.println("Address, Norm, Geo, Reverse, NTime, GTime, RTime");

                System.out.println("-----------------------------");
                System.out.println(className);
                System.out.println("-----------------------------\n");

                for (final String address : addressList) {
                    System.out.println("Address:     " + address);

                    // Use timer for Gisgraphy since it limits calls per second
                    if (sln instanceof Gisgraphy) {
                        try {
                            Thread.sleep(2000);
                        } catch (final InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    // Normalization
                    long then = System.currentTimeMillis();
                    final String normAddress = sln.normalize(address);
                    long now = System.currentTimeMillis();
                    final double normTime = (now - then) / 1000.0;
                    System.out.println(String.format("  (%ss)  Norm:      %s",
                            normTime, normAddress));

                    LatLong latlong;

                    // Geocoding
                    then = System.currentTimeMillis();
                    latlong = sln.geocode(address);
                    now = System.currentTimeMillis();
                    final double geoTime = (now - then) / 1000.0;
                    System.out.println(String.format("  (%ss)  Geocode:   (%s)",
                            geoTime, latlong));

                    // Reverse Geocoding
                    then = System.currentTimeMillis();
                    final String newAddress = sln.reverseGeocode(latlong);
                    now = System.currentTimeMillis();
                    final double reverseTime = (now - then) / 1000.0;
                    System.out.println(String.format("  (%ss)  R-Geocode: %s",
                            reverseTime, newAddress));

                    System.out.println();
                    output.println(String.format(
                            "\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                            address, normAddress, latlong, newAddress, normTime,
                            geoTime, reverseTime));
                }

                output.close();

            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}