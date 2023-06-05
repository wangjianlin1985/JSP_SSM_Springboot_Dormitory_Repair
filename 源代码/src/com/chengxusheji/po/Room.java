package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Room {
    /*记录id*/
    private Integer roomId;
    public Integer getRoomId(){
        return roomId;
    }
    public void setRoomId(Integer roomId){
        this.roomId = roomId;
    }

    /*所在宿舍楼*/
    private Building buildingObj;
    public Building getBuildingObj() {
        return buildingObj;
    }
    public void setBuildingObj(Building buildingObj) {
        this.buildingObj = buildingObj;
    }

    /*宿舍号*/
    @NotEmpty(message="宿舍号不能为空")
    private String roomNo;
    public String getRoomNo() {
        return roomNo;
    }
    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    /*宿舍照片*/
    private String roomPhoto;
    public String getRoomPhoto() {
        return roomPhoto;
    }
    public void setRoomPhoto(String roomPhoto) {
        this.roomPhoto = roomPhoto;
    }

    /*床位数*/
    @NotNull(message="必须输入床位数")
    private Integer personNum;
    public Integer getPersonNum() {
        return personNum;
    }
    public void setPersonNum(Integer personNum) {
        this.personNum = personNum;
    }

    /*房间详情*/
    @NotEmpty(message="房间详情不能为空")
    private String roomDesc;
    public String getRoomDesc() {
        return roomDesc;
    }
    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonRoom=new JSONObject(); 
		jsonRoom.accumulate("roomId", this.getRoomId());
		jsonRoom.accumulate("buildingObj", this.getBuildingObj().getBuildingName());
		jsonRoom.accumulate("buildingObjPri", this.getBuildingObj().getBuildingId());
		jsonRoom.accumulate("roomNo", this.getRoomNo());
		jsonRoom.accumulate("roomPhoto", this.getRoomPhoto());
		jsonRoom.accumulate("personNum", this.getPersonNum());
		jsonRoom.accumulate("roomDesc", this.getRoomDesc());
		return jsonRoom;
    }}