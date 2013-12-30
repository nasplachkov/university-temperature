package logic;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import data.TemperatureData;
import data.TemperatureLocation;

public class Temperature {
	
	private static double EARTH_RADIUS = 6371;	// Earth's radius in KM
	
	private ArrayList<TemperatureData> temps;

	public Temperature() throws IOException {
		TemperatureRetriever retriever = new TemperatureRetriever();
		temps = retriever.getTemperatureData();
	}
	
	public String getTemperatures() {
		ArrayList<Float> temperatures = new ArrayList<Float>();
		
		for (TemperatureData datum : temps) {
			temperatures.add(datum.getTemperature());
		}
		
		JSONObject temperatureObject = new JSONObject();
		temperatureObject.put("temperatures", temperatures);
		
		return temperatureObject.toString();
	}
	
	public String getHumidities() {
		ArrayList<Float> humidities = new ArrayList<Float>();
		
		for (TemperatureData datum : temps) {
			humidities.add(datum.getHumidity());
		}
		
		JSONObject humidityObject = new JSONObject();
		humidityObject.put("humidities", humidities);
		
		return humidityObject.toString();
	}
	
	public String getByName(String name) {
		TemperatureData byName = null;
		
		for (TemperatureData datum : temps) {
			if (datum.getPlaceName().equals(name)) {
				byName = datum;
				break;
			}
		}
		
		JSONObject temperatureObject = null;
		
		if (byName == null) {
			temperatureObject = new JSONObject();
			temperatureObject.put("info", "There is no data for a location with that name.");
		} else {
			temperatureObject = new JSONObject(byName);
		}
				
		return temperatureObject.toString();
	}
	
	public String getAll() {
		JSONObject all = new JSONObject();
		all.put("list", temps);
		
		return all.toString();
	}
	
	private double distanceBetween(TemperatureLocation tl1, TemperatureLocation tl2) {
	    double dLat = Math.toRadians(tl2.getLatitude() - tl1.getLatitude());
	    double dLng = Math.toRadians(tl2.getLongitude() - tl1.getLongitude());
	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);
	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	            * Math.cos(Math.toRadians(tl1.getLatitude())) * Math.cos(Math.toRadians(tl2.getLatitude()));
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double dist = EARTH_RADIUS * c;

	    return dist;
    }
	
	public String getLocations(double latCenter, double longCenter, double radius) {
		if (radius == 0) {
			radius = 0.00001;
		}
		
		ArrayList<TemperatureData> byLocations = new ArrayList<TemperatureData>();
		TemperatureLocation center = new TemperatureLocation(latCenter, longCenter);
		
		for (TemperatureData datum : temps) {
			
			// Is location in the radius?
			if (distanceBetween(center, datum.getLocation()) <= radius) {
				byLocations.add(datum);
			}
		}
		
		JSONObject locationsObject = new JSONObject();
		JSONObject centerObject = new JSONObject();
		centerObject.put("latitude", center.getLatitude());
		centerObject.put("longitutde", center.getLongitude());
		
		locationsObject.put("center", centerObject);
		locationsObject.put("radius", radius);
		locationsObject.put("locations", byLocations);
		
		return locationsObject.toString();
	}

}
