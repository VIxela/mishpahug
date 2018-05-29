package main.java.com.mishpahug.back.app.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DailyEvent {
	@Getter @Setter private Long eventId;
	@Getter @Setter private Date dateEnding;
	@Getter @Setter private String status;
}
