package main.java.com.mishpahug.back.app.api;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.java.com.mishpahug.back.app.model.User;

@NoArgsConstructor
@AllArgsConstructor
public class UserDataWithFullName {
	@Getter @Setter private Long userId;
	@Getter @Setter private String fullName;
	@Getter @Setter private String confession;
	@Getter @Setter private String gender;
	@Getter @Setter private Integer age;
	@Getter @Setter private ArrayList<String> pictureLink; 
	@Getter @Setter private String phoneNumber; 	
	@Getter @Setter private String maritalStatus;
	@Getter @Setter private ArrayList<String> foodPreferences;
	@Getter @Setter private ArrayList<String> languages;
	@Getter @Setter private Double rate;
	@Getter @Setter private Integer numberOfVoters;
	@Getter @Setter private Boolean isInvited;
	
	public UserDataWithFullName(User user) {
		this.userId = user.getId();
		this.fullName= user.getFirstName() + " " +user.getLastName();
		this.confession = (user.getConfession() != null) ? user.getConfession().getConfession() : null;
		this.gender = (user.getGender() != null) ? user.getGender().getGender() : null;
		this.age = user.getAge();
		this.pictureLink = user.getPictureLink();
		this.phoneNumber = user.getPhoneNumber();
		this.maritalStatus = (user.getMaritalStatus() != null) ? user.getMaritalStatus().getMaritalStatus() : null;
		this.foodPreferences = (user.getFoodPreferences() != null) ? User.fpToString(user.getFoodPreferences()) : new ArrayList<String>();
		this.languages = (user.getLanguages() != null) ? User.langToString(user.getLanguages()) : new ArrayList<String>();
		this.numberOfVoters = user.getNumberOfVoters();
		this.rate = user.getRate();
	}
}
