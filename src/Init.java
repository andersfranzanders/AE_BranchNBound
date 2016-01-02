import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import currentAlgos.EasyGreedy;
import currentAlgos.GreedyAlgo2;
import oldAlgos.AC3Algo;
import oldAlgos.BacktrackingAlgo;
import oldAlgos.BranchNBoundAlgo;
import oldAlgos.EvolutionaryAlgor;
import oldAlgos.GreedyAlgo;
import wrappers.Category;
import wrappers.Day;
import wrappers.Hour;
import wrappers.ResultOfMeasurement;
import wrappers.Song;
import wrappers.Song.Genre;
import wrappers.Song.Tempo;

public class Init {

	int repetitions = 10;

	public void start() {
		List<ResultOfMeasurement> results = new ArrayList<ResultOfMeasurement>();
		Database database = new Database();
		GreedyAlgo2 gAlgo2 = new GreedyAlgo2();
		EasyGreedy easyGreedy = new EasyGreedy();

		 for (int hours = 1; hours < 17; hours = hours + 2) {
		//for (int hours = 1; hours < 18; hours = hours + 4) {
			for (int rep = 0; rep < repetitions; rep++) {
				System.gc();

				Day emptyDay = database.buildRandomDay(hours);
				//Day emptyDay = database.generate80sDay(hours);
				// System.out.println(emptyDay);
				long start = System.currentTimeMillis();
				//Day plannedDay = gAlgo2.planNextSong(emptyDay);
				Day plannedDay = easyGreedy.planDay(emptyDay);
				long time = System.currentTimeMillis() - start;
				bulidResultOfMeasurement(time, results, hours * 16, plannedDay.getTotalViolations());
			}
		}
		writeOutresultsToXLSX(results, "resources/resultsOfEasyGreedyWithRandomAC.xlsx");

	}

	private void writeOutresultsToXLSX(List<ResultOfMeasurement> results, String path) {
		try {
			Workbook wb = convertResultsToXLSX(results);

			FileOutputStream fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Workbook convertResultsToXLSX(List<ResultOfMeasurement> results) {
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet("new sheet");
		for (int rowNum = 0; rowNum < results.size(); rowNum++) {

			Row row = sheet.createRow((short) rowNum);
			for (int cellNum = 0; cellNum < 3; cellNum++) {
				Cell cell = row.createCell(cellNum);
				switch (cellNum) {
				case 0:
					cell.setCellValue(results.get(rowNum).numberOfSlots);
					break;
				case 1:
					cell.setCellValue(results.get(rowNum).time);
					break;
				case 2:
					cell.setCellValue(results.get(rowNum).violationsPerSlots);
					break;
				}

			}
		}
		return wb;
	}

	private void bulidResultOfMeasurement(long time, List<ResultOfMeasurement> results, int slotLength,
			int totalViolations) {
		double timeInS = time * 0.001D;

		ResultOfMeasurement result = new ResultOfMeasurement();
		result.time = timeInS;
		result.numberOfSlots = slotLength;
		result.violationsPerSlots = (double) totalViolations / slotLength;
		results.add(result);
		System.out.println(result);

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
