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
		//Day day = buildRandomDay(i);
		//day.remainingSlots = day.calNumberOfSlots();
		System.out.println("Begin!");
		BranchNBoundAlgo bbAlgo = new BranchNBoundAlgo();
		BranchNBoundAlgo2 bbAlgo2 = new BranchNBoundAlgo2();
		GreedyAlgo gAlgo = new GreedyAlgo();
		
		Database database = new Database();
		Day emptyDay = database.buildRandomDay(4);
		//Day emptyDay = database.generate80sDay(15);
		
		//Day plannedDay = gAlgo.planNextSong(emptyDay);
		Day plannedDay = bbAlgo2.planNextSong(emptyDay);
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

	

}
