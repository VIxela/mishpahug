package main.java.com.mishpahug.back.app.api;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.java.com.mishpahug.back.app.model.Event;
import main.java.com.mishpahug.back.app.model.FoodPreferences;
import static main.java.com.mishpahug.back.app.constants.DateFormat.*;

@NoArgsConstructor
@AllArgsConstructor
public class EventData {
	
	@Getter @Setter private String title;
	@Getter @Setter private AddressData address;
	@Getter @Setter private String confession;
	@Getter @Setter private String holiday;
	@Getter @Setter private String date;
	@Getter @Setter private Integer duration;
	@Getter @Setter private String time;
	@Getter @Setter private ArrayList<String> food;
	@Getter @Setter private String description;
	
	public EventData(Event event) {
		this.title = event.getTitle();
		this.address = new AddressData(event.getAddress());
		this.confession = event.getConfession().getConfession();
		this.date = formatForDate.format(event.getDate());
		this.time = formatForTime.format(event.getDate());
		this.duration = event.getDuration();
		this.food = new ArrayList<>();
			for (FoodPreferences fp : event.getFood()) {
				food.add(fp.getFoodPreference());
			}
		this.description = event.getDescription();
	}
}
