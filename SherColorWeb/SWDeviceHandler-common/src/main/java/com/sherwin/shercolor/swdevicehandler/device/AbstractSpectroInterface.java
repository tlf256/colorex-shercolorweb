package com.sherwin.shercolor.swdevicehandler.device;

import com.sherwin.shercolor.swdevicehandler.domain.SpectralCurve;

public interface AbstractSpectroInterface {
	public SpectralCurve Measure();
	
	public boolean Calibrate();
}
