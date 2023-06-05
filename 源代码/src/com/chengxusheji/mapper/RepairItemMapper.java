package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.RepairItem;

public interface RepairItemMapper {
	/*添加报修项目信息*/
	public void addRepairItem(RepairItem repairItem) throws Exception;

	/*按照查询条件分页查询报修项目记录*/
	public ArrayList<RepairItem> queryRepairItem(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有报修项目记录*/
	public ArrayList<RepairItem> queryRepairItemList(@Param("where") String where) throws Exception;

	/*按照查询条件的报修项目记录数*/
	public int queryRepairItemCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条报修项目记录*/
	public RepairItem getRepairItem(int itemId) throws Exception;

	/*更新报修项目记录*/
	public void updateRepairItem(RepairItem repairItem) throws Exception;

	/*删除报修项目记录*/
	public void deleteRepairItem(int itemId) throws Exception;

}
