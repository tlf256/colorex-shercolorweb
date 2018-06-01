package application;
	
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sherwin.shercolor.calloader.controller.SherColorLoaderController;
import com.sherwin.shercolor.common.dao.CustWebEcalDao;
import com.sherwin.shercolor.common.domain.CustWebEcal;
import com.sherwin.shercolor.common.service.EcalService;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;



public class SherColorCalLoader extends Application {
   
	SherColorLoaderController controller = new SherColorLoaderController();

    File selectedFile = null;
    TextField customerTextField = new TextField();
	Label lFileName = new Label("");
	Label lColorantName = new Label("");
	Label lModelName = new Label("");
	Label lSerialNbr = new Label("");
	Label lUpld = new Label("");
	Label ltUpld = new Label("");
	Button btn = new Button("Choose File");

   
    private void setFields(){
    	lFileName.setText(selectedFile.getName());
    	lColorantName.setText(this.getController().getEcal().getColorantid());
    	lModelName.setText(this.getController().getEcal().getTintermodel());
    	lSerialNbr.setText(this.getController().getEcal().getTinterserial());
    	lUpld.setText(this.getController().getEcal().getUploaddate());
    	ltUpld.setText(this.getController().getEcal().getUploadtime());
    	btn.setOnAction(uploadHandler);
    	btn.setText("Upload");
    }
    
    EventHandler<ActionEvent> chooserHandler = new EventHandler<ActionEvent>() {
		 
	    @Override
	    public void handle(ActionEvent e) {
	    	FileChooser fileChooser = new FileChooser();
			
			selectedFile = fileChooser.showOpenDialog(null);
				if (selectedFile != null) {
				getController().fillFields(selectedFile);
				setFields();
					
			
			} 
	    }
	};
	EventHandler<ActionEvent> uploadHandler = new EventHandler<ActionEvent>() {
		 
	    @Override
	    public void handle(ActionEvent e) {
	    	if(customerTextField.getText().length()>0){
	    		getController().UploadEcal(customerTextField.getText());
	    	}
			
			}
	
	    };
	
    
	
	@Override
	public void start(Stage primaryStage) {
		ApplicationContext context = 
	            new ClassPathXmlApplicationContext("classpath*:config/spring/shercolorcommon.xml");
	       System.out.println("The method of my Bean: " + getController().getService().toString());
		try {
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25, 25, 25, 25));
			Text scenetitle = new Text("Welcome");
			scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
			grid.add(scenetitle, 0, 0, 2, 1);

			Label customerName = new Label("customer Id:");
			grid.add(customerName, 0, 1);
			
			grid.add(customerTextField, 1, 1);
			
			Label fLabel = new Label("File Name:");
			grid.add(fLabel, 0, 2);
		
			grid.add(lFileName, 1, 2);
			
			Label cLabel = new Label("Colorant ID:");
			grid.add(cLabel, 0, 3);
			
			grid.add(lColorantName, 1, 3);
			
			Label mLabel = new Label("Tinter Model:");
			grid.add(mLabel, 0, 4);
			
			grid.add(lModelName, 1, 4);
			
			
			Label sLabel = new Label("Tinter Serial:");
			grid.add(sLabel, 0, 5);
					grid.add(lSerialNbr, 1, 5);
			
			Label uLabel = new Label("Upload Date:");
			grid.add(uLabel, 0, 6);
		
			grid.add(lUpld, 1, 6);
			
			Label tLabel = new Label("Upload Time:");
			grid.add(tLabel, 0, 7);
		
			grid.add(ltUpld, 1, 7);
				
		
			HBox hbBtn = new HBox(10);
			hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn.getChildren().add(btn);
			grid.add(hbBtn, 1, 8);
			
			
			btn.setOnAction(chooserHandler);
		
	
		
			Scene scene = new Scene(grid,1200,400);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public SherColorLoaderController getController() {
		return controller;
	}

	public void setController(SherColorLoaderController controller) {
		this.controller = controller;
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(File selectedFile) {
		this.selectedFile = selectedFile;
	}

	public static void main(String[] args) {
		
		launch(args);
	}
	
}
