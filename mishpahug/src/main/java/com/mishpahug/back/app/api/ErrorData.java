package main.java.com.mishpahug.back.app.api;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.java.com.mishpahug.back.app.model.Error;

@NoArgsConstructor
@AllArgsConstructor
public class ErrorData {
	
	@Getter @Setter private Integer code;
	@Getter @Setter private String message;
	
	public ErrorData(Error error) {
		this.code = error.getCode();
		this.message = error.getMessage();
	}
}
