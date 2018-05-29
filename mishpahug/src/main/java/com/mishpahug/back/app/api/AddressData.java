package main.java.com.mishpahug.back.app.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.java.com.mishpahug.back.app.model.Address;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressData {
	@Getter @Setter private String city;
	@Getter @Setter private String place_id;
	@Getter @Setter private LocationData location;
	
	
	public AddressData(Address add) {
		this.city = add.getCity();
		this.place_id = add.getPlace_id();
		this.location = new LocationData(add.getLocation());
	}
}
