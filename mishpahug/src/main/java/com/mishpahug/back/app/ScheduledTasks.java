package main.java.com.mishpahug.back.app;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

import static main.java.com.mishpahug.back.app.constants.DateFormat.*;
import main.java.com.mishpahug.back.app.dao.Orm;
import main.java.com.mishpahug.back.app.model.Confession;
import main.java.com.mishpahug.back.app.model.DailyEvent;
import main.java.com.mishpahug.back.app.model.FoodPreferences;
import main.java.com.mishpahug.back.app.model.Gender;
import main.java.com.mishpahug.back.app.model.Holiday;
import main.java.com.mishpahug.back.app.model.Language;
import main.java.com.mishpahug.back.app.model.MaritalStatus;
import main.java.com.mishpahug.back.app.model.User;

@Component
public class ScheduledTasks {
	private static boolean checkFillDb=false;
	private static Boolean checkFirstRun = true;
	@Getter @Setter public static ArrayList<DailyEvent> dailyEvents = new ArrayList<>();

	
	@Autowired
	Orm orm;
	
	 @Scheduled(fixedRate = 600000)
	    public void reportCurrentTime() {
	        System.out.println("The time is now {} "+ formatForDateAndTime.format(new Date()));
	        orm.getAllGenders();
	    }
	 
