package org.typhon.client.model.dto;

import java.util.*;
	
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
	
@JsonIgnoreProperties(ignoreUnknown = true)
public class TextWarningDTO {

	private Date timeStamp;
	private String textWarning_id;
	private String text;
		
	public void setTimeStamp (Date timeStamp){
		this.timeStamp = timeStamp;
	}

	public Date getTimeStamp(){
		return timeStamp;
	}

	public void setTextWarning_id (String textWarning_id){
		this.textWarning_id = textWarning_id;
	}

	public String getTextWarning_id(){
		return textWarning_id;
	}

	public void setText (String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}


}
