package main.java.com.mishpahug.back.app.api;

import static main.java.com.mishpahug.back.app.constants.DateFormat.formatForDate;
import static main.java.com.mishpahug.back.app.constants.DateFormat.formatForTime;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.java.com.mishpahug.back.app.model.Event;
import main.java.com.mishpahug.back.app.model.FoodPreferences;
import main.java.com.mishpahug.back.app.model.User;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDataDependingOnStatus {
	@Getter @Setter private Long eventId;
	@Getter @Setter private String title;
	@Getter @Setter private String holiday;
	@Getter @Setter private String confession;
	@Getter @Setter private String date;
	@Getter @Setter private String time;
	@Getter @Setter private Integer duration;
	@Getter @Setter private AddressData address;
	@Getter @Setter private ArrayList<String> food;
	@Getter @Setter private String description;
	@Getter @Setter private String status;
	@Getter @Setter private ArrayList<UserDataWithFullName> participants;
	
	
	public EventDataDependingOnStatus(Event event) {
		this.eventId = event.getEventId();
		this.title = event.getTitle();
		this.holiday = (event.getHoliday() != null) ? event.getHoliday().getHoliday() : null;
		this.confession = (event.getConfession() != null) ? event.getConfession().getConfession() : null;;
		this.date = formatForDate.format(event.getDate());
		this.time = formatForTime.format(event.getDate());
		this.duration = event.getDuration();
		this.address = new AddressData(event.getAddress());
		this.food =  new ArrayList<>();
		for (FoodPreferences fp : event.getFood()) {
			food.add(fp.getFoodPreference());
		}
		this.description = event.getDescription();
		this.status = event.getStatus();
		this.participants = (event.getStatus().equals("Pending")) ? createListOfInvitedParticipants(event.getInvited())
										: createListOfSubscribersAndInvited(event.getSubscribers(), event.getInvited());
	}
	
	public static ArrayList<UserDataWithFullName> createListOfInvitedParticipants (List<User> users) {
		ArrayList<UserDataWithFullName> res = new ArrayList<>();
		for (User user : users) {
			UserDataWithFullName us = new UserDataWithFullName(user);
			us.setIsInvited(true);
			res.add(us);
		}
		return res;
	}
	
	public static ArrayList<UserDataWithFullName> createListOfSubscribersAndInvited (List<User> subscribers, List<User> invited) {
		ArrayList<UserDataWithFullName> res = new ArrayList<>();
		for (User user : invited) {
			UserDataWithFullName us = new UserDataWithFullName(user);
			us.setIsInvited(true);
			res.add(us);
		}
		for (User user : subscribers) {
			UserDataWithFullName us = new UserDataWithFullName(user);
			us.setIsInvited(false);
			res.add(us);
		}	
		return res;
	}
}
