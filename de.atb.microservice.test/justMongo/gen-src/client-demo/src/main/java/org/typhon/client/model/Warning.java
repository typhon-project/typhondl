package org.typhon.client.model;

import java.util.*;
	
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.typhon.client.service.*;

	
public class Warning {
	
private static final Logger logger = LoggerFactory.getLogger(Warning.class);

	private Date time;
	private String warning_id;
	private String warningType;
	private int severity;
	private Double area;
		
	public void setTime (Date time){
		this.time = time;
	}

	public Date getTime(){
		return time;
	}

	public void setWarning_id (String warning_id){
		this.warning_id = warning_id;
	}

	public String getWarning_id(){
		return warning_id;
	}

	public void setWarningType (String warningType){
		this.warningType = warningType;
	}

	public String getWarningType(){
		return warningType;
	}

	public void setSeverity (int severity){
		this.severity = severity;
	}

	public int getSeverity(){
		return severity;
	}

	public void setArea (Double area){
		this.area = area;
	}

	public Double getArea(){
		return area;
	}



}

