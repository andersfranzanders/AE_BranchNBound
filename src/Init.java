import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import wrappers.Category;
import wrappers.Day;
import wrappers.Hour;
import wrappers.Song;
import wrappers.Song.Genre;
import wrappers.Song.Tempo;

public class Init {

	int hoursOfDay = 24;

	public void start() {

		for (int i = 1; i < 11; i++) {

			hoursOfDay = i * 24;

			for (int messrunde = 0; messrunde < 10; messrunde++) {
				System.gc();

				Day day = buildRandomDay();
				System.out.println("Begin!");

				Algorythm algo = new Algorythm();
				long start = System.currentTimeMillis();
				float violationsPerSlot = algo.planDay(day);
				long time = System.currentTimeMillis() - start;
				double timeInS = time * 0.001D;
				writeTimeToFile(
						"/Users/franzanders/Documents/HTWK/Master/3. Semester/Algorythm Engineering/Projekt/ersteLaufzeitmessung/ersteLaufzeitmessung_200Songs.txt",
						timeInS, violationsPerSlot);
			}
		}
	}

	private void writeTimeToFile(String fileName, double time, float violations) {
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			String contents = scanner.useDelimiter("\\Z").next();
			contents += " \n Hours Planned:  " + hoursOfDay + " | Time: " + String.valueOf(time)
					+ " | Violations Per Slot: " + violations;
			System.out.println(contents);
			scanner.close();

			PrintWriter out = new PrintWriter(fileName);
			out.println(contents);
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Day buildRandomDay() {
		// NumberForCats: 4, 16, 30, 40
		Category powerCat = new Category("Power Cat", 8, 4);
		powerCat.fillCategoryWithRandomSongs();

		Category newCat = new Category("New Cat", 32, 6);
		newCat.fillCategoryWithRandomSongs();

		Category ninetiesCat = new Category("90s", 60, 8);
		ninetiesCat.fillCategoryWithRandomSongs();

		Category eightiesCat = new Category("80s", 100, 10);
		eightiesCat.fillCategoryWithRandomSongs();

		// System.out.println(powerCat);
		// System.out.println(newCat);
		// System.out.println(ninetiesCat);
		// System.out.println(eightiesCat);

		Day day = new Day(hoursOfDay);

		for (int i = 0; i < hoursOfDay; i++) {
			Hour dayHour = generateDayHour(powerCat, newCat, ninetiesCat, eightiesCat);
			day.addHourToList(dayHour);
		}
		return day;
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
