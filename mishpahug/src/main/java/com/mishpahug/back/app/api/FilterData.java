package main.java.com.mishpahug.back.app.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
public class FilterData {
	@Getter @Setter private String dateFrom;
	@Getter @Setter private String dateTo;
	@Getter @Setter private String holidays;
	@Getter @Setter private String confession;
	@Getter @Setter private String food;
}
