package com.sherwin.shercolor.customershercolorweb.web.dto;

import com.sherwin.shercolor.common.entity.CustWebDealerCust;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CustWebDealerCustDtoBuilder {
	
	public CustWebDealerCustDto build(CustWebDealerCust custWebDealerCust) throws Exception{
		CustWebDealerCustDto result = new CustWebDealerCustDto();
		if (custWebDealerCust != null){
			result.setCustomerId(custWebDealerCust.getCustomerId());
			result.setDlrCustId(custWebDealerCust.getDlrCustId());
			result.setDlrCustName(custWebDealerCust.getDlrCustName());
			result.setDlrCustAddress(custWebDealerCust.getDlrCustAddress());
			result.setDlrCustCity(custWebDealerCust.getDlrCustCity());
			result.setDlrCustState(custWebDealerCust.getDlrCustState());
			result.setDlrCustZip(custWebDealerCust.getDlrCustZip());
			result.setDlrCustCountry(custWebDealerCust.getDlrCustCountry());
			result.setDlrCustPhoneNbr(custWebDealerCust.getDlrCustPhoneNbr());
			result.setDlrCustContact(custWebDealerCust.getDlrCustContact());
			result.setUpdateUser(custWebDealerCust.getUpdateUser());
			result.setComments(custWebDealerCust.getComments());
			result.setDateAdded(custWebDealerCust.getDateAdded().toString());
			result.setDateUpdated(custWebDealerCust.getDateUpdated().toString());
		}
		return result;
	}
	
	public List<CustWebDealerCustDto> build(List<CustWebDealerCust> customers) throws Exception {
		List<CustWebDealerCustDto> result = new ArrayList<CustWebDealerCustDto>();
		if (customers != null && customers.size() > 0){
			for (CustWebDealerCust customer : customers){
				CustWebDealerCustDto dto = build(customer);
				result.add(dto);
			}
		}
		return result;
	}

	public CustWebDealerCust buildUpdate(CustWebDealerCustDto custWebDealerCustDto) throws Exception{
		CustWebDealerCust result = new CustWebDealerCust();
		if (custWebDealerCustDto != null){
			result.setCustomerId(custWebDealerCustDto.getCustomerId());
			result.setCustomerId(custWebDealerCustDto.getCustomerId());
			result.setDlrCustId(custWebDealerCustDto.getDlrCustId());
			result.setDlrCustName(custWebDealerCustDto.getDlrCustName());
			result.setDlrCustAddress(custWebDealerCustDto.getDlrCustAddress());
			result.setDlrCustCity(custWebDealerCustDto.getDlrCustCity());
			result.setDlrCustState(custWebDealerCustDto.getDlrCustState());
			result.setDlrCustCity(custWebDealerCustDto.getDlrCustCity());
			result.setDlrCustZip(custWebDealerCustDto.getDlrCustZip());
			result.setDlrCustCountry(custWebDealerCustDto.getDlrCustCountry());
			result.setDlrCustPhoneNbr(custWebDealerCustDto.getDlrCustPhoneNbr());
			result.setDlrCustContact(custWebDealerCustDto.getDlrCustContact());
			result.setUpdateUser(custWebDealerCustDto.getUpdateUser());
			result.setComments(custWebDealerCustDto.getComments());
			try {
				// Date added - Updated in add transaction only. 
				// Date updated - current date when transaction is updated.
				Date dateAdded = new SimpleDateFormat("MM/dd/yyyy").parse(custWebDealerCustDto.getDateAdded());
				result.setDateAdded(dateAdded);
				Date dateUpdated = new SimpleDateFormat("MM/dd/yyyy").parse(custWebDealerCustDto.getDateUpdated());
				result.setDateUpdated(dateUpdated);
			}
			catch(Exception e){
				throw e;
			}
		}
		return result;
	}
	

}
