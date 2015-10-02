package com.zhongzhang.randomstudent.bean;

import java.text.NumberFormat;

import android.R.integer;

public class StudentBean {
	
	private String firstName;
	private String secondName;
	private String gender;
	private boolean attendance;
	private boolean isCalled;
	private int callTime;
	private int correctTime;
	private int inCorrectTime;
	private String headImage;
	private String correctRate;
	
	public StudentBean() {
		inits();
	}

	private void inits() {
		// TODO Auto-generated method stub
		headImage = "";
		firstName = "";
		secondName = "";
		gender = "male";
		attendance = false;
		callTime = 0;
		correctTime = 0;
		inCorrectTime = 0;
		correctRate = "100%";
		isCalled = false;
	};
	
	public String getCorrectRate() {
		double shu;
		if (inCorrectTime == 0) {
			shu = 1;
		}else {
			shu = (double)correctTime/(correctTime + inCorrectTime);
		}
		NumberFormat nt = NumberFormat.getPercentInstance();
		nt.setMinimumFractionDigits(0);
		return nt.format(shu);
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public void setFirstName(String firtName){
		this.firstName = firtName;
	}
	
	public String getSecondName(){
		return secondName;
	}
	
	public void setSecondName(String secondName){
		this.secondName = secondName;
	}
	
	public String getGender(){
		return gender;
	}
	
	public void setGender(String gender){
		this.gender = gender;
	}
	
	public boolean getAttendance(){
		return attendance;
	}
	
	public void setAttendance(boolean attendance) {
		this.attendance = attendance;
	}
	
	public int getCallTime(){
		return callTime;
	}
	
	public void setCallTime(int callTime) {
		this.callTime = callTime;
	}
	
	public int getCorrectTime(){
		return correctTime;
	}
	
	public void setCorrectTime(int correctTime){
		this.correctTime = correctTime;
	}
	
	public int getIncorrectTime(){
		return inCorrectTime;
	}
	
	public void setIncorrectTime(int inCorrectTime){
		this.inCorrectTime = inCorrectTime;
	}
	
	public String getHeadImage(){
		return this.headImage;
	}
	
	public void setHeadImage(String headImage){
		this.headImage = headImage;
	}
	
	public boolean getIsCalled(){
		return isCalled;
	}
	
	public void setIsCalled(boolean isCalled) {
		this.isCalled = isCalled;
	}
	
}
