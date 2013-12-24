package data;

public class TemperatureData {

	private float temperature;
	private float humidity;
	private String status;
	private String placeName;
	private TemperatureLocation location;
	
	public float getTemperature() {
		return temperature;
	}
	
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	
	public float getHumidity() {
		return humidity;
	}
	
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public String getPlaceName() {
		return placeName;
	}
	
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	
	public TemperatureLocation getLocation() {
		return location;
	}
	
	public void setLocation(TemperatureLocation location) {
		this.location = location;
	}
	
}
