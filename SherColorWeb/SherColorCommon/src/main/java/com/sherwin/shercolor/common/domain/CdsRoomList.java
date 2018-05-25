package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CDS_ROOM_LIST")
@IdClass(CdsRoomListPK.class)
public class CdsRoomList {
	private String listName;
	private Integer seqNbr;
	private String roomUse;
	private String cdsAdlFld;
	
	@Id
	@Column(name="list_name")
	@NotNull
	public String getListName() {
		return listName;
	}

	@Id
	@Column(name="seq_nbr")
	@NotNull
	public Integer getSeqNbr() {
		return seqNbr;
	}

	@Column(name="room_use")
	public String getRoomUse() {
		return roomUse;
	}

	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	/////////////// SETTERS  //////////////////
	
	public void setListName(String listName) {
		this.listName = listName;
	}
	public void setSeqNbr(Integer seqNbr) {
		this.seqNbr = seqNbr;
	}
	public void setRoomUse(String roomUse) {
		this.roomUse = roomUse;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}

	
}
