package com.aim.project.ssp.instance.reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.instance.SSPInstance;
import com.aim.project.ssp.interfaces.SSPInstanceInterface;
import com.aim.project.ssp.interfaces.SSPInstanceReaderInterface;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public class SSPInstanceReader implements SSPInstanceReaderInterface {

	@Override
	public SSPInstanceInterface readSSPInstance(Path path, Random random) {

		try (BufferedReader br = Files.newBufferedReader(path))
		{
			String line;
			Location hotel = null;
			Location airport = null;
			List<Location> tempList = new ArrayList<>(); //list to hold all locations - dynamically size changing array

			while ((line = br.readLine()) != null) {

				line = line.trim(); //remove all leading and trailing whitespace

				if (line.equals("HOTEL_LOCATION")) {

					String[] coords = br.readLine().trim().split("\\s+"); //splits cords into 2 strings
					hotel = new Location(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));

				} else if (line.equals("AIRPORT_LOCATION")) {

					String[] coords = br.readLine().trim().split("\\s+");
					airport = new Location(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));

				} else if (line.equals("POINTS_OF_INTEREST")) {

					while ((line = br.readLine()) != null && !line.equals("EOF")) { //searching until EOF text

						String[] coords = line.trim().split("\\s+");
						tempList.add(new Location(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]))); //convert to int and create location instance
					}
				}
			}
			Location[] locations = tempList.toArray(tempList.toArray(new Location[0]));

			return new SSPInstance(locations.length , locations , hotel , airport , random);

		} catch ( IOException e) {

			e.printStackTrace();
			return null;

		}

	}
}
