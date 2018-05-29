package main.java.com.mishpahug.back.app.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EventWithStatusDone {	
	@Id
	@Getter @Setter private Long eventId;
	@Getter @Setter private String title;
	@ManyToOne
	@Getter @Setter private Holiday holiday;
	@ManyToOne
	@Getter @Setter private Address address;
	@ManyToOne
	@Getter @Setter private Confession confession;
	@Getter @Setter private Date date;
	@Getter @Setter private Integer duration;
	@Getter @Setter private Date dateEnding;
	@ManyToMany
	@Getter @Setter private List <FoodPreferences> food;
	@Column(columnDefinition = "varchar(1000)")
	@Getter @Setter private String description;
	@Getter @Setter private String status;
	@ManyToOne
	@Getter @Setter private User owner;
	@ManyToMany
	@Getter @Setter private List <User> invited;
	
	
	public EventWithStatusDone(Event event) {
		this.eventId = event.getEventId();
		this.title = event.getTitle();
		this.holiday = event.getHoliday();
		this.address = event.getAddress();
		this.confession = event.getConfession();
		this.date = event.getDate();
		this.duration = event.getDuration();
		this.dateEnding = event.getDateEnding();
		this.food = event.getFood();
		this.description = event.getDescription();
		this.status = event.getStatus();
		this.owner = event.getOwner();
		this.invited = event.getInvited();
	}
}
