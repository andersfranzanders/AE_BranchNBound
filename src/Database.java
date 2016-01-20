import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import wrappers.Arc;
import wrappers.Category;
import wrappers.ConnectedNode;
import wrappers.Day;
import wrappers.Hour;
import wrappers.Slot;
import wrappers.Song;

public class Database {
	String path = "resources/rotation 80s80s.xlsx";

	public Day generate80sDay(int hours) {

		Category Acat = new Category("A", 115, 16);
		Category Bcat = new Category("B", 271, 29);
		Category Ccat = new Category("C", 89, 59);
		Category Rcat = new Category("R", 45, (7 * 24) + 6);

		// Category Acat = new Category("A", 115 / 2, 16);
		// Category Bcat = new Category("B", 271 / 2, 29);
		// Category Ccat = new Category("C", 89 / 2, 59);
		// Category Rcat = new Category("R", 45 / 2, (7 * 24) + 6);

		extractSongsAndFillCategories(Acat, Bcat, Ccat, Rcat);

		Day day = new Day(hours);
		for (int i = 0; i < hours; i++) {
			if (i % 2 == 0) {
				Hour aHour = generateHour_A(Acat, Bcat, Ccat);
				day.addHourToList(aHour);
			} else {
				Hour bHour = generateHour_B(Acat, Bcat, Ccat, Rcat);
				day.addHourToList(bHour);
			}
		}
		// System.out.println(Acat);
		// System.out.println(Bcat);
		// System.out.println(Ccat);
		// System.out.println(Rcat);
		connectAllSlots(day);
		fillRemainingSongs(day);

		day.remainingSlots = day.calNumberOfSlots();
		return day;

	}

	private Hour generateHour_B(Category Acat, Category Bcat, Category Ccat, Category Rcat) {
		List<Category> BhourCats = new ArrayList<Category>();
		BhourCats.add(Bcat);
		BhourCats.add(Bcat);
		BhourCats.add(Acat);
		BhourCats.add(Bcat);
		BhourCats.add(Ccat);
		BhourCats.add(Bcat);
		BhourCats.add(Acat);
		BhourCats.add(Bcat);
		BhourCats.add(Bcat);
		BhourCats.add(Rcat);
		BhourCats.add(Acat);
		BhourCats.add(Bcat);
		BhourCats.add(Bcat);
		BhourCats.add(Acat);
		BhourCats.add(Bcat);
		BhourCats.add(Bcat);

		Hour bHour = new Hour("B Hour", BhourCats);
		return bHour;
	}

	private Hour generateHour_A(Category Acat, Category Bcat, Category Ccat) {
		List<Category> AhourCats = new ArrayList<Category>();
		AhourCats.add(Bcat);
		AhourCats.add(Acat);
		AhourCats.add(Bcat);
		AhourCats.add(Ccat);
		AhourCats.add(Bcat);
		AhourCats.add(Bcat);
		AhourCats.add(Acat);
		AhourCats.add(Bcat);
		AhourCats.add(Ccat);
		AhourCats.add(Bcat);
		AhourCats.add(Acat);
		AhourCats.add(Bcat);
		AhourCats.add(Bcat);
		AhourCats.add(Acat);
		AhourCats.add(Bcat);
		AhourCats.add(Bcat);
		Hour aHour = new Hour("A Hour", AhourCats);
		return aHour;
	}

