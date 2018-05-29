package main.java.com.mishpahug.back.app.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class Event {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", nullable = false)
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
	@Getter @Setter private List <User> subscribers;
	@ManyToMany
	@Getter @Setter private List <User> invited;
	
	@ManyToMany
	@Getter @Setter private List <User> wereSubscribed;

}