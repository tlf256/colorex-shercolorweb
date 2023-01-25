package com.sherwin.shercolor.customershercolorweb.web.dto;

import java.util.ArrayList;
import java.util.List;
import com.sherwin.shercolor.common.domain.CustWebDealerCustOrd;

public class CustWebDealerCustOrdDtoBuilder {
	
	public CustWebDealerCustOrdDto build(CustWebDealerCustOrd custWebDealerCustOrd) throws Exception {
		CustWebDealerCustOrdDto result = new CustWebDealerCustOrdDto();
		if (custWebDealerCustOrd != null){
			result.setCustomerId(custWebDealerCustOrd.getCustomerId());
			result.setDlrCustId(custWebDealerCustOrd.getDlrCustId());
			result.setCustOrderNbr(custWebDealerCustOrd.getCustOrderNbr());
			result.setComments(custWebDealerCustOrd.getComments());
			result.setUpdateUser(custWebDealerCustOrd.getUpdateUser());
			try{
				result.setControlNbr(Integer.toString(custWebDealerCustOrd.getControlNbr()));
			}
			catch(NumberFormatException nfe){
				throw nfe;
			}
		}
		return result;
	}
	
	public List<CustWebDealerCustOrdDto> build(List<CustWebDealerCustOrd> orders) throws Exception {
		List<CustWebDealerCustOrdDto> result = new ArrayList<CustWebDealerCustOrdDto>();
		if (orders != null && orders.size() > 0){
			for (CustWebDealerCustOrd order : orders){
				CustWebDealerCustOrdDto dto = build(order);
				result.add(dto);
			}
		}
		return result;
	}

	public CustWebDealerCustOrd buildUpdate(CustWebDealerCustOrdDto custWebDealerCustOrdDto) throws Exception {
		CustWebDealerCustOrd result = new CustWebDealerCustOrd();
		if (custWebDealerCustOrdDto != null){
			result.setCustomerId(custWebDealerCustOrdDto.getCustomerId());
			result.setDlrCustId(custWebDealerCustOrdDto.getDlrCustId());
			result.setCustOrderNbr(custWebDealerCustOrdDto.getCustOrderNbr());
			result.setComments(custWebDealerCustOrdDto.getComments());
			result.setUpdateUser(custWebDealerCustOrdDto.getUpdateUser());
			try{
				result.setControlNbr(Integer.parseInt(custWebDealerCustOrdDto.getControlNbr()));
			}
			catch(NumberFormatException nfe){
					throw nfe;
			}
		}
		return result;
	}

}
