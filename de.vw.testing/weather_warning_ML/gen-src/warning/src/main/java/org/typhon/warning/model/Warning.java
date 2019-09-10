package org.typhon.warning.model;

import java.util.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Warning {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private Date time; 
	private String warning_id; 
	private String warningType; 
	private Int severity; 
	private Double area; 

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
		
	public void setTime(Date time){
		this.time = time;
	}

	public Date getTime (){
		return time;
	}
	public void setWarning_id(String warning_id){
		this.warning_id = warning_id;
	}

	public String getWarning_id (){
		return warning_id;
	}
	public void setWarningType(String warningType){
		this.warningType = warningType;
	}

	public String getWarningType (){
		return warningType;
	}
	public void setSeverity(Int severity){
		this.severity = severity;
	}

	public Int getSeverity (){
		return severity;
	}
	public void setArea(Double area){
		this.area = area;
	}

	public Double getArea (){
		return area;
	}

}

