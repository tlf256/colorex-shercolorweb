package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsRoomList;

public interface CdsRoomListDao {
	
	/***
	 * Read a CdsRoomList record for the Room List Name and Sequence Number provided.
	 * @param listName - Room List Name
	 * @param seqNbr - Sequence Number
	 * @return Single CdsRoomList record or null if not found
	 */
	public CdsRoomList read(String listName, int seqNbr);
	
	/***
	 * Retrieve a list of CdsRoomList records for the Room List Name provided.
	 * @param listName - Room List Name
	 * @return - List of CdsRoomList records or Zero sized list if none found
	 */
	public List<CdsRoomList> listForName(String listName);
	

}
