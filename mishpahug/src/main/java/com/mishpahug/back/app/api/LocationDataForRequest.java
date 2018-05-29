package main.java.com.mishpahug.back.app.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@NoArgsConstructor
@AllArgsConstructor
public class LocationDataForRequest {
	@Getter @Setter private Double lat;
	@Getter @Setter private Double lng;
	@Getter @Setter private Double radius;
}
