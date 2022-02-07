package com.sherwin.shercolor.customerprofilesetup.web.dto;

public class CustProfile {
	private String custType;
	private boolean useRoomByRoom;
	private boolean useLocatorId;
	
	public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public boolean isUseRoomByRoom() {
		return useRoomByRoom;
	}
	public void setUseRoomByRoom(boolean useRoomByRoom) {
		this.useRoomByRoom = useRoomByRoom;
	}
	public boolean isUseLocatorId() {
		return useLocatorId;
	}
	public void setUseLocatorId(boolean useLocatorId) {
		this.useLocatorId = useLocatorId;
	}
}
