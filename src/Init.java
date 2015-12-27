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

	int hoursOfDay = 2;

	public void start() {

		int i = 1;
		Day day = buildRandomDay(i);
		day.remainingSlots = day.calNumberOfSlots();
		System.out.println("Begin!");
		BranchNBoundAlgo bbAlgo = new BranchNBoundAlgo();
		
		//bbAlgo.testComparator();
		Day plannedDay = bbAlgo.planNextSong(day);
		System.out.println(plannedDay);
		
		
	}

	private void writeTimeToFile(String fileName, double time, float violations, int i, double[] durchschnitt,
			int messrunde) {
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			String contents = scanner.useDelimiter("\\Z").next();
			// contents += " \n Hours Planned:  " + hoursOfDay + " | Time: " +
			// String.valueOf(time)
			// + " | Violations Per Slot: " + violations;
			contents += " \n Songs Planned:  " + (i * 20) + " | Time: " + String.valueOf(time)
					+ " | Violations Per Slot: " + violations;
			if (messrunde == 9) {
				double finalDurchschnitt = 0;
				for (double zahl : durchschnitt) {
					finalDurchschnitt += zahl;
				}
				finalDurchschnitt = finalDurchschnitt / (durchschnitt.length);
				contents += " \n Durchschnitt der Messreihe: " + finalDurchschnitt;
			}

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

	public Day buildRandomDay(int round) {
		// NumberForCats: 4, 16, 30, 40
		Category powerCat = new Category("Power Cat", round * 2, 4);
		powerCat.fillCategoryWithRandomSongs();

		Category newCat = new Category("New Cat", round * 2, 6);
		newCat.fillCategoryWithRandomSongs();

		Category ninetiesCat = new Category("90s", round * 2, 6);
		ninetiesCat.fillCategoryWithRandomSongs();

		Category eightiesCat = new Category("80s", round * 2, 10);
		eightiesCat.fillCategoryWithRandomSongs();
		
		Day day = new Day(hoursOfDay);

		for (int i = 0; i < hoursOfDay; i++) {
			Hour dayHour = generateDayHour(powerCat, newCat, ninetiesCat, eightiesCat);
			day.addHourToList(dayHour);
		}
		System.out.println(powerCat);
		System.out.println(newCat);
		System.out.println(ninetiesCat);
		System.out.println(eightiesCat);
		
		return day;
	}

	private Hour generateDayHour(Category powerCat, Category newCat, Category ninetiesCat, Category eightiesCat) {
		List<Category> categoriesForHour = new ArrayList<Category>();
		categoriesForHour.add(powerCat);
		categoriesForHour.add(eightiesCat);
//		categoriesForHour.add(newCat);
//		categoriesForHour.add(ninetiesCat);
//		categoriesForHour.add(eightiesCat);
//		categoriesForHour.add(newCat);
//		categoriesForHour.add(eightiesCat);
//		categoriesForHour.add(ninetiesCat);

		return new Hour("Day Hour", categoriesForHour);
	}

}
