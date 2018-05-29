package main.java.com.mishpahug.back.app.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import static main.java.com.mishpahug.back.app.constants.DateFormat.*;

@NoArgsConstructor
@AllArgsConstructor
public class NotificationData {
	@Getter @Setter private Long notificationId;
	@Getter @Setter private String title;
	@Getter @Setter private String message;
	@Getter @Setter private String date;
	@Getter @Setter private String type;
	@Getter @Setter private Boolean isRead;
	
	public NotificationData(main.java.com.mishpahug.back.app.model.Notification not) {
		this.notificationId = not.getNotificationId();
		this.title = not.getTitle();
		this.message = not.getMessage();
		this.date = formatForDate.format(not.getDate());
		this.type = not.getType();
		this.isRead = not.getIsRead();
	}
}
