package org.typhon.app.model;

import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class App {

	@Id
	private String id;

	private Date timeStamp;
	private String vehicle_id;
	private Double vehicle_position;

	private List<Integer> warnings; 
	private List<Integer> textwarnings; 

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTimeStamp(Date timeStamp){
		this.timeStamp = timeStamp;
	}

	public Date getTimeStamp(){
		return timeStamp;
	}
	public void setVehicle_id(String vehicle_id){
		this.vehicle_id = vehicle_id;
	}

	public String getVehicle_id(){
		return vehicle_id;
	}
	public void setVehicle_position(Double vehicle_position){
		this.vehicle_position = vehicle_position;
	}

	public Double getVehicle_position(){
		return vehicle_position;
	}

	public void setWarnings(List<Integer> warnings){
		this.warnings = warnings;
	}

	public List<Integer> getWarnings(){
		return warnings;
	}

	public void setTextwarnings(List<Integer> textwarnings){
		this.textwarnings = textwarnings;
	}

	public List<Integer> getTextwarnings(){
		return textwarnings;
	}
}

