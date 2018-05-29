package main.java.com.mishpahug.back.app.api;

import static main.java.com.mishpahug.back.app.constants.DateFormat.formatForDate;
import static main.java.com.mishpahug.back.app.constants.DateFormat.formatForTime;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.java.com.mishpahug.back.app.model.Event;
import main.java.com.mishpahug.back.app.model.FoodPreferences;

@NoArgsConstructor
@AllArgsConstructor
public class EventDataForListInProgress {
	@Getter @Setter private Long eventId;
	@Getter @Setter private String title;
	@Getter @Setter private String holiday;
	@Getter @Setter private String confession;
	@Getter @Setter private String date;
	@Getter @Setter private String time;
	@Getter @Setter private Integer duration;
	@Getter @Setter private AddressDataShort address;
	@Getter @Setter private ArrayList<String> food;
	@Getter @Setter private String description;
	@Getter @Setter private UserDataWithFullNameWithoutId owner;
	
	
	public EventDataForListInProgress(Event event) {
		this.eventId = event.getEventId();
		this.title = event.getTitle();
		this.holiday = (event.getHoliday() != null) ? event.getHoliday().getHoliday() : null;
		this.address = new AddressDataShort(event.getAddress());
		this.confession = (event.getConfession() != null) ? event.getConfession().getConfession() : null;;
		this.date = formatForDate.format(event.getDate());
		this.time = formatForTime.format(event.getDate());
		this.duration = event.getDuration();
		this.food = new ArrayList<>();
			for (FoodPreferences fp : event.getFood()) {
				food.add(fp.getFoodPreference());
			}
		this.description = event.getDescription();
		this.owner = new UserDataWithFullNameWithoutId(event.getOwner());
	}

}
