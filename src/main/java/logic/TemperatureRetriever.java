package logic;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.TemperatureData;
import data.TemperatureLocation;

public class TemperatureRetriever {
	
	// Static to avoid compilation every time => faster execution
	private static Pattern bodyPattern = Pattern.compile("<body>(.*?)</body>", Pattern.DOTALL);
	private static Pattern placeNamePattern = Pattern.compile("\\[(.*)\\]");
	private static Pattern temperaturePattern = Pattern.compile("<b>(.+)\\s.*</b>");
	private static Pattern humidityPattern = Pattern.compile("<b>(.+)\\s.*</b>");
	private static Pattern statusPattern = Pattern.compile("<b>(.+)</b>");
	private static Pattern locationPattern = Pattern.compile("Latitude=(.+);\\sLongitude=(.+\\d)");
	
	private URL url;

	public TemperatureRetriever() throws MalformedURLException {
		url = new URL("http://dsnet.tu-plovdiv.bg/3TierJSP/minimal.jsp");
	}
	
	private String getHtmlData() throws IOException {
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent", "University REST temprature server v0.1");
		
		InputStream is = conn.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		
		StringBuilder response = new StringBuilder();
		byte[] data = new byte[1024];
		int bytesRead;
		
		while ((bytesRead = bis.read(data)) != -1) {
			response.append(new String(data, 0, bytesRead));	// now using default charset (might require change)
		}
		
		return response.substring(response.indexOf("\n\n") + 1);
	}
	
	private TemperatureData parseTemperatureData(String strData) {
		TemperatureData data = new TemperatureData();
		
		String[] lines = strData.split("\n");
		Matcher placeNameMatcher = placeNamePattern.matcher(lines[0]);
		Matcher temperatureMatcher = temperaturePattern.matcher(lines[0]);
		Matcher humidityMatcher = humidityPattern.matcher(lines[1]);
		Matcher statusMatcher = statusPattern.matcher(lines[2]);
		Matcher locationMatcher = locationPattern.matcher(lines[3]);
		
		data.setPlaceName( (placeNameMatcher.find()) ? placeNameMatcher.group(1) : "");
		data.setTemperature( (temperatureMatcher.find()) ? Float.parseFloat(temperatureMatcher.group(1)) : 0);
		data.setHumidity( (humidityMatcher.find()) ? Float.parseFloat(humidityMatcher.group(1)) : 0);
		data.setStatus( (statusMatcher.find()) ? statusMatcher.group(1) : "");
		
		TemperatureLocation location = new TemperatureLocation();
		
		if (locationMatcher.find()) {
			location.setLatitude(Double.parseDouble(locationMatcher.group(1)));
			location.setLongitude(Double.parseDouble(locationMatcher.group(2)));
		} else {
			location.setLatitude(0);
			location.setLongitude(0);
		}
		data.setLocation(location);
		
		return data;
	}
	
	private ArrayList<TemperatureData> parsePageValues(String page) {
		ArrayList<TemperatureData> temps = new ArrayList<TemperatureData>();
		
		Matcher bodyMatcher = bodyPattern.matcher(page);
		String body = null;
		
		if (bodyMatcher.find()) {
			body = bodyMatcher.group(1);			
		} else {
			return null;
		}
		
		String[] places = body.split("<hr>");
		
		// Skip the last element, since it only shows the page generation date
		for (int i = 0; i < places.length - 1; i++) {
			TemperatureData data = parseTemperatureData(places[i].trim());
			temps.add(data);
		}
		
		return temps;
	}
	
	public ArrayList<TemperatureData> getTemperatureData() throws IOException {
		return parsePageValues(getHtmlData());
	}

}
