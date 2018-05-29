package main.java.com.mishpahug.back.app.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import main.java.com.mishpahug.back.app.ScheduledTasks;
import main.java.com.mishpahug.back.app.api.AddressData;
import main.java.com.mishpahug.back.app.api.CalendarData;
import main.java.com.mishpahug.back.app.api.ErrorData;
import main.java.com.mishpahug.back.app.api.EventData;
import main.java.com.mishpahug.back.app.api.EventDataDone;
import main.java.com.mishpahug.back.app.api.EventDataForSubscriber;
import main.java.com.mishpahug.back.app.api.EventDataDependingOnStatus;
import main.java.com.mishpahug.back.app.api.EventDataShort;
import main.java.com.mishpahug.back.app.api.EventStatusMessageData;
import main.java.com.mishpahug.back.app.api.EventsDataMyEvents;
import main.java.com.mishpahug.back.app.api.ListOfEventsInProgressRequestData;
import main.java.com.mishpahug.back.app.api.MessageData;
import main.java.com.mishpahug.back.app.api.NotificationsDataList;
import main.java.com.mishpahug.back.app.api.PageData;
import main.java.com.mishpahug.back.app.api.StaticfieldsData;
import main.java.com.mishpahug.back.app.api.SuccessfulInvitationMessageData;
import main.java.com.mishpahug.back.app.api.UserDataWithRate;
import main.java.com.mishpahug.back.app.api.UserDataWithoutRate;
import main.java.com.mishpahug.back.app.api.UserDataShort;
import main.java.com.mishpahug.back.app.constants.EmailValidator;
import main.java.com.mishpahug.back.app.dao.Orm;
import main.java.com.mishpahug.back.app.model.*;
import main.java.com.mishpahug.back.app.model.Error;
import static main.java.com.mishpahug.back.app.constants.Constants.*;
import static main.java.com.mishpahug.back.app.constants.DateFormat.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;


@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
@RestController
public class Controller {
	
	EmailValidator validator = new EmailValidator();
	
	@Autowired
	Orm orm;
	
