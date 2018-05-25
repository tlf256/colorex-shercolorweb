package com.sherwin.shercolor.common.dao;

import com.sherwin.shercolor.common.domain.CustWebLoginTransform;

public interface CustWebLoginTransformDao {

	
	/***
	 * Read a CustWebLoginTransform record for the keyfield provided.
	 * @param keyField - the keyField on the table (probably user ID)
	 * @return Single CustWebLoginTransform record or null if not found
	 */
	public CustWebLoginTransform read(String keyField);
	
	
	/***
	 * Insert a CustWebLoginTransform record.
	 * @param custWebLoginXForm - a CustWebLoginTransform record.
	 * @return - true if successfully inserted, false if an error occurred.
	 */
	public boolean create(CustWebLoginTransform custWebLoginXForm);
	
	
	/***
	 * Delete a CustWebLoginTransform record.
	 * @param custWebLoginXForm - a CustWebLoginTransform record.
	 * @return - true if successfully deleted, false if an error occurred.
	 */
	public boolean delete(CustWebLoginTransform custWebLoginXForm);
	
}
