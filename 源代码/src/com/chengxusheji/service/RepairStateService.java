package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.RepairState;

import com.chengxusheji.mapper.RepairStateMapper;
@Service
public class RepairStateService {

	@Resource RepairStateMapper repairStateMapper;
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

    /*添加报修状态记录*/
    public void addRepairState(RepairState repairState) throws Exception {
    	repairStateMapper.addRepairState(repairState);
    }

    /*按照查询条件分页查询报修状态记录*/
    public ArrayList<RepairState> queryRepairState(int currentPage) throws Exception { 
     	String where = "where 1=1";
    	int startIndex = (currentPage-1) * this.rows;
    	return repairStateMapper.queryRepairState(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<RepairState> queryRepairState() throws Exception  { 
     	String where = "where 1=1";
    	return repairStateMapper.queryRepairStateList(where);
    }

    /*查询所有报修状态记录*/
    public ArrayList<RepairState> queryAllRepairState()  throws Exception {
        return repairStateMapper.queryRepairStateList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber() throws Exception {
     	String where = "where 1=1";
        recordNumber = repairStateMapper.queryRepairStateCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取报修状态记录*/
    public RepairState getRepairState(int stateId) throws Exception  {
        RepairState repairState = repairStateMapper.getRepairState(stateId);
        return repairState;
    }

    /*更新报修状态记录*/
    public void updateRepairState(RepairState repairState) throws Exception {
        repairStateMapper.updateRepairState(repairState);
    }

    /*删除一条报修状态记录*/
    public void deleteRepairState (int stateId) throws Exception {
        repairStateMapper.deleteRepairState(stateId);
    }

    /*删除多条报修状态信息*/
    public int deleteRepairStates (String stateIds) throws Exception {
    	String _stateIds[] = stateIds.split(",");
    	for(String _stateId: _stateIds) {
    		repairStateMapper.deleteRepairState(Integer.parseInt(_stateId));
    	}
    	return _stateIds.length;
    }
}
