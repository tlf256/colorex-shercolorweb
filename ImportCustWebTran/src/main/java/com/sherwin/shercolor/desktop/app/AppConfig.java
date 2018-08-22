package com.sherwin.shercolor.desktop.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sherwin.shercolor.desktop.controller.SceneOneController;
import com.sherwin.shercolor.desktop.service.CdsTranService;
import com.sherwin.shercolor.desktop.service.CdsTranServiceImpl;

import javafx.fxml.Initializable;

@Configuration
public class AppConfig {

	@Bean
	public CdsTranService cdsTranService(){
		return new CdsTranServiceImpl();
	}
	
	@Bean
	public Initializable SceneOneController(){
		return new SceneOneController();
	}
}
