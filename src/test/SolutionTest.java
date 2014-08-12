package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import solutions.LatLong;
import solutions.OpenStreetMaps;
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
    public void testOpenStreetMaps() {
        OpenStreetMaps m = new OpenStreetMaps();
        testSolution(m);
    }

    public void testSolution(Solution sln) {

        System.out.println("-----------------------------");
        System.out.println(sln.getClass().getSimpleName());
        System.out.println("-----------------------------\n");

        for (String address : addressList) {
            System.out.println("Address:     " + address);
            System.out.println("  Norm:      " + sln.normalize(address));

            LatLong latlong = sln.geocode(address);

            System.out.println("  Geocode:   " + latlong);
            System.out.println("  R-Geocode: " + sln.reverseGeocode(latlong));
            System.out.println();
        }
    }
}