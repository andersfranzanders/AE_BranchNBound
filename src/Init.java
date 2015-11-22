import java.util.ArrayList;
import java.util.List;

import wrappers.Category;
import wrappers.Day;
import wrappers.Hour;
import wrappers.Song;
import wrappers.Song.Genre;
import wrappers.Song.Tempo;

public class Init {

	int hoursOfDay = 4;

	public void start() {

		//NumberForCats: 5, 14, 18, 33
		Category powerCat = new Category("Power Cat", 2, 4);
		powerCat.fillCategoryWithRandomSongs();

		Category newCat = new Category("New Cat", 4, 6);
		newCat.fillCategoryWithRandomSongs();

		Category ninetiesCat = new Category("90s", 6, 8);
		ninetiesCat.fillCategoryWithRandomSongs();

		Category eightiesCat = new Category("80s", 8, 10);
		eightiesCat.fillCategoryWithRandomSongs();

//		System.out.println(powerCat);
//		System.out.println(newCat);
//		System.out.println(ninetiesCat);
//		System.out.println(eightiesCat);
		
		Day day = new Day(hoursOfDay);

		for (int i = 0; i < hoursOfDay; i++) {
			Hour dayHour = generateDayHour(powerCat, newCat, ninetiesCat, eightiesCat);
			day.addHourToList(dayHour);
		}
	//	System.out.println(day.getListOfHours().get(0).getHourSlots().get(1).getCategory());
		
		Algorythm algo = new Algorythm();
		algo.planDay(day);
			
	}

	private Hour generateDayHour(Category powerCat, Category newCat, Category ninetiesCat, Category eightiesCat) {
		List<Category> categoriesForHour = new ArrayList<Category>();
		categoriesForHour.add(powerCat);
		categoriesForHour.add(eightiesCat);
		categoriesForHour.add(newCat);
		categoriesForHour.add(ninetiesCat);
		categoriesForHour.add(eightiesCat);
		categoriesForHour.add(newCat);
		categoriesForHour.add(eightiesCat);
		categoriesForHour.add(ninetiesCat);

		return new Hour("Day Hour", categoriesForHour);
	}

}
