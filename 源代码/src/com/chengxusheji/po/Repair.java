package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Repair {
    /*报修id*/
    private Integer repairId;
    public Integer getRepairId(){
        return repairId;
    }
    public void setRepairId(Integer repairId){
        this.repairId = repairId;
    }

    /*宿舍楼*/
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

    /*报修项目*/
    private RepairItem repairItemObj;
    public RepairItem getRepairItemObj() {
        return repairItemObj;
    }
    public void setRepairItemObj(RepairItem repairItemObj) {
        this.repairItemObj = repairItemObj;
    }

    /*问题描述*/
    @NotEmpty(message="问题描述不能为空")
    private String repairDesc;
    public String getRepairDesc() {
        return repairDesc;
    }
    public void setRepairDesc(String repairDesc) {
        this.repairDesc = repairDesc;
    }

    /*上报学生*/
    private Student studentObj;
    public Student getStudentObj() {
        return studentObj;
    }
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }

    /*上报时间*/
    @NotEmpty(message="上报时间不能为空")
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    /*维修状态*/
    private RepairState repairStateObj;
    public RepairState getRepairStateObj() {
        return repairStateObj;
    }
    public void setRepairStateObj(RepairState repairStateObj) {
        this.repairStateObj = repairStateObj;
    }

    /*处理结果*/
    private String handleResult;
    public String getHandleResult() {
        return handleResult;
    }
    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonRepair=new JSONObject(); 
		jsonRepair.accumulate("repairId", this.getRepairId());
		jsonRepair.accumulate("buildingObj", this.getBuildingObj().getBuildingName());
		jsonRepair.accumulate("buildingObjPri", this.getBuildingObj().getBuildingId());
		jsonRepair.accumulate("roomNo", this.getRoomNo());
		jsonRepair.accumulate("repairItemObj", this.getRepairItemObj().getItemName());
		jsonRepair.accumulate("repairItemObjPri", this.getRepairItemObj().getItemId());
		jsonRepair.accumulate("repairDesc", this.getRepairDesc());
		jsonRepair.accumulate("studentObj", this.getStudentObj().getName());
		jsonRepair.accumulate("studentObjPri", this.getStudentObj().getStudentNo());
		jsonRepair.accumulate("addTime", this.getAddTime().length()>19?this.getAddTime().substring(0,19):this.getAddTime());
		jsonRepair.accumulate("repairStateObj", this.getRepairStateObj().getStateName());
		jsonRepair.accumulate("repairStateObjPri", this.getRepairStateObj().getStateId());
		jsonRepair.accumulate("handleResult", this.getHandleResult());
		return jsonRepair;
    }}