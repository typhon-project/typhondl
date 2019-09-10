package org.typhon.textwarning.model;

import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class TextWarning {

	@Id
	private String id;

	private Date timeStamp;
	private String textWarning_id;
	private String text;


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
	public void setTextWarning_id(String textWarning_id){
		this.textWarning_id = textWarning_id;
	}

	public String getTextWarning_id(){
		return textWarning_id;
	}
	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

}

