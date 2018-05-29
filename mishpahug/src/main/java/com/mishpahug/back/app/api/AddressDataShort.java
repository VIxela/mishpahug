package main.java.com.mishpahug.back.app.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.java.com.mishpahug.back.app.model.Address;

@NoArgsConstructor
@AllArgsConstructor
public class AddressDataShort {
	@Getter @Setter private String city;
	
	
	public AddressDataShort (Address add) {
		this.city = add.getCity();
	}
}
