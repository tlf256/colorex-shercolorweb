package com.sherwin.shercolor.desktop.app;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.desktop.domain.CdsTranAll;
import com.sherwin.shercolor.desktop.service.CdsTranService;
import com.sherwin.shercolor.desktop.service.CdsTranServiceImpl;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.FXMLLoader;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@Import(AppConfig.class)
@ImportResource("config/spring/shercolorcommon.xml")
public class ImportCustWebTran extends Application
{
	private ConfigurableApplicationContext springContext;
//	private Parent rootNode;
	
	CdsTranService cdsTranService = new CdsTranServiceImpl();
	List<CdsTranAll> cdsTranAllList;

//	private static ApplicationContext context;
//	private BeanFactory factory;
	
//	ProductService productService;
//	SessionFactory sessionFactory;
	
	// for window drag
	private double xOffset = 0;
	private double yOffset = 0;
	
    public static void main(final String[] args)
    {
    	Application.launch(args);
//		BeanFactory factory = context = null;
//		try {
//			context = new ClassPathXmlApplicationContext("config/spring/shercolorcommon.xml");
//			factory = context;
//			System.out.println(Arrays.asList(context.getBeanDefinitionNames()));
//			
//			//ImportCustWebTran app = new ImportCustWebTran();
//			SpringApplication app = new SpringApplication(ImportCustWebTran.class);
//
////			app.productService      = (ProductService)     factory.getBean("productService");
////			app.sessionFactory      = (SessionFactory)     factory.getBean("sessionFactory");
//
//			//app.launch(args);
//			app.run(args);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		finally{
//			context = null;
//		}
    }
    
    @Override
    public void init() throws Exception {
    	SpringApplicationBuilder builder = new SpringApplicationBuilder(ImportCustWebTran.class);
    	springContext = builder.run(getParameters().getRaw().toArray(new String[0]));
//    	springContext = SpringApplication.run(ImportCustWebTran.class);
//    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SceneOne.fxml"));
//    	loader.setControllerFactory(springContext::getBean);
//    	rootNode = loader.load();
    }
    
	@Override
	@SuppressWarnings("restriction")
	public void start(final Stage stage) throws IOException {//throws Exception {
		//ApplicationContext context = new ClassPathXmlApplicationContext("config/spring/shercolorcommon.xml");
		//System.out.println(Arrays.asList(context.getBeanDefinitionNames()));
		//productService      = (ProductService)     context.getBean("productService");

		System.out.println(Arrays.asList(springContext.getBeanDefinitionNames()));
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SceneOne.fxml"));
		loader.setControllerFactory(springContext::getBean);
		
		
		//HBox hbox = loader.load();
		BorderPane borderPane = loader.load();

		Integer width = null;
		try {
			width = Integer.parseInt(getParameters().getNamed().get("width"));
		} catch (Exception e) {
		}
		if (width == null || width == 0) width = 1200;
		Integer height = null;
		try {
			height = Integer.parseInt(getParameters().getNamed().get("height"));
		} catch (Exception e) {
		}
		if (height == null || height == 0) height = 750;
		stage.setWidth(width);
		stage.setHeight(height);

		// setup drag window...
		borderPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
            }
		});
		
		borderPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
		});
		
		// setup scene
		//Scene scene = new Scene(hbox);
		Scene scene = new Scene(borderPane);
		
	    stage.setScene(scene);
		stage.setTitle("Import SherColor Web Transactions");
		stage.setAlwaysOnTop(false);
		stage.setResizable(true);
		stage.initStyle(StageStyle.TRANSPARENT); // nice no border
	    stage.show();
	}

	@Override
	public void stop() throws Exception {
		springContext.close();
	}
	
//	public ProductService getProductService(){
//		return productService;
//	}
}
