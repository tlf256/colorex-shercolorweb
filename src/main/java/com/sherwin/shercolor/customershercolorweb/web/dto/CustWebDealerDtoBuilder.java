package com.sherwin.shercolor.customershercolorweb.web.dto;

import java.util.ArrayList;
import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebDealer;

public class CustWebDealerDtoBuilder {
	
	public CustWebDealerDto build(CustWebDealer custWebDealer) throws Exception{
		CustWebDealerDto result = new CustWebDealerDto();

		if (custWebDealer != null){
			result.setCustomerId(custWebDealer.getCustomerId());
			result.setDealerName(custWebDealer.getDealerName());
			result.setDlrPhoneNbr(custWebDealer.getDlrPhoneNbr());
			result.setComments(custWebDealer.getComments());
			try{
				result.setHomeStore(Integer.toString(custWebDealer.getHomeStore()));
			}
			catch(Exception e){
					throw e;
			}
		}
		return result;
	}
	
	public List<CustWebDealerDto> build(List<CustWebDealer> dealers) throws Exception {
		List<CustWebDealerDto> result = new ArrayList<CustWebDealerDto>();
		if (dealers != null && dealers.size() > 0){
			for (CustWebDealer dealer : dealers){
				CustWebDealerDto dto = build(dealer);
				result.add(dto);
			}
		}
		
		return result;
	}

	public CustWebDealer buildUpdate(CustWebDealerDto custWebDealerDto) throws Exception {
		CustWebDealer result = new CustWebDealer();
		if (custWebDealerDto != null){
			result.setCustomerId(custWebDealerDto.getCustomerId());
			result.setDealerName(custWebDealerDto.getDealerName());
			result.setDlrPhoneNbr(custWebDealerDto.getDlrPhoneNbr());
			result.setComments(custWebDealerDto.getComments());
			try{
				result.setHomeStore(Integer.parseInt(custWebDealerDto.getHomeStore()));
			}
			catch(Exception e){
					throw e;
			}
		}
		return result;
	}
	
}