	//в полночь создаем список ивентов, которые в эти сутки закончатся
	@Scheduled(cron = "0 0 0 * * *")
	public void createListOfEventsToDone () {
		//создаем список ивентов, которые в эти сутки закончатся
		ArrayList<DailyEvent> events = (ArrayList<DailyEvent>) orm.checkEventsToDoneToday();
		//добавляем хвосты - ивенты за более ранние числа, которые не перешли в статус Done
		events.addAll(orm.checkEventsToDone());
		ScheduledTasks.dailyEvents = events;	
	}
	
	
	//в час ночи создаем список ивентов, которые надо перевести в статус not done
		@Scheduled(cron = "0 0 1 * * *")
		public void changeEventsToNotDone () {
			ArrayList<DailyEvent> events = (ArrayList<DailyEvent>) orm.checkEventsNotPending();
			for (DailyEvent ev : events) {
				orm.changeEventStatusToNotDone(ev);
			}
		}
		
		
	//каждую минуту проверяем список собыьий, которые надо перевести в статус Done
	@Scheduled(cron = "0 * * * * *")
	public void changeEventsToDone () {
		Date now = new Date();
		Iterator<DailyEvent> iter = dailyEvents.iterator();
		while(iter.hasNext()){
			DailyEvent next = iter.next();
			if(now.after(next.getDateEnding()) || now.equals(next.getDateEnding())) {
				orm.changeEventStatusToDone(next);
				iter.remove();
			}
		}
	}
	
	
	@Scheduled(fixedRate = 86400000) //86400000 = 24 hours
    public void scheduleFixedDelayTask() throws ParseException {
		System.out.println("Starting scheduled tasks");
		if (checkFirstRun) {
			changeEventsToNotDone();
			createListOfEventsToDone();
			orm.sendNotificationsNotDone();
			checkFirstRun = false;
		}
		
        if (!checkFillDb) {        	
            System.out.println("CheckIn min data in DB - " + formatForDateAndTime.format(new Date()));
            checkFillDb=true;
            
            if (orm.getAllFoodPreferences().size() == 0) {
            	System.out.println("Add food preferences...");
            	orm.postNewFoodPreference(new FoodPreferences("vegetarian", null));
            	orm.postNewFoodPreference(new FoodPreferences("any", null));
            	orm.postNewFoodPreference(new FoodPreferences("vegan", null));
            	orm.postNewFoodPreference(new FoodPreferences("kosher", null));
            	System.out.println("Done.");
            }
            
            if (orm.getAllConfessions().size() == 0) {
            	System.out.println("Add confessions...");
            	orm.postNewConfession(new Confession("religious"));
            	orm.postNewConfession(new Confession("non-religious"));
            	System.out.println("Done.");
            }
            
            if (orm.getAllGenders().size() == 0) {
            	System.out.println("Add genders...");
            	orm.postNewGender(new Gender("another"));
            	orm.postNewGender(new Gender("male"));
            	orm.postNewGender(new Gender("female"));
            	System.out.println("Done.");
            }
            
            if (orm.getAllMaritalStatuses().size() == 0) {
            	System.out.println("Add marital statuses...");
            	orm.postNewMaritalStatus(new MaritalStatus("married"));
            	orm.postNewMaritalStatus(new MaritalStatus("unmarried"));
            	orm.postNewMaritalStatus(new MaritalStatus("divorced"));
            	orm.postNewMaritalStatus(new MaritalStatus("widow"));
            	System.out.println("Done.");
            }
            
            if (orm.getAllLanguages().size() == 0) {
            	System.out.println("Add languages...");
            	orm.postLanguage(new Language("Russian"));
            	orm.postLanguage(new Language("English"));
            	orm.postLanguage(new Language("Hebrew"));
            	System.out.println("Done.");
            }     
            
            if (orm.getAllHolidays().size() == 0) {
            	System.out.println("Add holidays...");
            	orm.postNewHoliday(new Holiday("Pesah"));
            	orm.postNewHoliday(new Holiday("Shabbat"));
            	orm.postNewHoliday(new Holiday("Other"));
            	System.out.println("Done.");
            }
            	
         
            	System.out.println("Add users...");
            	User user1 = new User("test@mail.com", "12345");
            	user1.setFirstName("Elsa");
            	user1.setLastName("Fishman");
            	user1.setDateOfBirth(formatForDate.parse("1993-02-20"));
            	user1.setGender(orm.getGender("female"));
            	user1.setMaritalStatus(orm.getMaritalStatus("married"));
            	user1.setConfession(orm.getConfession("religious"));
            	ArrayList<String> pictures = new ArrayList<>();
            	pictures.add("https://i.pinimg.com/originals/23/bf/25/23bf251cab0b742f9257506357e70f38.png");
            	pictures.add("https://www.planwallpaper.com/static/images/Blue-Green-beautiful-nature-21891805-1920-1200_jX7pvvz.jpg");
            	user1.setPictureLink(pictures);
            	user1.setPhoneNumber("0503453183");
            	ArrayList<FoodPreferences> fps = new ArrayList<>();
            	fps.add(orm.getFoodPreferences("vegetarian"));
            	fps.add(orm.getFoodPreferences("kosher"));
            	user1.setFoodPreferences(fps);
            	ArrayList<Language> langs = new ArrayList<>();
            	langs.add(orm.getLanguage("Hebrew"));
            	langs.add(orm.getLanguage("English"));
            	user1.setLanguages(langs);
            	user1.setDescription("I like to arrange home parties and to meet new interesting people.");
            	user1.setRate(0.);
            	user1.setAge(calculateAge(user1.getDateOfBirth()));
            	user1.setEmptyProfile(false);
            	orm.postUser(user1);

            	User user2 = new User("test10@mail.com", "12345");
            	user2.setFirstName("Ivan");
            	user2.setLastName("Pupkin");
            	user2.setDateOfBirth(formatForDate.parse("1976-04-11"));
            	user2.setGender(orm.getGender("male"));
            	user2.setMaritalStatus(orm.getMaritalStatus("unmarried"));
            	user2.setConfession(orm.getConfession("religious"));
            	ArrayList<String> pictures2 = new ArrayList<>();
            	pictures2.add("https://img.joinfo.ua/i/2017/10/59ec7a6b9bb69.jpg");
            	pictures2.add("https://travel.rambler.ru/media/original_images/4e930e7d73c6a.jpg");
            	user2.setPictureLink(pictures2);
            	user2.setPhoneNumber("0503393020");
            	ArrayList<FoodPreferences> fps2 = new ArrayList<>();
            	fps2.add(orm.getFoodPreferences("any"));
            	user2.setFoodPreferences(fps2);
            	ArrayList<Language> langs2 = new ArrayList<>();
            	langs2.add(orm.getLanguage("Russian"));
            	langs2.add(orm.getLanguage("English"));
            	user2.setLanguages(langs2);
            	user2.setDescription("I like to arrange home parties and to meet new interesting people.");
            	user2.setRate(0.);
            	user2.setAge(calculateAge(user2.getDateOfBirth()));
            	user2.setEmptyProfile(false);
            	orm.postUser(user2);
          
            	User user3 = new User("test2@mail.com", "12345");
            	user3.setFirstName("Arnold");
            	user3.setLastName("Schwarzenegger");
            	user3.setDateOfBirth(formatForDate.parse("1947-06-30"));
            	user3.setGender(orm.getGender("male"));
            	user3.setMaritalStatus(orm.getMaritalStatus("married"));
            	user3.setConfession(orm.getConfession("religious"));
            	ArrayList<String> pictures3 = new ArrayList<>();
            	pictures3.add("https://st.kp.yandex.net/images/actor_iphone/iphone360_6264.jpg");
            	pictures3.add("http://irecommend.ru/sites/default/files/imagecache/copyright1/user-images/308844/dN82LRlDGrtYtz8GTlhg.jpg");
            	user3.setPictureLink(pictures3);
            	user3.setPhoneNumber("0503453183");
            	ArrayList<FoodPreferences> fps3 = new ArrayList<>();
            	fps3.add(orm.getFoodPreferences("vegetarian"));
            	user3.setFoodPreferences(fps3);
            	ArrayList<Language> langs3 = new ArrayList<>();
            	langs3.add(orm.getLanguage("Hebrew"));
            	langs3.add(orm.getLanguage("English"));
            	user3.setLanguages(langs3);
            	user3.setDescription("I like to arrange home parties and to meet new interesting people.");
            	user3.setRate(0.);
            	user3.setAge(calculateAge(user3.getDateOfBirth()));
            	user3.setEmptyProfile(false);
            	orm.postUser(user3);
            	
            	System.out.println("Done.");
            
        }
	}          
}
