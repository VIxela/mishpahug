package main.java.com.mishpahug.back.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.java.com.mishpahug.back.app.api.LoginData;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	@Getter @Setter private Long Id;
	@Getter @Setter private String email;
	@Getter @Setter private String password;
	@Getter @Setter private String firstName;
	@Getter @Setter private String lastName;
	@Getter @Setter private Date dateOfBirth;
	@ManyToOne
	@Getter @Setter private Gender gender;
	@Getter @Setter private Integer age;
	@ManyToOne
	@Getter @Setter private MaritalStatus maritalStatus;
	@ManyToOne
	@Getter @Setter private Confession confession;
	@Lob
	@Column(length=1000)
	@Getter @Setter private ArrayList<String> pictureLink; 
	@Getter @Setter private String phoneNumber;
	@ManyToMany
	@Getter @Setter private List<FoodPreferences> foodPreferences;
	@ManyToMany
	@Getter @Setter private List<Language> languages;
	@Column(columnDefinition = "varchar(300)")
	@Getter @Setter private String description;
	
	//for rating
	@Getter @Setter private Double rate;	
	@Getter @Setter private Double cumulativeScore;
	@Getter @Setter private Integer numberOfVoters;
	
	//три списка - мои ивенты, ивенты на которые я подписан и ивенты на которые приглашен
	@OneToMany(mappedBy="owner")
	@Getter @Setter private List<Event> myEvents;	
	@ManyToMany(mappedBy="subscribers")
	@Getter @Setter private List <Event> subEvents;
	@ManyToMany(mappedBy="invited")
	@Getter @Setter private List <Event> invEvents;
	@ManyToMany(mappedBy="invited")
	@Getter @Setter private List <EventWithStatusDone> invEventsDone;
	
	@ManyToMany(mappedBy="wereSubscribed")
	@Getter @Setter private List <Event> wasSubscribed;
	
	@OneToMany(mappedBy="notOwner")
	@Getter @Setter private List<Notification> notifications;
	@Getter @Setter private Boolean emptyProfile;

	
	public User(String email, String password) {
		this.Id = null;
		this.email = email;
		this.password = md5Apache(password);
		this.numberOfVoters = 0;
		this.cumulativeScore = 0.;
		this.rate = 0.;
		this.emptyProfile = true;
	}
	
	public void changeRating (Double score) {
		this.numberOfVoters += 1;
		this.cumulativeScore += score;
		this.rate = cumulativeScore / numberOfVoters;
	}
	
	public void changeRatingUsers (Integer numberOfUsers) {
		this.numberOfVoters += numberOfUsers;
		this.cumulativeScore += numberOfUsers;
		this.rate = cumulativeScore / numberOfVoters;
	}
	
	public static String md5Apache(String st) {
	    String md5Hex = DigestUtils.md5Hex(st);	 
	    return md5Hex;
	}
	
	public static ArrayList<String> fpToString (List<FoodPreferences> fps) {
		ArrayList<String> res = new ArrayList<>();
		for (FoodPreferences fp : fps) {
			res.add(fp.getFoodPreference());
		}
		return res;
	}
	
	public static ArrayList<String> langToString (List<Language> langs) {
		ArrayList<String> res = new ArrayList<>();
		for (Language lang : langs) {
			res.add(lang.getLanguage());
		}
		return res;
	}
}
