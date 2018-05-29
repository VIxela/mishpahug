package main.java.com.mishpahug.back.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Location {
	@Id
	@Getter @Setter private String id;
	@Getter @Setter private Double lat;
	@Getter @Setter private Double lng;
}
