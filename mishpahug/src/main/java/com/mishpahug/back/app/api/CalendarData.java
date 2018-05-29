package main.java.com.mishpahug.back.app.api;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
public class CalendarData {
	@Getter @Setter private ArrayList<EventDataShort> myEvents;
	@Getter @Setter private ArrayList<EventDataShort> subscribedEvents;
}
