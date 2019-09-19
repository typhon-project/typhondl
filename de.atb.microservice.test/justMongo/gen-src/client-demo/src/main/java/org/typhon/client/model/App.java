package org.typhon.client.model;

import java.util.*;
	
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.typhon.client.service.*;

	
public class App {
	
private static final Logger logger = LoggerFactory.getLogger(App.class);

	private Date timeStamp;
	private String vehicle_id;
	private Double vehicle_position;
	private List<Warning> warningsObj; 
	private List<String
	> warnings; 

	private List<TextWarning> textwarningsObj; 
	private List<String
	> textwarnings;
		
	public void setTimeStamp (Date timeStamp){
		this.timeStamp = timeStamp;
	}

	public Date getTimeStamp(){
		return timeStamp;
	}

	public void setVehicle_id (String vehicle_id){
		this.vehicle_id = vehicle_id;
	}

	public String getVehicle_id(){
		return vehicle_id;
	}

	public void setVehicle_position (Double vehicle_position){
		this.vehicle_position = vehicle_position;
	}

	public Double getVehicle_position(){
		return vehicle_position;
	}

	public List<String
	> getWarnings(){
		return warnings;
	}
	public void setWarnings(List<String
	> warnings){
		this.warnings = warnings;
	}
	public List<String
	> getTextwarnings(){
		return textwarnings;
	}
	public void setTextwarnings(List<String
	> textwarnings){
		this.textwarnings = textwarnings;
	}


	public List<Warning> getWarningObj() {
		WarningService warningService = new WarningService("http://localhost:8093");
		List<Warning> result = new ArrayList<Warning>();
		for (String
		 typeObj : warnings) {
			try {
				result.add(warningService.findById(typeObj));
			}
			catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return result;
	}

	
	public List<TextWarning> getTextWarningObj() {
		TextWarningService textwarningService = new TextWarningService("http://localhost:8092");
		List<TextWarning> result = new ArrayList<TextWarning>();
		for (String
		 typeObj : textwarnings) {
			try {
				result.add(textwarningService.findById(typeObj));
			}
			catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return result;
	}

	
}

