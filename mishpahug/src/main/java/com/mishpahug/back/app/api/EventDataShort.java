package main.java.com.mishpahug.back.app.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class EventDataShort {
	@Getter @Setter private Long eventId;
	@Getter @Setter private String title;
	@Getter @Setter private String date;
	@Getter @Setter private String time;
	@Getter @Setter private String status;
	
	
}
