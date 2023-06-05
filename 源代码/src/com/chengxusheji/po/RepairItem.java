package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class RepairItem {
    /*项目id*/
    private Integer itemId;
    public Integer getItemId(){
        return itemId;
    }
    public void setItemId(Integer itemId){
        this.itemId = itemId;
    }

    /*项目名称*/
    @NotEmpty(message="项目名称不能为空")
    private String itemName;
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonRepairItem=new JSONObject(); 
		jsonRepairItem.accumulate("itemId", this.getItemId());
		jsonRepairItem.accumulate("itemName", this.getItemName());
		return jsonRepairItem;
    }}