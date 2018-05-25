package com.sherwin.shercolor.swdevicehandler.device;

import java.util.concurrent.BlockingQueue;

import com.sherwin.shercolor.swdevicehandler.domain.Message;

public interface AbstractTinterInterface  {
	public BlockingQueue<Message> getToDeviceQueue();
	public BlockingQueue<Message> getFromDeviceQueue();
}
