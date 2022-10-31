package org.parsing.file;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CellPhoneUsage {
	private Date billDate;
	private int minutesUsed;
	private float dataUsed;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	public CellPhoneUsage(String billDate, String minutesUsed, String dataUsed) {

		try {
			this.billDate = dateFormat.parse(billDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.minutesUsed = Integer.parseInt(minutesUsed);
		this.dataUsed = Float.parseFloat(dataUsed);
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		try {
			this.billDate = dateFormat.parse(billDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public int getMinutesUsed() {
		return minutesUsed;
	}

	public void setMinutesUsed(String minutesUsed) {
		this.minutesUsed = Integer.parseInt(minutesUsed);
	}

	public float getDataUsed() {
		return dataUsed;
	}

	public void setDataUsed(String dataUsed) {
		this.dataUsed = Float.parseFloat(dataUsed);
	}
}
