package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.RepairState;

public interface RepairStateMapper {
	/*添加报修状态信息*/
	public void addRepairState(RepairState repairState) throws Exception;

	/*按照查询条件分页查询报修状态记录*/
	public ArrayList<RepairState> queryRepairState(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有报修状态记录*/
	public ArrayList<RepairState> queryRepairStateList(@Param("where") String where) throws Exception;

	/*按照查询条件的报修状态记录数*/
	public int queryRepairStateCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条报修状态记录*/
	public RepairState getRepairState(int stateId) throws Exception;

	/*更新报修状态记录*/
	public void updateRepairState(RepairState repairState) throws Exception;

	/*删除报修状态记录*/
	public void deleteRepairState(int stateId) throws Exception;

}
