package com.sherwin.shercolor.customershercolorweb.web.model;

public class JobSearch {
	private String controlNbr;
	private String fromDate;
	private String toDate;
	private String customerId;
	private String colorId;
	private String colorName;
	private String roomUse;

	public String getControlNbr() {
		return controlNbr;
	}

	public void setControlNbr(String controlNbr) {
		this.controlNbr = controlNbr;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getRoomUse() {
		return roomUse;
	}

	public void setRoomUse(String roomUse) {
		this.roomUse = roomUse;
	}
}
