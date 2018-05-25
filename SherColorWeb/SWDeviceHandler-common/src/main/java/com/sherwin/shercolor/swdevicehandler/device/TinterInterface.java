package com.sherwin.shercolor.swdevicehandler.device;

import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;

public interface TinterInterface {
	public void Detect(TinterMessage msg);
	public void Dispense(TinterMessage msg);
	void PurgeAll(TinterMessage msg);
}
