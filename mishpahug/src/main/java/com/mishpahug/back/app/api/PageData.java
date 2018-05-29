package main.java.com.mishpahug.back.app.api;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.java.com.mishpahug.back.app.model.Event;

@NoArgsConstructor
@AllArgsConstructor
public class PageData {
	@Getter @Setter private List <EventDataForListInProgress> content;
	@Getter @Setter private Long totalElements;
	@Getter @Setter private Integer totalPages;
	@Getter @Setter private Integer size;
	@Getter @Setter private Integer number;
	@Getter @Setter private Integer numberOfElements;
	@Getter @Setter private Boolean first;
	@Getter @Setter private Boolean last;
	@Getter @Setter private Sort sort;
}
