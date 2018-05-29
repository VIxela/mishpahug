package main.java.com.mishpahug.back.app.api;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
public class NotificationsDataList {
	@Getter @Setter private ArrayList<NotificationData> notifications;
}
