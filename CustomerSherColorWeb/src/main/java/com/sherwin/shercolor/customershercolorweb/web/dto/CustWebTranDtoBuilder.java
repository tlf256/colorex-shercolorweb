package com.sherwin.shercolor.customershercolorweb.web.dto;

import com.sherwin.shercolor.common.domain.CustWebTran;

import java.util.ArrayList;
import java.util.List;


public class CustWebTranDtoBuilder {
	
	public CustWebTranDto build(CustWebTran custWebTran) throws Exception {
		CustWebTranDto result = new CustWebTranDto();
		if (custWebTran != null){
			result.setCustomerId(custWebTran.getCustomerId());
			result.setColorId(custWebTran.getColorId());
			result.setColorName(custWebTran.getColorName());
			result.setProdNbr(custWebTran.getProdNbr());
			result.setSalesNbr(custWebTran.getSalesNbr());
			result.setSizeCode(custWebTran.getSizeCode());
			result.setRgbHex(custWebTran.getRgbHex());
			try{
				result.setControlNbr(Integer.toString(custWebTran.getControlNbr()));
			}
			catch(NumberFormatException nfe){
				throw nfe;
			}
			try{
				result.setLineNbr(Integer.toString(custWebTran.getLineNbr()));
			}
			catch(NumberFormatException nfe){
				throw nfe;
			}
			try{
				result.setQuantityDispensed(Integer.toString(custWebTran.getQuantityDispensed()));
			}
			catch(NumberFormatException nfe){
				throw nfe;
			}
		}
		return result;
	}
	
	public List<CustWebTranDto> build(List<CustWebTran> orderLines) throws Exception {
		List<CustWebTranDto> result = new ArrayList<CustWebTranDto>();
		if (orderLines != null && orderLines.size() > 0){
			for (CustWebTran orderLine : orderLines){
				CustWebTranDto dto = build(orderLine);
				result.add(dto);
			}
		}
		return result;
	}

	public CustWebTran buildUpdate(CustWebTranDto custWebTranDto) throws Exception {
		CustWebTran result = new CustWebTran();
		if (custWebTranDto != null){
			result.setCustomerId(custWebTranDto.getCustomerId());
			result.setColorId(custWebTranDto.getColorId());
			result.setColorName(custWebTranDto.getColorName());
			result.setProdNbr(custWebTranDto.getProdNbr());
			result.setSalesNbr(custWebTranDto.getSalesNbr());
			result.setSizeCode(custWebTranDto.getSizeCode());
			result.setRgbHex(custWebTranDto.getRgbHex());
			try{
				result.setControlNbr(Integer.parseInt(custWebTranDto.getQuantityDispensed()));
			}
			catch(NumberFormatException nfe){
					throw nfe;
			}
			try{
				result.setControlNbr(Integer.parseInt(custWebTranDto.getControlNbr()));
			}
			catch(NumberFormatException nfe){
					throw nfe;
			}
			try{
				result.setLineNbr(Integer.parseInt(custWebTranDto.getLineNbr()));
			}
			catch(NumberFormatException nfe){
					throw nfe;
			}
		}
		return result;
	}

}
