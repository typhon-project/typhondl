package org.typhon.client.model.dto;

import java.util.*;
	
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
	
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppDTO {

	private Date timeStamp;
	private String vehicle_id;
	private Double vehicle_position;
	private List<Integer
	> warningsObj; 
		
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
		return warningsObj;
	}
	public void setWarnings(List<Integer
	> warningsObj){
		this.warningsObj = warningsObj;
	}

}