	@Autowired
	private HttpServletRequest request;
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = StaticfieldsData.class) })
	@RequestMapping(value = "/user/staticfields", method = RequestMethod.GET)
	public ResponseEntity<?> getStaticFieldsForProfileForm() {	
		return new ResponseEntity<StaticfieldsData> (orm.createStaticfieldsData(), HttpStatus.OK);
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = StaticfieldsData.class) })
	@RequestMapping(value = "/user/registration", method = RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<?> postRegistrationUser () {	
		String token = request.getHeader("authorization");
		UserDataShort uds = getUserDataShort(token);
		if (uds == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		
		String email = uds.getEmail();
		String pass = uds.getPassword();
		if (pass == null || pass.length() == 0) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		if (email == null || !validator.validate(email)) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		User userToDB = new User(email, pass);
		if (orm.postUser(userToDB) == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, USER_ALREADY_EXIST)), HttpStatus.UNPROCESSABLE_ENTITY);
		return new ResponseEntity<StaticfieldsData> (orm.createStaticfieldsData(), HttpStatus.OK);
	}
	

	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = MessageData.class) })
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<?> postLoginUser () {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_LOGIN_OR_PASSWORD)), HttpStatus.UNAUTHORIZED);	
		
		if (user.getEmptyProfile())
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, EMPTY_PROFILE)), HttpStatus.CONFLICT);

		return new ResponseEntity<UserDataWithRate>(new UserDataWithRate(user), HttpStatus.OK);	
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = MessageData.class) })
	@RequestMapping(value = "/event/creation", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<?> postAddEvent(@RequestBody EventData eventData) {
		String token = request.getHeader("authorization");
		User owner = checkAuth(token);
		if (owner == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		//проверка на отрицательную длительность
		if (eventData.getDuration() <= 0) {
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA + " Duration <= 0")), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		Event event = new Event();
		if (eventData.getTitle() == null || eventData.getTitle().length() == 0)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		event.setTitle(eventData.getTitle());
		
		String dateStarting = LocalDate.now().plusDays(3).toString();
		Date dateS;
		try {
			event.setDate(formatForDateAndTime.parse(eventData.getDate() +" " +eventData.getTime()));
			dateS = formatForDate.parse(dateStarting);
		} catch (ParseException e) {
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		//Пользователь может создать событие не позднее, чем за два дня до даты этого события.
		if (event.getDate().before(dateS)) {
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA + " User can create an event at least two days before the date of this event.")), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		try {
			event.setDuration(eventData.getDuration());
			event.setDateEnding(formatForDateAndTime.parse(addMinutes(event.getDate(), event.getDuration())));
		} catch (ParseException e) {
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		//проверяем, пересекается ли этот ивент юзера с другими его ивентами за этот день.
		if (!orm.checkTimeInterval(owner, event.getDate(), event.getDateEnding()))
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, BUSY_DATE)), HttpStatus.CONFLICT);
		
		event.setOwner(owner);
		event.setDescription(eventData.getDescription());
		Confession conf = checkConfession(eventData.getConfession());
		if (conf == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		event.setConfession(conf);	
		
		Holiday holiday = checkHoliday(eventData.getHoliday());
		if (holiday == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		event.setHoliday(holiday);	
		
		Address ad = checkAddress(eventData.getAddress());
		if (ad == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		
		//Добавляем в БД Address и Location, если их там еще нет
		orm.postLocation(ad.getLocation());
		orm.postAddress(ad);
		event.setAddress(ad);
		
		ArrayList<FoodPreferences> fp = checkFP(eventData.getFood());
		if (fp == null || fp.size() == 0)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		event.setFood(fp);
		event.setStatus("In progress");	
		event.setEventId(null);
		if (orm.postEvent(event) == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, OPERATION_FAILED)), HttpStatus.CONFLICT);
		
		Event eventFromDB = orm.postEvent(event);
		if (eventFromDB == null)
		return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, OPERATION_FAILED)), HttpStatus.CONFLICT);
		
		//проверяем, закончится ли ивент сегодня. Если да - добавляем его в список ивентов, которые закончатся сегодня
		if (formatForDate.format(eventFromDB.getDateEnding()).equals(formatForDate.format(new Date()))) {
			ScheduledTasks.dailyEvents.add(new DailyEvent(eventFromDB.getEventId(), eventFromDB.getDateEnding(), eventFromDB.getStatus()));
		}

		
		return new ResponseEntity<MessageData> (new MessageData(EVENT_WAS_CREATED), HttpStatus.OK);
	}
	

	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = UserDataWithRate.class) })
	@RequestMapping(value = "/user/profile", method = RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<?> postUpdateUserProfile (@RequestBody UserDataWithoutRate userData) {
		String token = request.getHeader("authorization");	
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		if (userData.getFirstName() == null || userData.getFirstName().length() == 0)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);	
		user.setFirstName(userData.getFirstName());
		
		if (userData.getLastName() == null || userData.getLastName().length() == 0)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		user.setLastName(userData.getLastName());
		
		try {
			user.setDateOfBirth(formatForDate.parse(userData.getDateOfBirth()));
		} catch (ParseException e) {
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		Gender gender = checkGender(userData.getGender());
		if (gender == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		user.setGender(gender);
		
		MaritalStatus ms = checkMS(userData.getMaritalStatus());
		if (ms == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		user.setMaritalStatus(ms);
		
		Confession conf = checkConfession(userData.getConfession());
		if (conf == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		user.setConfession(conf);
		
		user.setPictureLink(userData.getPictureLink());
		user.setPhoneNumber(userData.getPhoneNumber());
		ArrayList<FoodPreferences> fp = checkFP(userData.getFoodPreferences());
		if (fp == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		user.setFoodPreferences(fp);
		
		ArrayList<Language> langs = checkLanguages(userData.getLanguages());
		if (langs == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_DATA)), HttpStatus.UNPROCESSABLE_ENTITY);
		user.setLanguages(langs);
		user.setDescription(userData.getDescription());
		user.setAge(calculateAge(user.getDateOfBirth()));
		user.setEmptyProfile(false);
		User userFromDb = orm.putUser(user); 
		if (userFromDb == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, OPERATION_FAILED)), HttpStatus.CONFLICT);
		return new ResponseEntity<UserDataWithRate>(new UserDataWithRate(userFromDb), HttpStatus.OK);			
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = UserDataWithRate.class) })
	@RequestMapping(value = "/user/profile", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<?> getUserProfile () {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		if (user.getEmptyProfile())
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, EMPTY_PROFILE)), HttpStatus.CONFLICT);
		return new ResponseEntity<UserDataWithRate>(new UserDataWithRate(user), HttpStatus.OK);			
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = CalendarData.class) })
	@RequestMapping(value = "/event/calendar/{month}", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header"),
	})
	public ResponseEntity<?> getEventsListForCalendar (@PathVariable Integer month) {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		ArrayList<EventDataShort> myEvents = orm.getMyEventsPerMonth(user.getId(), month);
		ArrayList<EventDataShort> mySubEvents = orm.getEventsWhereUserSubscribedPerMonth(user.getId(), month);
		
		return new ResponseEntity<CalendarData> (new CalendarData(myEvents, mySubEvents), HttpStatus.OK);			
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = EventDataDependingOnStatus.class) })
	@RequestMapping(value = "/event/own/{eventId}", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<?> getMyEventInfo (@PathVariable Long eventId) {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		Event eventFomDB = orm.getEvent(eventId);
		//проверка на то, что такой ивент есть	
		if (eventFomDB == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_PARAMETER)), HttpStatus.UNPROCESSABLE_ENTITY);
		//проверка на то, что запрашивающий является собственником события
		if (!eventFomDB.getOwner().equals(user))
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, NOT_HIS_EVENT)), HttpStatus.CONFLICT);
	return new ResponseEntity<EventDataDependingOnStatus>(new EventDataDependingOnStatus(eventFomDB), HttpStatus.OK);
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = EventDataForSubscriber.class) })
	@RequestMapping(value = "/event/subscribed/{eventId}", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header")})
	public ResponseEntity<?> getSubscribedEventInfo (@PathVariable Long eventId) {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		Event eventFomDB = orm.getEvent(eventId);
		//проверка на то, что такой ивент есть
		if (eventFomDB == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_PARAMETER)), HttpStatus.UNPROCESSABLE_ENTITY);
		
		//проверка что юзер подписан на ивент
		if (!eventFomDB.getSubscribers().contains(user) && !eventFomDB.getInvited().contains(user))
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, NOT_HIS_EVENT)), HttpStatus.CONFLICT);

		EventDataForSubscriber eventData = new EventDataForSubscriber(eventFomDB);
		if (eventData.getStatus().equals("In progress")) {
			eventData.getAddress().setLocation(null);
			eventData.getAddress().setPlace_id(null);
		}
		
		return new ResponseEntity<EventDataForSubscriber>(eventData, HttpStatus.OK);			
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = NotificationsDataList.class) })
	@RequestMapping(value = "/notification/list", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<?> getNotificationsList () {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		return new ResponseEntity<NotificationsDataList>(new NotificationsDataList(orm.getNotificationsList(user.getId())), HttpStatus.OK);			
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = EventsDataMyEvents.class) })
	@RequestMapping(value = "/event/currentlist", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header")})
	public ResponseEntity<?> getMyEventsList () {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		ArrayList<EventDataDependingOnStatus> res = orm.getMyEvents(user.getId());
		if (res == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, OPERATION_FAILED)), HttpStatus.CONFLICT);
		return new ResponseEntity<EventsDataMyEvents>(new EventsDataMyEvents(res), HttpStatus.OK);			
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = EventsDataMyEvents.class) })
	@RequestMapping(value = "/event/historylist", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header")})
	public ResponseEntity<?> getMyEventsHistory () {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		ArrayList<EventDataDone> res = orm.getMyHistory(user.getId());
		if (res == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, OPERATION_FAILED)), HttpStatus.CONFLICT);
		return new ResponseEntity<EventsDataMyEvents>(new EventsDataMyEvents(res), HttpStatus.OK);			
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = EventsDataMyEvents.class) })
	@RequestMapping(value = "/event/participationlist", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header")})
	public ResponseEntity<?> getParticipationList () {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);

		return new ResponseEntity<EventsDataMyEvents>(new EventsDataMyEvents(orm.getEventsWhereUserSubscribed(user)), HttpStatus.OK);			
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = MessageData.class) })
	@RequestMapping(value = "/event/subscription/{eventId}", method = RequestMethod.PUT)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header")})
	public ResponseEntity<?> putSubscribeToEvent (@PathVariable Long eventId) {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		//check event Id + проверка на то, чтобы не мог подписаться на свой ивент
		Event event = orm.getEvent(eventId);
		if (event == null || user.equals(event.getOwner()))
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, RESUBSCRIBING + " Event = null or user - owner.")), HttpStatus.UNPROCESSABLE_ENTITY);
		//check event status - if "pending" subscription is forbidden
		if (event.getStatus().equals("Pending"))
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, RESUBSCRIBING + " Event status - Pending")), HttpStatus.UNPROCESSABLE_ENTITY);

		//проверяем, не приглашен ли юзер куда-либо на эту дату
		List <Event> eventWhereInvited = user.getInvEvents();
		String eventDate = formatForDate.format(event.getDate());
		for (Event ev : eventWhereInvited) {
			if (eventDate.equals(formatForDate.format(ev.getDate()))) {
				return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, RESUBSCRIBING + " The user is already invited to the event on that date!")), HttpStatus.CONFLICT);
			}
		}
		
		List<User> subscribers = event.getSubscribers();
		//проверяем, что юзер не приглашен на этот ивент
		List<User> invited = event.getInvited();
		if (invited != null && invited.contains(user)) {
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, RESUBSCRIBING + " User already invited to this event.")), HttpStatus.CONFLICT);
		}
		
		if (subscribers == null) {
			subscribers = new ArrayList<>();
		}
		
		//проверяем если юзер уже подписан на этот ивент
		if (subscribers.contains(user)) {
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, RESUBSCRIBING + " User already subscribed to this event.")), HttpStatus.CONFLICT);
		}		
		
		
		subscribers.add(user);
		event.setSubscribers(subscribers);
		if (orm.putEvent(event) == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, OPERATION_FAILED)), HttpStatus.CONFLICT);
		return new ResponseEntity<MessageData> (new MessageData("You subscribed to event"), HttpStatus.OK);			
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = MessageData.class) })
	@RequestMapping(value = "/event/unsubscription/{eventId}", method = RequestMethod.PUT)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header")})
	public ResponseEntity<?> putUnsubscribeFromEvent (@PathVariable Long eventId) {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		//check event Id
		Event event = orm.getEvent(eventId);
		if (event == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, WRONG_UNSUBSCRIBTION + " Event = null.")), HttpStatus.CONFLICT);
		
		//check event status - if "pending" unsubscription is forbidden
		if (event.getStatus().equals("Pending"))
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, WRONG_UNSUBSCRIBTION + " Event in status Pending.")), HttpStatus.CONFLICT);
		
		//проверяем, был ли юзер подписан на ивент
		if (!event.getSubscribers().contains(user) && !event.getInvited().contains(user)) {
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, WRONG_UNSUBSCRIBTION + " User is not subscribed to this event")), HttpStatus.CONFLICT);
		}
		
		List<User> users = event.getSubscribers();
		users.remove(user);
		event.setSubscribers(users);
		users = event.getInvited();
		users.remove(user);
		event.setInvited(users);
		users = event.getWereSubscribed();
		users.remove(user);
		event.setWereSubscribed(users);
		if (orm.putEvent(event) == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, OPERATION_FAILED)), HttpStatus.CONFLICT);
		return new ResponseEntity<MessageData> (new MessageData("You unsubscribed from event"), HttpStatus.OK);			
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = MessageData.class) })
	@RequestMapping(value = "/event/vote/{eventId}/{voteCount}", method = RequestMethod.PUT)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header")})
	public ResponseEntity<?> putVoteForEvent (@PathVariable Long eventId, 
											  @PathVariable Double voteCount) {
		if (voteCount < 0. || voteCount > 5.)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_PARAMETER)), HttpStatus.UNPROCESSABLE_ENTITY);
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		//проверка на то, что такой ивент есть и он в статусе Done
		EventWithStatusDone eventFomDB = orm.getEventDone(eventId);
		if (eventFomDB == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_PARAMETER + "Event not in status Done")), HttpStatus.UNPROCESSABLE_ENTITY);
		//проверка на то, что юзер учавствовал в ивенте
		if (!eventFomDB.getInvited().contains(user))
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, REVOTE)), HttpStatus.CONFLICT);
		//удаляем юзера из списка приглашенных
		eventFomDB.getInvited().remove(user);
		if (eventFomDB.getInvited().size() == 0)
			eventFomDB.setInvited(null);
		orm.putEvent(eventFomDB);
		
		User owner = eventFomDB.getOwner();
		owner.changeRating(voteCount);
		orm.putUser(owner);
		
		return new ResponseEntity<MessageData> (new MessageData(VOTE_WAS_ACCEPTED), HttpStatus.OK);	
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = SuccessfulInvitationMessageData.class) })
	@RequestMapping(value = "/event/invitation/{eventId}/{userId}", method = RequestMethod.PUT)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header")
		})
	public ResponseEntity<?> putInviteToEvent (@PathVariable Long eventId,
											   @PathVariable Long userId) {
		String token = request.getHeader("authorization");
		User owner = checkAuth(token);
		if (owner == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		//check event Id
		Event event = orm.getEvent(eventId);
		//проверка на то, что такой ивент есть, и запрашивающий является собственником события
		if (event == null || !event.getOwner().equals(owner))
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_PARAMETER)), HttpStatus.UNPROCESSABLE_ENTITY);
		
		//проверяем, что есть юзер, которого приглашаем
		User userWhoIsInvited = orm.getUser(userId);
		if (userWhoIsInvited == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, INVALID_PARAMETER)), HttpStatus.UNPROCESSABLE_ENTITY);
	
		List<User> subscribers = event.getSubscribers();
		List<User> invited = event.getInvited();
		
		if (invited == null) invited = new ArrayList<>();
		if (invited.contains(userWhoIsInvited)) {
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, INVITE_NOT_OK)), HttpStatus.CONFLICT);
		}
		
		if (subscribers == null || !subscribers.contains(userWhoIsInvited))
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, INVITE_NOT_OK)), HttpStatus.CONFLICT);
		
		
		
		//делаем такую штуку:
		//User can subscribe to multiple events on the same date with the only condition: 
		//if he will be confirmed/invited to one the subscribed events on this date, 
		//the other subscriptions will be cancelled.
		
		List <Event> wasSubscribed = userWhoIsInvited.getWasSubscribed();
		List <Event> eventsSub = userWhoIsInvited.getSubEvents();
		if (wasSubscribed == null) wasSubscribed = new ArrayList<>();
		
		for (Event ev : eventsSub) {
			String date1 = formatForDate.format(ev.getDate());
			String date2 = formatForDate.format(event.getDate());
			if (date1.equals(date2)) {
				wasSubscribed.add(ev);
			}
		}
		
		//не знаю почему, но без этого не работает
		System.out.println("Необходимо очистить, ивентов = " +wasSubscribed.size());
		
		invited.add(userWhoIsInvited);

		//создаем новое оповещение
		Notification not = new Notification();
		not.setDate(new Date());
		not.setTitle("Invitation");
		not.setMessage("You was invited to event '" + event.getTitle() +"'");
		not.setType("System");
		not.setIsRead(false);
		not.setNotOwner(userWhoIsInvited);

		orm.postNotification(not);
		if (orm.putEvent(event) == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, OPERATION_FAILED)), HttpStatus.CONFLICT);
		//удаляем юзера из всех ивентов, на которые он был подписан.
		orm.removeSubscriberFromAllEvents(userWhoIsInvited);
		return new ResponseEntity<SuccessfulInvitationMessageData> (new SuccessfulInvitationMessageData(userWhoIsInvited.getId()), HttpStatus.OK);			
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = EventStatusMessageData.class) })
	@RequestMapping(value = "/event/pending/{eventId}", method = RequestMethod.PUT)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header")})
	public ResponseEntity<?> putChangeEventStatus (@PathVariable Long eventId) {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		//check event Id
		Event event = orm.getEvent(eventId);
		if (event == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_PARAMETER)), HttpStatus.UNPROCESSABLE_ENTITY);
		
		if (event.getStatus().equals("Pending")) {
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, ALREADY_PENDING)), HttpStatus.CONFLICT);
		}
		
		//check events owner
		if (!event.getOwner().equals(user))
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, NOT_HIS_EVENT)), HttpStatus.CONFLICT);
		
		event.setStatus("Pending");
		//очищаем список подписчиков
		event.setSubscribers(null);
		
		if (orm.putEvent(event) == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, OPERATION_FAILED)), HttpStatus.CONFLICT);
		return new ResponseEntity<EventStatusMessageData> (new EventStatusMessageData(event.getEventId(), event.getStatus()), HttpStatus.OK);
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = MessageData.class) })
	@RequestMapping(value = "/notification/isRead/{notificationId}", method = RequestMethod.PUT)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header")})
	public ResponseEntity<?> putNotificationIsRead (@PathVariable Long notificationId) {
		String token = request.getHeader("authorization");
		User user = checkAuth(token);
		if (user == null) 
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(401, WRONG_AUTH)), HttpStatus.UNAUTHORIZED);
		
		Notification notFromDB = orm.getNotification(notificationId);
		if (notFromDB == null)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_PARAMETER)), HttpStatus.UNPROCESSABLE_ENTITY);
		//проверяем "хозяина" оповещения
		if (!notFromDB.getNotOwner().equals(user)) {
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(409, INVALID_NOT_OWNER)), HttpStatus.CONFLICT);
		}
		
		
		notFromDB.setIsRead(true);
		orm.putNotification(notFromDB);

		return new ResponseEntity<MessageData> (new MessageData(NOTIFICATION_WAS_UPDATED), HttpStatus.OK);	
	}
	
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = PageData.class) })
	@RequestMapping(value = "/event/allprogresslist", method = RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "token", required = true, dataType = "string", paramType = "header"),
		@ApiImplicitParam(name = "page", value = "page", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "size", required = true, dataType = "int", paramType = "query")})
	public ResponseEntity<?> postListOfEventsInProgress (
			@RequestBody ListOfEventsInProgressRequestData filters, Integer page, Integer size) {
		Long id = null;
		String token = request.getHeader("authorization");
		if (token != null && token.length() != 0) {
			User user = checkAuth(token);
			if (user != null) {
				id = user.getId();
			}
		}

		if (page == null || size == null || page < 0 || size <= 0)
			return new ResponseEntity<ErrorData> (new ErrorData(new Error(422, INVALID_PARAMETER)), HttpStatus.UNPROCESSABLE_ENTITY);
		return new ResponseEntity<PageData> (orm.getListOfEventsInProgress(id, page, size, filters), HttpStatus.OK);
	}
	
	
	public UserDataShort getUserDataShort (String token) {
		UserDataShort res;
		try {
			String tokenWithoutBase = token.substring(6);
			byte[] tokenByteArray = Base64.getDecoder().decode(tokenWithoutBase.getBytes());
			String encoding = new String(tokenByteArray);
			res = new UserDataShort();
			StringTokenizer stk = new StringTokenizer(encoding,":");
			res.setEmail(stk.nextToken());
			res.setPassword(stk.nextToken());
			
			System.out.println("email = " +res.getEmail() +"   password = " +res.getPassword());
			
		} catch (Exception e) {
			return null;
		}
		return res;
	}
	
	
	public User checkAuth (String authorization) {
		UserDataShort user = getUserDataShort(authorization);
		if (user == null) return null;
		String email = user.getEmail();
		String password = User.md5Apache(user.getPassword());
		
		if (email == null || !validator.validate(email)) return null;
		if (password == null || password.length() == 0) return null;	
		User userFromDB = orm.getUserByEmail(email);
		if (userFromDB == null) return null;	
		String pwd = userFromDB.getPassword();
		if (!password.equals(pwd)) return null;
		return userFromDB;
	}
	
	
	public Confession checkConfession (String conf) {
		if (conf == null || conf.length() == 0) return null;
		Confession confFromDB = orm.getConfession(conf);
		if (confFromDB == null) return null;
		return confFromDB;	
	}
	
	private Gender checkGender (String gender) {
		if (gender == null || gender.length() == 0) return null;
		Gender genderFromDB = orm.getGender(gender);
		if (genderFromDB == null) return null;
		return genderFromDB;	
	}
	
	private MaritalStatus checkMS (String ms) {
		if (ms == null || ms.length() == 0) return null;
		MaritalStatus msFromDB = orm.getMaritalStatus(ms);
		if (msFromDB == null) return null;
		return msFromDB;
	}
	
	
	public Holiday checkHoliday (String holiday) {
		if (holiday == null || holiday.length() == 0) return null;
		Holiday holidayFromDB = orm.getHoliday(holiday);
		if (holidayFromDB == null) return null;
		return holidayFromDB;
	}
	
	public Address checkAddress (AddressData ad) {
		Address address = new Address();
		if (ad.getCity() == null || ad.getCity().length() == 0) return null;
		if (ad.getPlace_id() == null || ad.getPlace_id().length() == 0) return null;
		if (ad.getLocation().getLat() == null || ad.getLocation().getLat() == null) return null;
		address.setCity(ad.getCity());
		address.setPlace_id(ad.getPlace_id());
		Location loc = new Location(ad.getPlace_id(), ad.getLocation().getLat(), ad.getLocation().getLng());
		address.setLocation(loc);
		return address;
	}
	
	public ArrayList<FoodPreferences> checkFP (ArrayList<String> kitchen) {
		ArrayList<FoodPreferences> fpFromDB = (ArrayList<FoodPreferences>) orm.getAllFoodPreferences();
		ArrayList<FoodPreferences> res = new ArrayList<>();
		for (FoodPreferences fp : fpFromDB) {
			if (kitchen.contains(fp.getFoodPreference())) res.add(fp);
		}
		if (res.size() != kitchen.size()) return null;
		return res;	
	}
	
	public ArrayList<Language> checkLanguages (ArrayList<String> langs) {
		ArrayList<Language> langFromDB = (ArrayList<Language>) orm.getAllLanguages();
		ArrayList<Language> res = new ArrayList<>();
		for (Language lang : langFromDB) {
			if (langs.contains(lang.getLanguage())) res.add(lang);
		}
		if (res.size() != langs.size()) return null;
		return res;	
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = SUCCESSFUL_RETRIEVAL, response = String.class) })
	@RequestMapping(value = "/token", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "email", value = "email", required = true, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "password", value = "password", required = true, dataType = "string", paramType = "query"),
		})
	public ResponseEntity<?> getToken (String email, String password) {
		String encoding = Base64.getEncoder().encodeToString((email +":" +password).getBytes());
		String res = "Basic " + encoding;	
		return new ResponseEntity<String> (res, HttpStatus.OK);			
	}

}
