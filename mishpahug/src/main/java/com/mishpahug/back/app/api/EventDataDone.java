package main.java.com.mishpahug.back.app.api;

import static main.java.com.mishpahug.back.app.constants.DateFormat.formatForDate;
import static main.java.com.mishpahug.back.app.constants.DateFormat.formatForTime;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.java.com.mishpahug.back.app.model.Event;
import main.java.com.mishpahug.back.app.model.EventWithStatusDone;
import main.java.com.mishpahug.back.app.model.FoodPreferences;

@NoArgsConstructor
@AllArgsConstructor
public class EventDataDone {
	@Getter @Setter private Long eventId;
	@Getter @Setter private String title;
	@Getter @Setter private String holiday;
	@Getter @Setter private String confession;
	@Getter @Setter private String date;
	@Getter @Setter private String description;
	@Getter @Setter private String status;
	@Getter @Setter private UserDataWithFullNameWithoutId owner;
	
	public EventDataDone (Event event) {
		this.eventId = event.getEventId();
		this.title = event.getTitle();
		this.holiday = (event.getHoliday() != null) ? event.getHoliday().getHoliday() : null;
		this.confession = (event.getConfession() != null) ? event.getConfession().getConfession() : null;;
		this.date = formatForDate.format(event.getDate());
		this.description = event.getDescription();
		this.status = event.getStatus();
		this.owner = new UserDataWithFullNameWithoutId(event.getOwner());
	}
	
	public EventDataDone (EventWithStatusDone event) {
		this.eventId = event.getEventId();
		this.title = event.getTitle();
		this.holiday = (event.getHoliday() != null) ? event.getHoliday().getHoliday() : null;
		this.confession = (event.getConfession() != null) ? event.getConfession().getConfession() : null;;
		this.date = formatForDate.format(event.getDate());
		this.description = event.getDescription();
		this.status = event.getStatus();
		this.owner = new UserDataWithFullNameWithoutId(event.getOwner());
	}
}