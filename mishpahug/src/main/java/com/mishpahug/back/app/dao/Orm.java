package main.java.com.mishpahug.back.app.dao;

import static main.java.com.mishpahug.back.app.constants.DateFormat.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.transaction.Transactional;
import org.springframework.stereotype.Component;

import main.java.com.mishpahug.back.app.api.EventDataDependingOnStatus;
import main.java.com.mishpahug.back.app.api.EventDataDone;
import main.java.com.mishpahug.back.app.api.EventDataForListInProgress;
import main.java.com.mishpahug.back.app.api.EventDataForSubscriber;
import main.java.com.mishpahug.back.app.api.EventDataShort;
import main.java.com.mishpahug.back.app.api.ListOfEventsInProgressRequestData;
import main.java.com.mishpahug.back.app.api.NotificationData;
import main.java.com.mishpahug.back.app.api.PageData;
import main.java.com.mishpahug.back.app.api.StaticfieldsData;
import main.java.com.mishpahug.back.app.model.Address;
import main.java.com.mishpahug.back.app.model.Confession;
import main.java.com.mishpahug.back.app.model.DailyEvent;
import main.java.com.mishpahug.back.app.model.Event;
import main.java.com.mishpahug.back.app.model.EventWithStatusDone;
import main.java.com.mishpahug.back.app.model.FoodPreferences;
import main.java.com.mishpahug.back.app.model.Gender;
import main.java.com.mishpahug.back.app.model.Holiday;
import main.java.com.mishpahug.back.app.model.Language;
import main.java.com.mishpahug.back.app.model.Location;
import main.java.com.mishpahug.back.app.model.MaritalStatus;
import main.java.com.mishpahug.back.app.model.Notification;
import main.java.com.mishpahug.back.app.model.User;

@Component
public class Orm {

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	EntityManager em;
	
