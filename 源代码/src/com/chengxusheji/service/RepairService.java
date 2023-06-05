package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Building;
import com.chengxusheji.po.RepairItem;
import com.chengxusheji.po.Student;
import com.chengxusheji.po.RepairState;
import com.chengxusheji.po.Repair;

import com.chengxusheji.mapper.RepairMapper;
@Service
public class RepairService {

	@Resource RepairMapper repairMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加报修记录*/
    public void addRepair(Repair repair) throws Exception {
    	repairMapper.addRepair(repair);
    }

    /*按照查询条件分页查询报修记录*/
    public ArrayList<Repair> queryRepair(Building buildingObj,String roomNo,RepairItem repairItemObj,Student studentObj,String addTime,RepairState repairStateObj,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != buildingObj && buildingObj.getBuildingId()!= null && buildingObj.getBuildingId()!= 0)  where += " and t_repair.buildingObj=" + buildingObj.getBuildingId();
    	if(!roomNo.equals("")) where = where + " and t_repair.roomNo like '%" + roomNo + "%'";
    	if(null != repairItemObj && repairItemObj.getItemId()!= null && repairItemObj.getItemId()!= 0)  where += " and t_repair.repairItemObj=" + repairItemObj.getItemId();
    	if(null != studentObj &&  studentObj.getStudentNo() != null  && !studentObj.getStudentNo().equals(""))  where += " and t_repair.studentObj='" + studentObj.getStudentNo() + "'";
    	if(!addTime.equals("")) where = where + " and t_repair.addTime like '%" + addTime + "%'";
    	if(null != repairStateObj && repairStateObj.getStateId()!= null && repairStateObj.getStateId()!= 0)  where += " and t_repair.repairStateObj=" + repairStateObj.getStateId();
    	int startIndex = (currentPage-1) * this.rows;
    	return repairMapper.queryRepair(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Repair> queryRepair(Building buildingObj,String roomNo,RepairItem repairItemObj,Student studentObj,String addTime,RepairState repairStateObj) throws Exception  { 
     	String where = "where 1=1";
    	if(null != buildingObj && buildingObj.getBuildingId()!= null && buildingObj.getBuildingId()!= 0)  where += " and t_repair.buildingObj=" + buildingObj.getBuildingId();
    	if(!roomNo.equals("")) where = where + " and t_repair.roomNo like '%" + roomNo + "%'";
    	if(null != repairItemObj && repairItemObj.getItemId()!= null && repairItemObj.getItemId()!= 0)  where += " and t_repair.repairItemObj=" + repairItemObj.getItemId();
    	if(null != studentObj &&  studentObj.getStudentNo() != null && !studentObj.getStudentNo().equals(""))  where += " and t_repair.studentObj='" + studentObj.getStudentNo() + "'";
    	if(!addTime.equals("")) where = where + " and t_repair.addTime like '%" + addTime + "%'";
    	if(null != repairStateObj && repairStateObj.getStateId()!= null && repairStateObj.getStateId()!= 0)  where += " and t_repair.repairStateObj=" + repairStateObj.getStateId();
    	return repairMapper.queryRepairList(where);
    }

    /*查询所有报修记录*/
    public ArrayList<Repair> queryAllRepair()  throws Exception {
        return repairMapper.queryRepairList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Building buildingObj,String roomNo,RepairItem repairItemObj,Student studentObj,String addTime,RepairState repairStateObj) throws Exception {
     	String where = "where 1=1";
    	if(null != buildingObj && buildingObj.getBuildingId()!= null && buildingObj.getBuildingId()!= 0)  where += " and t_repair.buildingObj=" + buildingObj.getBuildingId();
    	if(!roomNo.equals("")) where = where + " and t_repair.roomNo like '%" + roomNo + "%'";
    	if(null != repairItemObj && repairItemObj.getItemId()!= null && repairItemObj.getItemId()!= 0)  where += " and t_repair.repairItemObj=" + repairItemObj.getItemId();
    	if(null != studentObj &&  studentObj.getStudentNo() != null && !studentObj.getStudentNo().equals(""))  where += " and t_repair.studentObj='" + studentObj.getStudentNo() + "'";
    	if(!addTime.equals("")) where = where + " and t_repair.addTime like '%" + addTime + "%'";
    	if(null != repairStateObj && repairStateObj.getStateId()!= null && repairStateObj.getStateId()!= 0)  where += " and t_repair.repairStateObj=" + repairStateObj.getStateId();
        recordNumber = repairMapper.queryRepairCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取报修记录*/
    public Repair getRepair(int repairId) throws Exception  {
        Repair repair = repairMapper.getRepair(repairId);
        return repair;
    }

    /*更新报修记录*/
    public void updateRepair(Repair repair) throws Exception {
        repairMapper.updateRepair(repair);
    }

    /*删除一条报修记录*/
    public void deleteRepair (int repairId) throws Exception {
        repairMapper.deleteRepair(repairId);
    }

    /*删除多条报修信息*/
    public int deleteRepairs (String repairIds) throws Exception {
    	String _repairIds[] = repairIds.split(",");
    	for(String _repairId: _repairIds) {
    		repairMapper.deleteRepair(Integer.parseInt(_repairId));
    	}
    	return _repairIds.length;
    }
}
