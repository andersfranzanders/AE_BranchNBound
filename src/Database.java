import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Database {
	String path = "resources/rotation 80s80s.xlsx";

	public void generateDatabase() {

		try {
			InputStream inp = new FileInputStream(path);
			Workbook wb = WorkbookFactory.create(inp);

			Sheet sheet = wb.getSheetAt(0);

			for (int rowNum = 1; rowNum < 2; rowNum++) {

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

			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (InvalidFormatException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
