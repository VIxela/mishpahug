package main.java.com.mishpahug.back.app.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
public class SuccessfulInvitationMessageData {	
	@Getter @Setter private Long userId;
	@Getter @Setter private Boolean isInvited;
	
	public SuccessfulInvitationMessageData(Long userId) {
		this.userId = userId;
		this.isInvited = true;
	}	
}