	public StaticfieldsData createStaticfieldsData () {
		StaticfieldsData res = new StaticfieldsData();
		res.setConfession(getAllConfessionToString());
		res.setGender(getAllGendersToString());
		res.setMaritalStatus(getAllMaritalStatusesToString());
		res.setFoodPreferences(getAllFoodPreferencesToString());
		res.setLanguages(getAllLanguagesToString());
		res.setHoliday(getAllHolidaysToString());
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public Collection <FoodPreferences> getAllFoodPreferences () {
		return (Collection <FoodPreferences>) em.createQuery("SELECT fp FROM FoodPreferences fp").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection <String> getAllFoodPreferencesToString () {
		return (Collection <String>) em.createQuery("SELECT fp.foodPreference FROM FoodPreferences fp").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection <Confession> getAllConfessions () {
		return (Collection <Confession>) em.createQuery("SELECT c FROM Confession c").getResultList();
	}
	
	public Confession getConfession (String conf) {
		return em.find(Confession.class, conf);
	}
	
	@SuppressWarnings("unchecked")
	public Collection <String> getAllConfessionToString () {
		return (Collection <String>) em.createQuery("SELECT c.confession FROM Confession c").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection <Holiday> getAllHolidays () {
		return (Collection <Holiday>) em.createQuery("SELECT h FROM Holiday h").getResultList();
	}
	
	public Holiday getHoliday (String holiday) {
		return em.find(Holiday.class, holiday);
	}
	
	public FoodPreferences getFoodPreferences (String fp) {
		return em.find(FoodPreferences.class, fp);
	}
	
	public Language getLanguage (String lang) {
		return em.find(Language.class, lang);
	}
	
	@SuppressWarnings("unchecked")
	public Collection <String> getAllHolidaysToString () {
		return (Collection <String>) em.createQuery("SELECT h.holiday FROM Holiday h").getResultList();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public Collection <Gender> getAllGenders () {
		return (Collection <Gender>) em.createQuery("SELECT g FROM Gender g").getResultList();
	}
	
	public Gender getGender (String gender) {
		return em.find(Gender.class, gender);
	}
	
	@SuppressWarnings("unchecked")
	public Collection <String> getAllGendersToString () {
		return (Collection <String>) em.createQuery("SELECT g.gender FROM Gender g").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection <MaritalStatus> getAllMaritalStatuses () {
		return (Collection <MaritalStatus>) em.createQuery("SELECT ms FROM MaritalStatus ms").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection <String> getAllMaritalStatusesToString () {
		return (Collection <String>) em.createQuery("SELECT ms.maritalStatus FROM MaritalStatus ms").getResultList();
	}
	
	public MaritalStatus getMaritalStatus (String ms) {
		return em.find(MaritalStatus.class, ms);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Language> getAllLanguages () {
		return (Collection <Language>) em.createQuery("SELECT l FROM Language l").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<String> getAllLanguagesToString () {
		return (Collection <String>) em.createQuery("SELECT l.language FROM Language l").getResultList();
	}
	
	public Event getEvent (Long id) {
		return em.find(Event.class, id);
	}
	
	public EventWithStatusDone getEventDone (Long id) {
		return em.find(EventWithStatusDone.class, id);
	}
	
	@Transactional
	public FoodPreferences postNewFoodPreference (FoodPreferences fp) {
		if (em.find(FoodPreferences.class, fp.getFoodPreference()) != null) return null;
		em.persist(fp);
		em.flush();
		return fp;
	}
	
	@Transactional
	public Confession postNewConfession (Confession conf) {
		if (em.find(Confession.class, conf.getConfession()) != null) return null;
		em.persist(conf);
		em.flush();
		return conf;
	}
	
	@Transactional
	public Holiday postNewHoliday (Holiday holiday) {
		if (em.find(Holiday.class, holiday.getHoliday()) != null) return null;
		em.persist(holiday);
		em.flush();
		return holiday;
	}
	
	@Transactional
	public Gender postNewGender (Gender gender) {
		if (em.find(Gender.class, gender.getGender()) != null) return null;
		em.persist(gender);
		em.flush();
		return gender;
	}
	
	@Transactional
	public MaritalStatus postNewMaritalStatus (MaritalStatus ms) {
		if (em.find(MaritalStatus.class, ms.getMaritalStatus()) != null) return null;
		em.persist(ms);
		em.flush();
		return ms;
	}
	
	@Transactional
	public User postUser (User user) {
		//проверка есть такоей юзер или нет
		if (getUserByEmail(user.getEmail()) != null) return null;
		em.persist(user);
		em.flush();
		return user;	
	}
	
	public User getUser (Long id) {
		return em.find(User.class, id);
	}
	
	public User getUserByEmail (String email) {
		String req = "SELECT u FROM User u WHERE u.email = '" +email +"'";
		User userFromDB;
		try {
			userFromDB = (User) em.createQuery(req).getResultList().get(0);
			return userFromDB;	
		} catch (Exception e) {
			return null;
		}
	}
	
	@Transactional
	public User putUser (User user) {
		User userFromDB = em.find(User.class, user.getId());
		userFromDB = user;
		em.merge(userFromDB);
		em.flush();
		return userFromDB;
	}
	
	@Transactional
	public Event putEvent (Event ev) {
		Event eventFromDB = em.find(Event.class, ev.getEventId());
		eventFromDB = ev;
		em.merge(eventFromDB);
		em.flush();
		return eventFromDB;
	}
	
	@Transactional
	public EventWithStatusDone putEventDone (EventWithStatusDone ev) {
		EventWithStatusDone eventFromDB = em.find(EventWithStatusDone.class, ev.getEventId());
		eventFromDB = ev;
		em.merge(eventFromDB);
		em.flush();
		return eventFromDB;
	}
	
	@Transactional
	public EventWithStatusDone putEvent (EventWithStatusDone ev) {
		EventWithStatusDone eventFromDB = em.find(EventWithStatusDone.class, ev.getEventId());
		eventFromDB = ev;
		em.merge(eventFromDB);
		em.flush();
		return eventFromDB;
	}
	
	@Transactional
	public Language postLanguage (Language language) {
		if (em.find(Language.class, language.getLanguage()) != null) return null;
		em.persist(language);
		em.flush();
		return language;
	}
	
	@Transactional
	public Event postEvent (Event event) {
		em.persist(event);
		em.flush();
		return event;
	} 	
	
	@Transactional
	public Address postAddress (Address address) {
		if (em.find(Address.class, address.getPlace_id()) != null) return null;
		em.persist(address);
		em.flush();
		return address;
	}
	
	@Transactional
	public Location postLocation (Location location) {
		if (em.find(Location.class, location.getId()) != null) return null;
		em.persist(location);
		em.flush();
		return location;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList <EventDataDependingOnStatus> getMyEvents (Long userId) {	
		String req = "SELECT e FROM Event e WHERE e.owner = '" + userId +"'"; 
		req += " AND (e.status = 'In progress' OR e.status = 'Pending')";
		req += " ORDER BY e.date ASC";
		ArrayList<Event> eventsFromDB = (ArrayList<Event>) em.createQuery(req).getResultList();
		ArrayList<EventDataDependingOnStatus> res = new ArrayList<>();
		for (Event event : eventsFromDB) {
			EventDataDependingOnStatus edds = new EventDataDependingOnStatus(event);
			edds.setAddress(null);
			res.add(edds);
		}	
	return res;		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList <EventDataDone> getMyHistory (Long userId) {	
		String req = "SELECT e FROM EventWithStatusDone e WHERE e.owner = '" + userId +"'"; 
		req += " AND e.status = 'Done'";
		req += " ORDER BY e.date DESC";
		ArrayList<EventWithStatusDone> eventsFromDB = (ArrayList<EventWithStatusDone>) em.createQuery(req).getResultList();
		ArrayList<EventDataDone> res = new ArrayList<>();
		for (EventWithStatusDone event : eventsFromDB) {
			res.add(new EventDataDone(event));
		}	
	return res;		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList <EventDataShort> getMyEventsPerMonth (Long id, Integer month) {	
		int year = getYear();
		String dateToReq = (month < 10) ?  year+"-0"+month+"-%" : year+"-"+month+"-%";
		String req = "SELECT e FROM Event e WHERE e.owner = '" + id +"' AND e.date LIKE '" +dateToReq + "'"; 
		req += " ORDER BY e.date";
		//req += " AND (e.status = 'In progress' OR e.status = 'Pending')";
		ArrayList<Event> eventsFromDB = (ArrayList<Event>) em.createQuery(req).getResultList();
		ArrayList<EventDataShort> res = new ArrayList<>();
		for (Event event : eventsFromDB) {
			res.add(new EventDataShort(event.getEventId(), event.getTitle(), formatForDate.format(event.getDate()), formatForTime.format(event.getDate()), event.getStatus()));
		}	
	return res;		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<EventDataShort> getEventsWhereUserSubscribedPerMonth (Long id, Integer month) {	
		int year = getYear();
		//подписался
		String dateToReq = (month < 10) ?  year+"-0"+month+"-%" : year+"-"+month+"-%";
		String req1 = "Select e FROM User u JOIN u.subEvents e where u.Id = '" +id +"' AND e.date LIKE '" +dateToReq + "'"; 
		req1 += " AND (e.status = 'In progress' OR e.status = 'Pending')";
		req1 += " ORDER BY e.date";
		Collection<Event> mySubscribedEvents = em.createQuery(req1).getResultList();
		//приглашен
		String req2 = "Select e FROM User u JOIN u.invEvents e where u.Id = '" +id +"' AND e.date LIKE '" +dateToReq + "'"; 
		req2 += " AND (e.status = 'In progress' OR e.status = 'Pending')";
		req2 += " ORDER BY e.date";
		Collection<Event> myInvitedEvents = em.createQuery(req2).getResultList();
		
		ArrayList<EventDataShort> res = new ArrayList<>();
		for (Event event : mySubscribedEvents) {
			res.add(new EventDataShort(event.getEventId(), event.getTitle(), formatForDate.format(event.getDate()), formatForTime.format(event.getDate()), event.getStatus()));
		}		
		for (Event event : myInvitedEvents) {
			res.add(new EventDataShort(event.getEventId(), event.getTitle(), formatForDate.format(event.getDate()), formatForTime.format(event.getDate()), event.getStatus()));
		}
	return res;		
	}
	
	@Transactional
	public EventWithStatusDone postEventDone (EventWithStatusDone eventDone) {
		if (em.find(EventWithStatusDone.class, eventDone.getEventId()) != null) return null;
		em.persist(eventDone);
		return eventDone;
	} 
	
	@Transactional
	public Event removeEvent (Event event) {
		em.remove(event);
		em.flush();
		return event;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<EventDataForSubscriber> getEventsWhereUserSubscribed (User user) {	
		//подписался
		String req1 = "Select e FROM User u JOIN u.subEvents e where u.Id = '" +user.getId() + "'"; 
		Collection<Event> mySubscribedEvents = em.createQuery(req1).getResultList();
		//приглашен
		String req2 = "Select e FROM User u JOIN u.invEvents e where u.Id = '" +user.getId() +"'"; 
		Collection<Event> myInvitedEvents = em.createQuery(req2).getResultList();
		
		//ивенты со статусом Done, но за которые юзер не проголосовал
		String req3 = "Select e FROM User u JOIN u.invEventsDone e where u.Id = '" +user.getId() +"' AND e.status = 'Done'"; 
		Collection<EventWithStatusDone> myEventsWithStatusDone = em.createQuery(req3).getResultList();
		ArrayList<EventDataForSubscriber> res = new ArrayList<>();
		for (Event event : mySubscribedEvents) {
			EventDataForSubscriber eventData = new EventDataForSubscriber(event);
			if (eventData.getStatus().equals("In progress")) {
				eventData.getAddress().setLocation(null);
				eventData.getAddress().setPlace_id(null);
				eventData.getOwner().setPhoneNumber(null);
			}
			
			res.add(eventData);
		}		
		for (Event event : myInvitedEvents) {
			EventDataForSubscriber eventData = new EventDataForSubscriber(event);
			if (eventData.getStatus().equals("In progress")) {
				eventData.getAddress().setLocation(null);
				eventData.getAddress().setPlace_id(null);
				eventData.getOwner().setPhoneNumber(null);
			}
			res.add(eventData);
		}
		
		for (EventWithStatusDone event : myEventsWithStatusDone) {
			EventDataForSubscriber eventData = new EventDataForSubscriber(event);
			eventData.setAddress(null);
			eventData.getOwner().setPhoneNumber(null);
			res.add(eventData);
		}
		//сортируем по дате, по возрастанию
		Collections.sort(res, new Comparator<EventDataForSubscriber>() {
			@Override
			public int compare(EventDataForSubscriber o1, EventDataForSubscriber o2) {
				return o1.getDate().compareToIgnoreCase(o2.getDate());
			}
		});
	return res;		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<NotificationData> getNotificationsList (Long id) {	
		ArrayList<Notification> nots = (ArrayList<Notification>) em.createQuery(
				"SELECT n from User u JOIN u.notifications n Where u.Id = " +id +" ORDER BY n.isRead, n.date DESC").getResultList();
		ArrayList<NotificationData> notsData = new ArrayList<>();
		for (Notification not : nots) {
			notsData.add(new NotificationData(not));
		}
		return notsData;
	}
	
	public Notification getNotification (Long notId) {
		return em.find(Notification.class, notId);
	}
	
	@Transactional
	public Notification postNotification (Notification not) {
		em.persist(not);
		return not;
	}
	
	@Transactional
	public Notification putNotification (Notification not) {
		Notification notFromDB = em.find(Notification.class, not.getNotificationId());
		notFromDB = not;
		em.merge(notFromDB);
		em.flush();
		return notFromDB;
	}
	

    @SuppressWarnings("unchecked")
	public PageData getListOfEventsInProgress (Long id, int pageStart, int countOnPage, ListOfEventsInProgressRequestData filters){ 
      	
    	StringBuilder queryS = new StringBuilder();
    	queryS.append("SELECT DISTINCT COUNT(e) From Event e Where e.status = 'In progress' ");
    	//Добавляем фильтры
    	if (filters.getFilters().getFood() != null && filters.getFilters().getFood().length() != 0 &&  !filters.getFilters().getFood().equals("any")) {
    		if (getFoodPreferences(filters.getFilters().getFood()) != null) {
    			queryS.replace(37, 68, " join e.food f Where e.status = 'In progress' ");
    			queryS.append(" AND f.foodPreference LIKE '" +filters.getFilters().getFood()+"' ");
    		}
    	}

    	if (filters.getFilters().getHolidays() != null && filters.getFilters().getHolidays().length() != 0) {
    		if (getHoliday(filters.getFilters().getHolidays()) != null) {
    			queryS.append(" AND e.holiday = '" +filters.getFilters().getHolidays()+"' ");
    		}
    	}
    			
    	if (filters.getFilters().getConfession() != null && filters.getFilters().getConfession().length() != 0) {
    		if (getConfession(filters.getFilters().getConfession()) != null) {
    			queryS.append(" AND e.confession = '" +filters.getFilters().getConfession()+"' ");	
    		}
    	}
    	
    	
    	if (filters.getFilters().getDateFrom() != null && filters.getFilters().getDateFrom().length() != 0) {
    		if (isDateValid(filters.getFilters().getDateFrom())) {
    			queryS.append(" AND e.date >= '" +filters.getFilters().getDateFrom() +"' ");
    		}
    	}
    	
    	if (filters.getFilters().getDateTo() != null && filters.getFilters().getDateTo().length() != 0) {
    		if (isDateValid(filters.getFilters().getDateTo())) {
    			queryS.append(" AND e.date <= '" + filters.getFilters().getDateTo() +"' ");
    		}		
    	}

    	if (filters.getLocation().getLat() != null  &&
        		filters.getLocation().getLng() != null &&
        		filters.getLocation().getRadius() != null) {
    		
    		Double lat = filters.getLocation().getLat();    //широта - const
    		Double lng = filters.getLocation().getLng();	//долгота    	
    		Double radius = filters.getLocation().getRadius() / 111111;
    		Double cos = Math.abs(Math.cos(lng));
    		Double km = 111300 * cos;
    		Double radius2 = filters.getLocation().getRadius() / km;
    		Double var1=lat -radius;
    		Double var2 = lat + radius;
    		Double var3 = lng - radius2;
    		Double var4 = lng + radius2;	
    		queryS.append(" AND e.address.location.lat BETWEEN " +var1 +" AND " +var2 +" AND e.address.location.lng BETWEEN " +var3 +" AND " +var4);
    	}
    	
    	if (id != null) queryS.append(" AND e.owner != '" + id + "' ");
    
    	List <Event> res = new ArrayList<>();
    	TypedQuery<Long> testQuery = em.createQuery(queryS.toString(), Long.class);
    	Long countEvents = testQuery.getSingleResult();
    	if (countEvents > pageStart * countOnPage) {
    		String finalQuery = queryS.replace(0, 24, "SELECT e").toString();
    		finalQuery += " ORDER BY e.date ASC";
    		
    		System.out.println("request - " +finalQuery);
    		
        	Query query = em.createQuery(finalQuery).setFirstResult(pageStart * countOnPage);
    		
        	if (countOnPage > 0) {
    			query.setMaxResults(countOnPage);
    		}
        	res = query.getResultList();
    	}
    	
    	ArrayList<EventDataForListInProgress> resFinal = new ArrayList<>();
    	for (Event event : res) {
    		resFinal.add(new EventDataForListInProgress(event));
    	}
    	int totalPages = (int) Math.ceil((double)countEvents / (double)countOnPage);
    	PageData pd = new PageData(resFinal,
    			countEvents, 
    			totalPages, 
    			countOnPage, 
    			pageStart, 
    			res.size(), 
    			(pageStart == 0) ? true : false, 
    			(pageStart == totalPages-1) ? true : false, 
    		null);
        return pd;
    }
    
    @Transactional
	public EventWithStatusDone changeEventStatusToDone (DailyEvent dailyEvent) {
		Event event = getEvent(dailyEvent.getEventId());
		if (event.getStatus().equals("Pending")) {
			event.setStatus("Done");
		}
		else {
			return changeEventStatusToNotDone(dailyEvent);
		}
			
		EventWithStatusDone eventD = new EventWithStatusDone(event);
		postEventDone(eventD);
		for (User user : eventD.getInvited()) {
			//создаем новое оповещение
			Notification not = new Notification();
			not.setDate(new Date());
			not.setTitle("Vote event");
			not.setMessage("You should vote for the event '" + eventD.getTitle() +"'");
			not.setType("System");
			not.setIsRead(false);
			not.setNotOwner(user);
			postNotification(not);
		}	
		removeEvent(event);
		em.flush();
		return eventD;
	}

    
    @SuppressWarnings("unchecked")
	public Collection <DailyEvent> checkEventsToDoneToday () {
   		Date date = new Date();
       	//выбираем все ивенты которые закончатся сегодня
       	String req = "SELECT e FROM Event e WHERE (e.dateEnding BETWEEN '" +formatForDate.format(date) 
       		+ " 00:00:00.00' AND '" +formatForDate.format(date) +" 23:59:59.999') ";
       	ArrayList<Event> myEvents = (ArrayList<Event>) em.createQuery(req).getResultList();
       
       	ArrayList<DailyEvent> res = new ArrayList<>();
       	for (Event event : myEvents) {
       		res.add(new DailyEvent(event.getEventId(), event.getDateEnding(), event.getStatus()));
       	}  
   		return res;
   	}
    
    @SuppressWarnings("unchecked")
	public Boolean checkTimeInterval (User user, Date date, Date dateEnding) {
    	//выбираем все ивенты юзера за этот день
    	String req = "SELECT e FROM Event e WHERE e.owner = '" + user.getId() 
		+"'  AND ((e.date BETWEEN '" +formatForDate.format(date) + " 00:00:00.00' AND '" +formatForDate.format(dateEnding) +" 23:59:59.999') "
		+ " OR (e.dateEnding BETWEEN '" +formatForDate.format(date) + " 00:00:00.00' AND '" +formatForDate.format(dateEnding) +" 23:59:59.999')) ";
    	ArrayList<Event> myEvents = (ArrayList<Event>) em.createQuery(req).getResultList();
    	for (Event event : myEvents) {
    		if (isOverlapping(event.getDate(), event.getDateEnding(), date, dateEnding)) return false;
    	}	
		return true;
	}
    
    public static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return !start1.after(end2) && !start2.after(end1);
    }
    
    @Transactional
	public void removeSubscriberFromAllEvents (User user) {
		for (Event event : user.getWasSubscribed()) {
			event.getSubscribers().remove(user);
			event.getWereSubscribed().add(user);
			putEvent(event);
		}
	}
    
    @SuppressWarnings("unchecked")
	public  Collection <DailyEvent> checkEventsNotPending () {
    	String tomorrow = LocalDate.now().plusDays(1).toString();
    	//выбираем все ивенты юзера которые он не перевел в пендинг
    	String req = "SELECT e FROM Event e WHERE (e.date BETWEEN '" +tomorrow + " 00:00:00.00' AND '" +tomorrow +" 23:59:59.999') ";
    	req += " AND e.status = 'In progress'";
    	ArrayList<Event> myEvents = (ArrayList<Event>) em.createQuery(req).getResultList();
    	
    	ArrayList<DailyEvent> res = new ArrayList<>();
       	for (Event event : myEvents) {
       		res.add(new DailyEvent(event.getEventId(), event.getDateEnding(), event.getStatus()));
       	}  
   		return res;
	}
    
    @Transactional
	public EventWithStatusDone changeEventStatusToNotDone (DailyEvent dailyEvent) {
    	Event event = getEvent(dailyEvent.getEventId());
    	List<User> invited = event.getInvited();
    	if (invited != null && invited.size() != 0) {
    		for (User user : invited) {
    			//создаем новое оповещение
    			Notification not = new Notification();
    			not.setDate(new Date());
    			not.setTitle("Event canceled");
    			not.setMessage("Sorry, event " +event.getTitle() +" has been canceled. Event organizer was automatically set a minimum score. We apologize for any inconvenience.");
    			not.setType("System");
    			not.setIsRead(false);
    			not.setNotOwner(user);
    			postNotification(not);
    		}
    		User owner = event.getOwner();
    		owner.changeRatingUsers(invited.size());
    		invited.clear();
    		event.setInvited(invited);
    		event.setOwner(owner);
    	}
		event.setStatus("Not done");
		
		EventWithStatusDone eventD = new EventWithStatusDone(event);
		postEventDone(eventD);
		removeEvent(event);
		em.flush();
		return eventD;
	}
    
    @SuppressWarnings("unchecked")
 	public Collection <DailyEvent> checkEventsToDone () {
    		//выбираем все ивенты которые уже давно должны были перейти в статус Done
        	String req = "SELECT e FROM Event e WHERE e.dateEnding < date(now()) ";
        	ArrayList<Event> myEvents = (ArrayList<Event>) em.createQuery(req).getResultList();
        
        	ArrayList<DailyEvent> res = new ArrayList<>();
        	for (Event event : myEvents) {
        		res.add(new DailyEvent(event.getEventId(), event.getDateEnding(), event.getStatus()));
        	}
    		return res;
    	}	
    
    //временно - подчищает хвосты. Ивенты нот дан у которых есть приглашенные - отправляет им оповещения, очищает список приглашенных.
    @Transactional
    public void sendNotificationsNotDone () {
    	//выбираем все ивенты которые уже  not Done
    	String req = "SELECT e FROM EventWithStatusDone e WHERE e.status = 'Not done' ";
    	@SuppressWarnings("unchecked")
		ArrayList<EventWithStatusDone> events = (ArrayList<EventWithStatusDone>) em.createQuery(req).getResultList();
    	for (EventWithStatusDone event : events) {
    		List<User> invited = event.getInvited();
    		if (invited != null && invited.size() != 0) {
    			for (User user : invited) {
        			//создаем новое оповещение
        			Notification not = new Notification();
        			not.setDate(new Date());
        			not.setTitle("Event canceled");
        			not.setMessage("Sorry, event " +event.getTitle() +" has been canceled. Event organizer was automatically set a minimum score. We apologize for any inconvenience.");
        			not.setType("System");
        			not.setIsRead(false);
        			not.setNotOwner(user);
        			postNotification(not);
        		}
        		User owner = event.getOwner();
        		owner.changeRatingUsers(invited.size());
        		invited.clear();
        		event.setInvited(invited);
        		event.setOwner(owner);
        		putEventDone(event);
    		}
    	}
    }
}
