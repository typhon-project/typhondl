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
	private List<Integer
	> warnings; 

		
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

	public List<Integer
	> getWarnings(){
		return warnings;
	}
	public void setWarnings(List<Integer
	> warnings){
		this.warnings = warnings;
	}


	public List<Warning> getWarningObj() {
		WarningService warningService = new WarningService("http://localhost:8093");
		List<Warning> result = new ArrayList<Warning>();
		for (Integer
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

	
}

