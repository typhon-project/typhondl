package org.typhon.client.model.dto;

import java.util.*;
	
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
	
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarningDTO {

	private Date time;
	private String warning_id;
	private String warningType;
	private Int severity;
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

	public void setSeverity (Int severity){
		this.severity = severity;
	}

	public Int getSeverity(){
		return severity;
	}

	public void setArea (Double area){
		this.area = area;
	}

	public Double getArea(){
		return area;
	}


}
