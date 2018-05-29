package main.java.com.mishpahug.back.app.api;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventsDataMyEvents {
	@Getter @Setter private ArrayList<?> events;

}