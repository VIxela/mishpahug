package main.java.com.mishpahug.back.app.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Error {
	
	@Getter @Setter public int code;
	@Getter @Setter public String message;
}
