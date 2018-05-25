package com.sherwin.shercolor.swdevicehandler.domain;

import java.util.HashMap;
import java.util.List;

public class CanisterMap {
	HashMap<Long,String> map = new HashMap<Long,String>();
	HashMap<String,Long> codeMap = new HashMap<String,Long>();

	public CanisterMap(List<Canister> canisters){
		for(Canister can:canisters){
			map.put(can.getPump(), can.getCode());
			codeMap.put(can.getCode(), can.getPump());
			System.out.println("Adding " + can.getPump() + can.getCode() + " to CanisterMap");
		}
	}
	public CanisterMap(){
		//default map
		//map.put((long) 1, "R3");
		//map.put((long) 10, "Y3");
	}
	public HashMap<Long,String> getMap() {
		return map;
	}

	public void setMap(HashMap<Long,String> map) {
		this.map = map;
	}
	public HashMap<String, Long> getCodeMap() {
		return codeMap;
	}
	public void setCodeMap(HashMap<String, Long> codeMap) {
		this.codeMap = codeMap;
	}
	
	
	
}