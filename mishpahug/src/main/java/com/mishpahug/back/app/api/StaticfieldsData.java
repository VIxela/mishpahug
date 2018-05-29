package main.java.com.mishpahug.back.app.api;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
public class StaticfieldsData {
	@Getter @Setter private Collection<String> confession;
	@Getter @Setter private Collection<String> gender;
	@Getter @Setter private Collection<String> maritalStatus;
	@Getter @Setter private Collection<String> foodPreferences;
	@Getter @Setter private Collection<String> languages;
	@Getter @Setter private Collection<String> holiday;
}
