package main.java.com.mishpahug.back.app.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.java.com.mishpahug.back.app.model.Location;

@NoArgsConstructor
@AllArgsConstructor
public class LocationData {
	@Getter @Setter private Double lat;
	@Getter @Setter private Double lng;
	
	public LocationData(Location loc) {
		this.lat = loc.getLat();
		this.lng = loc.getLng();
	}
}
