package main.java.com.mishpahug.back.app.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
public class ListOfEventsInProgressRequestData {
	@Getter @Setter private LocationDataForRequest location;
	@Getter @Setter private FilterData filters;
}