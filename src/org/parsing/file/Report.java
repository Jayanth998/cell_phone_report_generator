package org.parsing.file;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Report {

	List<CellPhone> cellPhonesList = new ArrayList<CellPhone>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scan = new Scanner(System.in);
		System.out.print("Enter year : ");
		int year = scan.nextInt();
		Report rp = new Report();

		try {
			rp.parseCellPhones();
			rp.parseCellPhoneUsage();
			rp.generateReport(year);
			rp.printReport();
		} catch (IOException e) {
			e.printStackTrace();
		}

		scan.close();

	}

	public void parseCellPhones() throws IOException {
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			file = new File("src/resources/CellPhone.csv");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = br.readLine();
			while ((line = br.readLine()) != null && !line.trim().equals("")) {
				String[] str = line.split(",");
				CellPhone cp = new CellPhone(str[0], str[1], str[2], str[3]);
				cellPhonesList.add(cp);
			}
			System.out.println("Done reading CellPhone.csv");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
			fr.close();
		}

	}

	public void parseCellPhoneUsage() throws IOException {
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			file = new File("src/resources/CellPhoneUsageByMonth.csv");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = br.readLine();
			while ((line = br.readLine()) != null && !line.trim().equals("")) {
				String[] str = line.split(",");
				CellPhone cp = getCellPhone(str[0]);
				CellPhoneUsage cpu = new CellPhoneUsage(str[1], str[2], str[3]);
				cp.addCellPhoneUsage(cpu);
			}
			System.out.println("Done reading CellPhoneUsageByMonth.csv");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
			fr.close();
		}
	}

	public CellPhone getCellPhone(String employeeID) {
		int empID = Integer.parseInt(employeeID);
		for (CellPhone cp : cellPhonesList) {
			if (cp.getEmployeeId() == empID) {
				return cp;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void generateReport(int year) throws IOException {

		String runDate = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss").format(LocalDateTime.now());
		List<CellPhone> phoneList = new ArrayList<CellPhone>();
		Map<Integer, Integer[]> minutesMap = new HashMap<Integer, Integer[]>();
		Map<Integer, Float[]> dataMap = new HashMap<Integer, Float[]>();
		int totalMinutes = 0;
		float totalData = 0.0f;

		for (CellPhone cellPhone : cellPhonesList) {
			Integer[] minutes = new Integer[12];
			Float[] data = new Float[12];
			int counter = 0;
			for (CellPhoneUsage cellPhoneUsage : cellPhone.getCellPhoneUsages()) {
				// System.out.println((cellPhoneUsage.getBillDate().getYear()+1900)+";"+cellPhone.getEmployeeId());
				if ((cellPhoneUsage.getBillDate().getYear() + 1900) == year) {
					// System.out.println("3");
					counter++;
					minutes[cellPhoneUsage.getBillDate().getMonth()] = cellPhoneUsage.getMinutesUsed();
					data[cellPhoneUsage.getBillDate().getMonth()] = cellPhoneUsage.getDataUsed();
					totalMinutes += cellPhoneUsage.getMinutesUsed();
					totalData += cellPhoneUsage.getDataUsed();
				}
			}

			if (counter != 0) {
				phoneList.add(cellPhone);
				minutesMap.put(cellPhone.getEmployeeId(), minutes);
				dataMap.put(cellPhone.getEmployeeId(), data);
			}
		}

		Path path = Paths.get("src/resources/Report.txt");
		Files.deleteIfExists(path);
		FileWriter fWriter = new FileWriter(path.toString());

		fWriter.write("***************REPORT***************\n\n");
		fWriter.write("Report run Date : " + runDate + "\n");
		fWriter.write("Number of Phones : " + phoneList.size() + "\n");
		fWriter.write("Total Minutes : " + totalMinutes + "\n");
		fWriter.write("Total Data : " + String.format("%.02f", totalData) + "\n");
		fWriter.write(
				"Average Minutes : " + String.format("%.02f", Float.valueOf(totalMinutes) / phoneList.size()) + "\n");
		fWriter.write("Average Data : " + String.format("%.02f", totalData / phoneList.size()) + "\n\n\n");
		fWriter.write("***************DETAILS***************\n\n");

		for (CellPhone cellPhone : phoneList) {
			fWriter.write("Employee ID : " + cellPhone.getEmployeeId() + "\n");
			fWriter.write("Employee Name : " + cellPhone.getEmployeeName() + "\n");
			fWriter.write("Model : " + cellPhone.getModel() + "\n");
			fWriter.write("Purchase Date : " + (new SimpleDateFormat("MM/dd/yyyy")).format(cellPhone.getPurchaseDate())
					+ "\n");
			int counter = 0;
			fWriter.write("Minutes Usage :\n");
			for (Integer min : minutesMap.get(cellPhone.getEmployeeId())) {
				if (min == null)
					min = 0;
				if (counter == 0) {
					fWriter.write("** " + min);
					counter++;
				} else {
					fWriter.write(", " + min);
				}
			}
			counter = 0;
			fWriter.write("\nData Usage :\n");
			for (Float data : dataMap.get(cellPhone.getEmployeeId())) {
				if (data == null)
					data = 0.0f;
				if (counter == 0) {
					fWriter.write("** " + String.format("%.02f", data));
					counter++;
				} else {
					fWriter.write(", " + String.format("%.02f", data));
				}
			}
			fWriter.write("\n\n**********\n\n");
		}
		fWriter.write("\n************END OF REPORT************");
		fWriter.close();
		System.out.println("Done generating Report");
	}

	public void printReport() throws IOException {
		File file = new File("src/resources/Report.txt");
		Desktop.getDesktop().print(file);
		System.out.println("Done Printing Report");
	}

}
