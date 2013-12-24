package logic;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import data.TemperatureData;

public class Temperature {
	
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

}
