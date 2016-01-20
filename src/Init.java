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

import currentAlgos.AC3Algo;
import currentAlgos.GraphGreedy;
import oldAlgos.AC3AlgoOld;
import oldAlgos.BacktrackingAlgo;
import oldAlgos.BranchNBoundAlgo;
import oldAlgos.EasyGreedy;
import oldAlgos.EvolutionaryAlgor;
import oldAlgos.GreedyAlgo;
import oldAlgos.GreedyAlgo2;
import wrappers.Category;
import wrappers.Day;
import wrappers.Hour;
import wrappers.ResultOfMeasurement;
import wrappers.Song;
import wrappers.Song.Genre;
import wrappers.Song.Tempo;

public class Init {

	int maxHours = 24;
	int maxTries = 10;
	int skipHours = 2;

	public void start() {
		List<ResultOfMeasurement> results = new ArrayList<ResultOfMeasurement>();
		Database database = new Database();
		AC3Algo ac3 = new AC3Algo();
		GraphGreedy gGreedy = new GraphGreedy();
		for (int hours = 1; hours < maxHours + 1; hours = hours+skipHours) {
			for (int round = 0; round < maxTries; round++) {
				System.gc();

				Day emptyDay = database.generate80sDay(hours);
				//ac3.initializeDay(emptyDay);
				long start = System.currentTimeMillis();
				Day plannedDay = gGreedy.planDay(emptyDay);
				long time = System.currentTimeMillis() - start;
				bulidResultOfMeasurement(time, results, plannedDay.getNumOfArcs(), plannedDay.getLargestDomain(), plannedDay.calZeroes(), hours);
			}
		}
		System.out.println(results);
		writeOutresultsToXLSX(results, "resources/greedy02.xlsx");

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

		for (int rowNum = 0; rowNum < maxTries*2 + 5; rowNum++) {

			Row row = sheet.createRow((short) rowNum);
		}
		int counter = 0;
		int counter2 = 0;

		for (int cellNum = 0; cellNum < (maxHours / skipHours); cellNum++) {

			for (int rowNum = 0; rowNum < maxTries*2 + 5; rowNum++) {

				Row row = sheet.getRow(rowNum);
				Cell cell = row.createCell(cellNum);
				if (rowNum == 0) {
					cell.setCellValue(results.get(counter).largestDomain);
				}
				if (rowNum == 1) {
					cell.setCellValue(results.get(counter).totalArcs);

				}
				if (rowNum == 2) {
					cell.setCellValue(results.get(counter).hours);

				}
				if (rowNum > 3 && rowNum < 14) {
					cell.setCellValue(results.get(counter).time);
					counter++;
				}
				if (rowNum > 14) {
					cell.setCellValue(results.get(counter2).zeroes);
					counter2++;
				}
				

			}
		}
		return wb;
	}

	private void bulidResultOfMeasurement(long time, List<ResultOfMeasurement> results, int numOfArcs, int largestDomain, double zeroes, int hours) {
		double timeInS = time * 0.001D;

		ResultOfMeasurement result = new ResultOfMeasurement();
		result.time = timeInS;
		result.largestDomain = largestDomain;
		result.totalArcs = numOfArcs;
		result.zeroes = zeroes;
		result.hours = hours;
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
