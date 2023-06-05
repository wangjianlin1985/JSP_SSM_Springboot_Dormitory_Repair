package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Building {
    /*宿舍楼id*/
    private Integer buildingId;
    public Integer getBuildingId(){
        return buildingId;
    }
    public void setBuildingId(Integer buildingId){
        this.buildingId = buildingId;
    }

    /*宿舍楼名称*/
    @NotEmpty(message="宿舍楼名称不能为空")
    private String buildingName;
    public String getBuildingName() {
        return buildingName;
    }
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    /*宿舍楼类型*/
    @NotEmpty(message="宿舍楼类型不能为空")
    private String buildingType;
    public String getBuildingType() {
        return buildingType;
    }
    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    /*宿舍楼介绍*/
    @NotEmpty(message="宿舍楼介绍不能为空")
    private String buildingDesc;
    public String getBuildingDesc() {
        return buildingDesc;
    }
    public void setBuildingDesc(String buildingDesc) {
        this.buildingDesc = buildingDesc;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonBuilding=new JSONObject(); 
		jsonBuilding.accumulate("buildingId", this.getBuildingId());
		jsonBuilding.accumulate("buildingName", this.getBuildingName());
		jsonBuilding.accumulate("buildingType", this.getBuildingType());
		jsonBuilding.accumulate("buildingDesc", this.getBuildingDesc());
		return jsonBuilding;
    }}