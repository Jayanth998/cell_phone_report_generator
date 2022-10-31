package org.parsing.file;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CellPhone {
	private int employeeId;
	private String employeeName;
	private Date purchaseDate;
	private String model;
	private List<CellPhoneUsage> cellUsageList = new ArrayList<CellPhoneUsage>();

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	public CellPhone(String employeeId, String employeeName, String purchaseDate, String model) {
		this.employeeId = Integer.parseInt(employeeId);
		this.employeeName = employeeName;
		this.model = model;

		try {
			this.purchaseDate = dateFormat.parse(purchaseDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = Integer.parseInt(employeeId);
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		try {
			this.purchaseDate = dateFormat.parse(purchaseDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	public List<CellPhoneUsage> getCellPhoneUsages(){
		return cellUsageList;
	}
	
	public void addCellPhoneUsage(CellPhoneUsage usage) {
		cellUsageList.add(usage);
	}
}
