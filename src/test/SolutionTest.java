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

import solutions.GeocoderUS;
import solutions.LatLong;
import solutions.Solution;

public class SolutionTest {

	private final List<String> addressList;

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
	public void test() {
		final Solution[] solutions = new Solution[] {
		// new GeocodeFarm(),
		// new Geocodio(),
		// new HereGeocoder(),
		// new MapQuest(),
		// new OpenCageGeocoder(),
		// new OpenStreetMaps()
		// new Gisgraphy()
		new GeocoderUS() };

		for (final Solution sln : solutions) {
			testSolution(sln);
		}
	}

	public void testSolution(Solution sln) {

		final String className = sln.getClass().getSimpleName();

		try {
			final PrintWriter output = new PrintWriter("results/" + className
					+ ".csv");
			output.println("Address, Norm, Geo, Reverse, NTime, GTime, RTime");

			System.out.println("-----------------------------");
			System.out.println(sln.getClass().getSimpleName());
			System.out.println("-----------------------------\n");

			for (final String address : addressList) {
				System.out.println("Address:     " + address);
				// // use timer for gisgraphy
				// try {
				// Thread.sleep(2000); // 1000 milliseconds is one second.
				// } catch (final InterruptedException ex) {
				// Thread.currentThread().interrupt();
				// }

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}