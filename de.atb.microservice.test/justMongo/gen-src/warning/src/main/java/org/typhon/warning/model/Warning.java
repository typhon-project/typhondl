package org.typhon.warning.model;

import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Warning {

	@Id
	private String id;

	private Date time;
	private String warning_id;
	private String warningType;
	private int severity;
	private Double area;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTime(Date time){
		this.time = time;
	}

	public Date getTime(){
		return time;
	}
	public void setWarning_id(String warning_id){
		this.warning_id = warning_id;
	}

	public String getWarning_id(){
		return warning_id;
	}
	public void setWarningType(String warningType){
		this.warningType = warningType;
	}

	public String getWarningType(){
		return warningType;
	}
	public void setSeverity(int severity){
		this.severity = severity;
	}

	public int getSeverity(){
		return severity;
	}
	public void setArea(Double area){
		this.area = area;
	}

	public Double getArea(){
		return area;
	}

}

