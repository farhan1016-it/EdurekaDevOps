package com.mindtree.libraries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDrivers {

	private InputStream oFileReader;
	private OutputStream oFileWriter;
	private Workbook oExcelWorkbook;
	private String sExcelFileName;

	public ExcelDrivers() {
		this.setNull();
	}

	public void createNewExcelWorkbook(String sFileName) {
		try {

			sFileName = sFileName.trim();
			if (sFileName.isEmpty()) {
				throw new Exception("No file name specified...");
			}

			if ((new File(sFileName)).exists()) {
				throw new Exception("File already Exists");
			}

			if (sFileName.toLowerCase().endsWith("xlsx")) {
				oExcelWorkbook = new XSSFWorkbook();

			} else if (sFileName.toLowerCase().endsWith("xls")) {
				oExcelWorkbook = new HSSFWorkbook();

			} else {
				throw new Exception("Invalid File Extension...");
			}

			oFileWriter = new FileOutputStream(sFileName);
			oExcelWorkbook.write(oFileWriter);
			oFileWriter.close();
			((FileInputStream) oExcelWorkbook).close();
			this.setNull();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setNull() {
		oFileReader = null;
		oFileWriter = null;
		oExcelWorkbook = null;
		sExcelFileName = "";
	}

	// ---------------------------------------------------------

	public void openExcelWorkbook(String sFileName) {
		try {

			sFileName = sFileName.trim();
			if (sFileName.isEmpty()) {
				throw new Exception("No file name specified...");
			}

			if (!(new File(sFileName)).exists()) {
				throw new Exception("File does not Exists");
			}

			oFileReader = new FileInputStream(sFileName);
			sExcelFileName = sFileName;
			oExcelWorkbook = WorkbookFactory.create(oFileReader);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			oFileWriter = new FileOutputStream(sExcelFileName);
			oExcelWorkbook.write(oFileWriter);
			oFileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean saveAs(String sFileName) {
		try {
			if (sFileName.isEmpty()) {
				throw new Exception("No file name specified...");
			}

			if ((new File(sFileName)).exists()) {
				throw new Exception("File already Exists");
			}

			oFileWriter = new FileOutputStream(sFileName);
			oExcelWorkbook.write(oFileWriter);
			oFileWriter.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void close() {
		try {
			((FileInputStream) oExcelWorkbook).close();
			oFileReader.close();
			setNull();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void createSheet(String sSheetName) {
		try {
			sSheetName = sSheetName.trim();
			if (sSheetName.isEmpty()) {
				throw new Exception("Sheet name not specified");

			}

			Sheet oSheet;

			oSheet = oExcelWorkbook.getSheet(sSheetName);
			if (oSheet != null) {
				throw new Exception("Sheet already Exist");
			}
			oExcelWorkbook.createSheet(sSheetName);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getRowCountOfSheet(String sSheetName) {
		try {
			sSheetName = sSheetName.trim();
			if (sSheetName.isEmpty()) {
				throw new Exception("Sheet name not specified");

			}

			Sheet oSheet;

			oSheet = oExcelWorkbook.getSheet(sSheetName);
			System.out.println("Sheet Name: " + sSheetName);
			if (oSheet == null) {
				throw new Exception("Sheet does not Exist");
			}

			return oSheet.getLastRowNum();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	public int getCellCount(String sSheetName, int iRow) {
		try {
			sSheetName = sSheetName.trim();
			if (sSheetName.isEmpty()) {
				throw new Exception("Sheet name not specified");

			}

			Sheet oSheet;

			oSheet = oExcelWorkbook.getSheet(sSheetName);
			if (oSheet == null) {
				throw new Exception("Sheet doesnot Exist");
			}

			if (iRow < 1) {
				throw new Exception("ROw Index start from 1");
			}

			Row oRow;

			oRow = oSheet.getRow(iRow - 1);

			if (oRow == null) {
				return 0;
			} else {
				return oRow.getLastCellNum() + 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	@SuppressWarnings("deprecation")
	public String getCellCData(String sSheetName, int iRow, int iCell) {
		try {
			sSheetName = sSheetName.trim();
			if (sSheetName.isEmpty()) {
				throw new Exception("Sheet name not specified");

			}

			Sheet oSheet;

			oSheet = oExcelWorkbook.getSheet(sSheetName);
			if (oSheet == null) {
				throw new Exception("Sheet already Exist");
			}

			if (iRow < 1 || iCell < 1) {
				throw new Exception("Row & Cell Index start from 1");
			}

			Row oRow;

			oRow = oSheet.getRow(iRow - 1);

			if (oRow == null) {
				return "";
			}

			Cell oCell;
			oCell = oRow.getCell(iCell - 1);
			if (oCell == null) {
				return "";
			} else {

				if (oCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					System.out.println("Cell Value:" + String.valueOf((long) oCell.getNumericCellValue()));
					return String.valueOf((long) oCell.getNumericCellValue());
				} else {
					System.out.println("String cell value: " + oCell.getStringCellValue());
					return oCell.getStringCellValue();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public void setCellCData(String sSheetName, int iRow, int iCell, String sValue) {
		try {
			sSheetName = sSheetName.trim();
			if (sSheetName.isEmpty()) {
				throw new Exception("Sheet name not specified");

			}

			Sheet oSheet;

			oSheet = oExcelWorkbook.getSheet(sSheetName);
			if (oSheet == null) {
				throw new Exception("Sheet doesnot  Exist");
			}

			if (iRow < 1 || iCell < 1) {
				throw new Exception("Row & Cell Index start from 1");
			}

			Row oRow;

			oRow = oSheet.getRow(iRow - 1);

			if (oRow == null) {
				oSheet.createRow(iRow - 1);
				oRow = oSheet.getRow(iRow - 1);
			}

			Cell oCell;
			oCell = oRow.getCell(iCell - 1);
			if (oCell == null) {
				oRow.createCell(iCell - 1);
				oCell = oRow.getCell(iCell - 1);
			}

			oCell.setCellValue(sValue);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}
}
