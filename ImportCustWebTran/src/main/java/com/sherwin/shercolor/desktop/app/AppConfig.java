package com.sherwin.shercolor.desktop.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import com.sherwin.shercolor.desktop.controller.SceneOneController;
import com.sherwin.shercolor.desktop.service.CdsTranService;
import com.sherwin.shercolor.desktop.service.CdsTranServiceImpl;
import com.sherwin.shercolor.util.domain.LocaleContainer;

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
	
	@Primary
	@Bean
	@Scope("singleton")
	public LocaleContainer localeContainer() {
		return new LocaleContainer("en", "US");
	}
}
