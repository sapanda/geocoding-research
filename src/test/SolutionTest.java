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

import org.junit.Test;

import solutions.LatLong;
import solutions.Solution;

public class SolutionTest {

    private final List<String> addressList;

    public SolutionTest() throws IOException, URISyntaxException {
        // Load the list of addresses
        addressList = new ArrayList<String>();
        String address;

        File f = new File(getClass().getResource("Addresses.txt").toURI());
        BufferedReader file = new BufferedReader(new FileReader(f));
        while ((address = file.readLine()) != null) {
            addressList.add(address);
        }
        file.close();
    }

    @Test
    public void test() {
        Solution[] solutions = new Solution[] {
//                new BingMaps(),
//                new GeocodeFarm(),
//                new Geocodio(),
//                new HereGeocoder(),
//                new MapQuest(),
//                new OpenCageGeocoder(),
//                new OpenStreetMaps(),
        };

        for (Solution sln : solutions) {
            testSolution(sln);
        }
    }

    public void testSolution(Solution sln) {

        String className = sln.getClass().getSimpleName();

        try {
            PrintWriter output = new PrintWriter("results/" + className + ".csv");
            output.println("Address, Norm, Geo, Reverse, NTime, GTime, RTime");

            System.out.println("-----------------------------");
            System.out.println(sln.getClass().getSimpleName());
            System.out.println("-----------------------------\n");

            for (String address : addressList) {
                System.out.println("Address:     " + address);

                // Normalization
                long then = System.currentTimeMillis();
                String normAddress = sln.normalize(address);
                long now = System.currentTimeMillis();
                double normTime = (now - then) / 1000.0;
                System.out.println(String.format("  (%ss)  Norm:      %s",
                        normTime, normAddress));

                LatLong latlong;

                // Geocoding
                then = System.currentTimeMillis();
                latlong = sln.geocode(address);
                now = System.currentTimeMillis();
                double geoTime = (now - then) / 1000.0;
                System.out.println(String.format("  (%ss)  Geocode:   (%s)",
                        geoTime, latlong));

                // Reverse Geocoding
                then = System.currentTimeMillis();
                String newAddress = sln.reverseGeocode(latlong);
                now = System.currentTimeMillis();
                double reverseTime = (now - then) / 1000.0;
                System.out.println(String.format("  (%ss)  R-Geocode: %s",
                        reverseTime, newAddress));

                System.out.println();
                output.println(String.format(
                        "\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                        address, normAddress, latlong, newAddress, normTime,
                        geoTime, reverseTime));
            }

            output.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}