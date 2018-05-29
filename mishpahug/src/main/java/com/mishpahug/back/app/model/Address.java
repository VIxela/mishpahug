package main.java.com.mishpahug.back.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {
	@Getter @Setter private String city;
	@Id
	@Getter @Setter private String place_id;
	@ManyToOne
	@Getter @Setter private Location location;
}
