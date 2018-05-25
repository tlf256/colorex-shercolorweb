package com.sherwin.shercolor.common.dao;

import java.util.Date;
import java.util.List;

import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CdsColorStand;

public interface CdsColorStandDao {

	/***
	 * Read a CdsColorStand record for the Spectro Model, Spectro Mode, Color Company, Color Id and Sequence Number  provided.
	 * @param spectroModel - Spectro Model (XTS)
	 * @param spectroMode - Spectro Mode (SCI)
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param seqNbr - Sequence Number
	 * @return Single CdsColorStand record or null if not found
	 */
	public CdsColorStand read(String spectroModel, String spectroMode, String colorComp, String colorId, int seqNbr);
	
	/***
	 * Retrieve a List of CdsColorStand records for the CdsColorMast record and Activity Date provided. Spectro Model and Mode are assumed to be XTS and SCI.
	 * @param cdsColorMast - CdsColorMast record
	 * @param activityDate - Date of the action (most likely current date). If null, all standards will be returned (even the expired standards).
	 * @return - List of CdsColorStand records or Zero sized list if none found
	 */
	public List<CdsColorStand> listForColorCompId(CdsColorMast cdsColorMast, Date activityDate);

	/***
	 * Retrieve a List of CdsColorStand records for the Color Company, Color Id and Activity Date provided. Spectro Model and Mode are assumed to be XTS and SCI.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param activityDate - Date of the action (most likely current date). If null, all standards will be returned (even the expired standards).
	 * @return - List of CdsColorStand records or Zero sized list if none found
	 */
	public List<CdsColorStand> listForColorCompId(String colorComp, String colorId, Date activityDate);

	/***
	 * Read a CdsColorStand record for the CdsColorMast record, Activity Date and Color Engine Version provided. Spectro Model and Mode are assumed to be XTS and SCI.
	 * @param cdsColorMast - CdsColorMast record
	 * @param activityDate - Date of the action (most likely current date). If null, all standards will be considered (even the expired standards).
	 * @param colorEngVer - Color Engine Version
	 * @return Single CdsColorStand record or null if not found
	 */
	public CdsColorStand readByEffectiveVersion(CdsColorMast cdsColorMast, Date activityDate, String colorEngVer);

	/***
	 * Read a CdsColorStand record for the Color Company, Color Id, Activity Date and Color Engine Version provided. Spectro Model and Mode are assumed to be XTS and SCI.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param activityDate - Date of the action (most likely current date). If null, all standards will be considered (even the expired standards).
	 * @param colorEngVer - Color Engine Version
	 * @return Single CdsColorStand record or null if not found
	 */
	public CdsColorStand readByEffectiveVersion(String colorComp, String colorId, Date activityDate, String colorEngVer);

	/***
	 * Retrieve a List of CdsColorStand records for the CIE L Range, CIE A Range and CIE B Range provided. Note only returns standards that are in effect for the current system date.
	 * @param colorComp - Color Company for search, if null all Color Companies will be searched
	 * @param minLValue - CIE L Range Minimum
	 * @param maxLValue - CIE L Range Maximum
	 * @param minAValue - CIE A Range Minimum
	 * @param maxAValue - CIE A Range Maximum
	 * @param minBValue - CIE B Range Minimum
	 * @param maxBValue - CIE B Range Maximum
	 * @return - List of CdsColorStand records or Zero sized list if none found
	 */
	public List<CdsColorStand> listForLABRanges(String colorComp, Double minLValue, Double maxLValue, Double minAValue, Double maxAValue, Double minBValue, Double maxBValue);

}
