package org.typhon.app.model;

import java.util.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class App {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private Date timeStamp; 
	private String vehicle_id; 
	private Double vehicle_position; 

	@ElementCollection
	private List<String> warnings; 
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
		
	public void setTimeStamp(Date timeStamp){
		this.timeStamp = timeStamp;
	}

	public Date getTimeStamp (){
		return timeStamp;
	}
	public void setVehicle_id(String vehicle_id){
		this.vehicle_id = vehicle_id;
	}

	public String getVehicle_id (){
		return vehicle_id;
	}
	public void setVehicle_position(Double vehicle_position){
		this.vehicle_position = vehicle_position;
	}

	public Double getVehicle_position (){
		return vehicle_position;
	}

	public void setWarnings(List<String> warnings){
		this.warnings = warnings;
	}

	public List<String> getWarnings(){
		return warnings;
	}
}