	private void extractSongsAndFillCategories(Category Acat, Category Bcat, Category Ccat, Category Rcat) {
		try {
			InputStream inp = new FileInputStream(path);
			Workbook wb = WorkbookFactory.create(inp);

			Sheet sheet = wb.getSheetAt(0);

			for (int rowNum = 1; rowNum < 472; rowNum++) {

				Row row = sheet.getRow(rowNum);

				String title = "";
				String artist = "";
				String category = "";
				double year = 0;
				double energy = 0;
				String genre = "";
				String tempo = "";
				String gender = "";

				for (int columnNum = 0; columnNum < 12; columnNum++) {
					Cell cell = row.getCell(columnNum);
					if (cell != null) {
						double numValue = 0;
						String stringValue = "";

						int type = cell.getCellType();

						if (type == 0) {
							numValue = cell.getNumericCellValue();
							// System.out.println(numValue);
						}
						if (type == 1) {
							stringValue = cell.getStringCellValue();
							// System.out.println(stringValue);
						}

						switch (columnNum) {
						case 0:
							year = numValue;
							break;
						case 2:
							category = stringValue;
							break;
						case 5:
							title = stringValue;
							break;
						case 6:
							artist = stringValue;
							break;
						case 7:
							energy = numValue;
							break;
						case 8:
							genre = stringValue;
							break;
						case 9:
							tempo = stringValue;
							break;
						case 11:
							gender = stringValue;
							break;
						}

					}
				}

				Song song = Song.generateSongFromXls(year, category, title, artist, energy, genre, tempo, gender);

				switch (category) {
				case "A":
					Acat.addSong(song);
					break;
				case "B":
					Bcat.addSong(song);
					break;
				case "C":
					Ccat.addSong(song);
					break;
				case "R":
					Rcat.addSong(song);
					break;
				}

			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (InvalidFormatException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public Day buildRandomDay(int hours) {
		// NumberForCats: 4, 16, 30, 40
		Category powerCat = new Category("A", 50, 16);
		powerCat.fillCategoryWithRandomSongs();

		Category newCat = new Category("B", 271, 29);
		newCat.fillCategoryWithRandomSongs();

		Category ninetiesCat = new Category("C", 50, 30);
		ninetiesCat.fillCategoryWithRandomSongs();

		Category eightiesCat = new Category("D", 50, 30);
		eightiesCat.fillCategoryWithRandomSongs();

		Category thousandsCat = new Category("E", 50, 30);
		thousandsCat.fillCategoryWithRandomSongs();

		Category recurrentCat = new Category("F", 49, 30);
		recurrentCat.fillCategoryWithRandomSongs();

		Day day = new Day(hours);

		for (int i = 0; i < hours; i++) {
			Hour dayHour = generateRandomDayHour(powerCat, newCat, ninetiesCat, eightiesCat, thousandsCat, recurrentCat);
			day.addHourToList(dayHour);
		}
		connectAllSlots(day);
		fillRemainingSongs(day);

		System.out.println(powerCat);
		System.out.println(newCat);
		System.out.println(ninetiesCat);
		System.out.println(eightiesCat);
		day.remainingSlots = day.calNumberOfSlots();
		return day;
	}

	private void fillRemainingSongs(Day day) {

		for (Hour hour : day.getListOfHours()) {
			for (Slot slot : hour.getHourSlots()) {
				for (Song song : slot.getCategory().getSongList()) {
					slot.getRemainingSongs().add(song);
				}
			}
		}

	}

	private void connectAllSlots(Day day) {

		for (int hourIndex = 0; hourIndex < day.getListOfHours().size(); hourIndex++) {
			Hour hour = day.getListOfHours().get(hourIndex);
			for (int slotIndex = 0; slotIndex < hour.getHourSlots().size(); slotIndex++) {
				Slot slot = hour.getHourSlots().get(slotIndex);

				if (slotIndex > 0) {
					ConnectedNode node = new ConnectedNode(hour.getHourSlots().get(slotIndex - 1), 0);
					slot.getConnectedNodes().add(node);
				}
				if (slotIndex < hour.getHourSlots().size() - 1) {
					ConnectedNode node = new ConnectedNode(hour.getHourSlots().get(slotIndex + 1), 0);
					slot.getConnectedNodes().add(node);
				}
				Category thisCat = slot.getCategory();
				int rotTime = thisCat.getMaxRot();
				for (int rotIndex = hourIndex - rotTime; rotIndex <= hourIndex + rotTime; rotIndex++) {
					if (rotIndex >= 0 && rotIndex < day.getListOfHours().size()) {
						Hour rotHour = day.getListOfHours().get(rotIndex);
						for (Slot rotSlot : rotHour.getHourSlots()) {
							if (rotSlot.getCategory().equals(thisCat) && !rotSlot.equals(slot)) {
								ConnectedNode node = new ConnectedNode(rotSlot, 1);
								slot.getConnectedNodes().add(node);
							}
						}
					}
				}

			}
		}

	}

	private Hour generateRandomDayHour(Category A, Category B, Category C, Category D,
			Category E, Category F) {
		List<Category> categoriesForHour = new ArrayList<Category>();
		categoriesForHour.add(A);
		categoriesForHour.add(D);
		categoriesForHour.add(B);
		 categoriesForHour.add(C);
		 categoriesForHour.add(F);
		 categoriesForHour.add(D);
		 categoriesForHour.add(E);
		 categoriesForHour.add(B);
		 categoriesForHour.add(A);
		 categoriesForHour.add(B);
		 categoriesForHour.add(B);
		 categoriesForHour.add(C);
		 categoriesForHour.add(F);
		 categoriesForHour.add(B);
		 categoriesForHour.add(E);
		 categoriesForHour.add(B);

		return new Hour("Day Hour", categoriesForHour);
	}

	public void serializeDay(Day day, String filename) {

		OutputStream fos = null;

		try {
			fos = new FileOutputStream(filename);
			ObjectOutputStream o = new ObjectOutputStream(fos);
			o.writeObject("Today");
			o.writeObject(day);
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
