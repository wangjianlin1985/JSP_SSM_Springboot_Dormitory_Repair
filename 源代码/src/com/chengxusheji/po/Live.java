package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Live {
    /*记录id*/
    private Integer liveId;
    public Integer getLiveId(){
        return liveId;
    }
    public void setLiveId(Integer liveId){
        this.liveId = liveId;
    }

    /*入住宿舍楼*/
    private Building buildingObj;
    public Building getBuildingObj() {
        return buildingObj;
    }
    public void setBuildingObj(Building buildingObj) {
        this.buildingObj = buildingObj;
    }

    /*入住宿舍号*/
    @NotEmpty(message="入住宿舍号不能为空")
    private String roomNo;
    public String getRoomNo() {
        return roomNo;
    }
    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    /*入住学生*/
    private Student studentObj;
    public Student getStudentObj() {
        return studentObj;
    }
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }

    /*入住日期*/
    @NotEmpty(message="入住日期不能为空")
    private String inDate;
    public String getInDate() {
        return inDate;
    }
    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    /*备注信息*/
    private String liveMemo;
    public String getLiveMemo() {
        return liveMemo;
    }
    public void setLiveMemo(String liveMemo) {
        this.liveMemo = liveMemo;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonLive=new JSONObject(); 
		jsonLive.accumulate("liveId", this.getLiveId());
		jsonLive.accumulate("buildingObj", this.getBuildingObj().getBuildingName());
		jsonLive.accumulate("buildingObjPri", this.getBuildingObj().getBuildingId());
		jsonLive.accumulate("roomNo", this.getRoomNo());
		jsonLive.accumulate("studentObj", this.getStudentObj().getName());
		jsonLive.accumulate("studentObjPri", this.getStudentObj().getStudentNo());
		jsonLive.accumulate("inDate", this.getInDate().length()>19?this.getInDate().substring(0,19):this.getInDate());
		jsonLive.accumulate("liveMemo", this.getLiveMemo());
		return jsonLive;
    }}