package main.java.com.mishpahug.back.app.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Language {
	@Id
	@Getter @Setter private String language;
	
	@ManyToMany(mappedBy="languages")
	@Getter @Setter private List <User> users;

	public Language(String language) {
		this.language = language;
	}
}
