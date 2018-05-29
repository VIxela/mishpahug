package main.java.com.mishpahug.back.app.api;

import static main.java.com.mishpahug.back.app.constants.DateFormat.formatForDate;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.java.com.mishpahug.back.app.model.User;

@NoArgsConstructor
@AllArgsConstructor
public class UserDataWithoutRate {
	@Getter @Setter private String firstName;
	@Getter @Setter private String lastName;
	@Getter @Setter private String dateOfBirth;
	@Getter @Setter private String gender;
	@Getter @Setter private String maritalStatus;
	@Getter @Setter private String confession;
	@Getter @Setter private ArrayList<String> pictureLink; 
	@Getter @Setter private String phoneNumber;
	@Getter @Setter private ArrayList<String> foodPreferences;
	@Getter @Setter private ArrayList<String> languages;
	@Getter @Setter private String description;
	
		
	public UserDataWithoutRate(User user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.dateOfBirth = (user.getDateOfBirth() != null) ? formatForDate.format(user.getDateOfBirth()) : null;
		this.confession = (user.getConfession() != null) ? user.getConfession().getConfession() : null;
		this.gender = (user.getGender() != null) ? user.getGender().getGender() : null;
		this.pictureLink = user.getPictureLink();
		this.phoneNumber = user.getPhoneNumber();
		this.maritalStatus = (user.getMaritalStatus() != null) ? user.getMaritalStatus().getMaritalStatus() : null;
		this.foodPreferences = (user.getFoodPreferences() != null) ? User.fpToString(user.getFoodPreferences()) : new ArrayList<String>();
		this.languages = (user.getLanguages() != null) ? User.langToString(user.getLanguages()) : new ArrayList<String>();
		this.description = user.getDescription();
	}

}
