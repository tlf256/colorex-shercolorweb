/**
 * 9/6/2017 - BKP - code for an enhanced WebSocket to be used by Spectro and Tinter objects.
 */

function WSWrapper(devicecontext) {
        this.wserrormsg = "";
        this.wsmsg = "";
        this.isReady = "false";
        this.deviceContext = devicecontext;  
        var ws;
        var self = this;
        var timeoutVar;
        var receiver;
        
        //Detect browser and create ws
        var browser = getBrowser();
        createWS();
        

        ws.onopen = function () {
            console.log("connection opening");
            if (ws.readyState === 1) {
            	console.log("in onopen, readystate is 1");
            	self.isReady="true";
            }
        };
        
        ws.onclose = function (evt) {
            console.log("connection closing");
            self.isReady = "false";
        };
        
        ws.onmessage = function (evt) {
            // handle messages here
        	//console.log("recd msg: " + evt.data);
        	console.log("recd msg: " + encodeURI(evt.data));
        	self.wsmsg = evt.data;
        	if(self.receiver==null){
				RecdMessage();
			}
        	else{
				self.receiver();	
			}
        };
        
        ws.onerror = function (evt) {
            //console.log("connection error: " + evt.data);
            console.log("connection error: " + encodeURI(evt.data));
            console.log("event type: " + evt.type);
            console.log("event code: " + evt.code);
            if (evt.data==null) {
            	self.wserrormsg = i18n['wSWrapper.undefinedConnectionError']; 
            	self.isReady = "false";
            } else {
            	self.wserrormsg = evt.data;
            }
            if(self.receiver==null){
				RecdMessage();
			}
        	else{
				self.receiver();	
			}
        };

        this.send = function (message, callback) {
        	console.log("in wswrapper.send");
        	console.log("outbound message is " + message);
        	console.log("deviceContext is " + self.deviceContext);
            this.waitForConnection(function () {
            	this.wsmsg = "";
            	console.log("actually sending it!")
            	console.log("deviceContext is " + self.deviceContext);
                ws.send(message);
                if (typeof callback !== 'undefined') {
                  callback();
                }
            }, 1000);
        };

        this.waitForConnection = function (callback, interval) {
            if (ws.readyState === 1) {
            	console.log("in wait, readystate is 1");
            	self.isReady="true";
                callback();
            } else {
            	console.log("in wait, still waiting");
            	console.log("in wait, error is " + this.wserrormsg);
            	if (this.wserrormsg!="") {
            		console.log("cancelling send");
            		this.cancelSend();
            		return;
            	}
                var that = this;
                // optional: implement backoff for interval here
                self.timeoutVar = setTimeout(function () {
                    that.waitForConnection(callback, interval);
                }, interval);
            }
        };
        
        this.cancelSend = function() {
        	clearTimeout(this.timeoutVar);
        }

        function createWS(){
            if(browser){
                switch (browser.name.toLowerCase()) {
    
                    case 'opera':
                    case 'chrome':
                        console.log('Creating chrome instance with ws, browser is chrome|opera');
                        self.validBrowser = true;
                        ws = new WebSocket("ws://localhost:55216/"+self.deviceContext
                                /* , {
                            protocolVersion: 8,
                            origin: 'http://localhost:55216',
                            rejectUnauthorized: false
                            }
                            */
                        );
                        break;
                    
                    default:
                        console.log('Creating non-chrome instance with wss, browser is ie|safari|firefox|edge');
                        self.validBrowser = false;
                        ws = new WebSocket("wss://localhost:55217/"+self.deviceContext
                                /* , {
                            protocolVersion: 8,
                            origin: 'https://localhost:55217',
                            rejectUnauthorized: false
                            }
                            */
                        );
                        break;

                }
            }else{
                console.error('Browser object not properly constructed');
            }
        }
}