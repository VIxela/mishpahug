package main.java.com.mishpahug.back.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="notification")
public class Notification {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	@Getter @Setter private Long notificationId;
	@Getter @Setter private String title;
	@Getter @Setter private String message;
	@Getter @Setter private Date date;
	@Getter @Setter private String type;
	@Getter @Setter private Boolean isRead;
	
	@ManyToOne
	@Getter @Setter User notOwner;
}
