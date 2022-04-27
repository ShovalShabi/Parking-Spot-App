package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import exceptions.UnvalidAddressException;

public class AddressFinder {
	private static String urlString = "https://nominatim.openstreetmap.org/search?q=";
	private static String country = "Israel";
	private static String format = "&format=json";
	private static String limit = "&limit=1";

	public AddressFinder() {}// empty constructor

	/* A class that importing data from nominatim API (belongs to Open Street Map) by JSON format
	 * Brings the coordinates of a specific address by name
	 ******************************************************************************************/
	public Address getValidAddress(String city, String street, String numHouse)throws UnvalidAddressException {
		try {
			// getting the url connection
			URL url = new URL(urlString + "+" + country + "+" + city + "+" + street + "+" + numHouse + format + limit);
			StringBuilder sb = new StringBuilder();
			String line;
			InputStream in = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));// reads input as string from the
																					// stream
			while ((line = reader.readLine()) != null) {
				sb.append(line).append(System.lineSeparator());
			}
			FileWriter file = new FileWriter("Data.json");
			file.write(sb.toString());// writing to file Data.json
			file.close();
			JSONParser jsonParser = new JSONParser();
			Object obj = jsonParser.parse(new FileReader("Data.json"));
			JSONArray l1 = (JSONArray) ((JSONArray) obj);
			if (l1 == null || l1.isEmpty())throw new UnvalidAddressException(city,street,numHouse);// in case it's null or empty
			double latidtude = Double.parseDouble((String) (((JSONObject) l1.get(0)).get("lat")));// latitude
			double longtitude = Double.parseDouble((String) (((JSONObject) l1.get(0)).get("lon")));// longtitude
			Coordinate coordinate = new Coordinate(latidtude, longtitude);
			System.out.println("Found address at coordinates:"+coordinate);
			Address address = new Address(country, city, street, numHouse, coordinate);
			return address;

		} catch (IOException | ParseException e) {
		}
		return null;
	}

}
