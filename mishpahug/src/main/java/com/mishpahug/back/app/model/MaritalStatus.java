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
@EqualsAndHashCode
@Entity
public class MaritalStatus {
	@Id
	@Getter @Setter private String maritalStatus;
}
