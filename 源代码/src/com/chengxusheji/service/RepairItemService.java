package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.RepairItem;

import com.chengxusheji.mapper.RepairItemMapper;
@Service
public class RepairItemService {

	@Resource RepairItemMapper repairItemMapper;
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

    /*添加报修项目记录*/
    public void addRepairItem(RepairItem repairItem) throws Exception {
    	repairItemMapper.addRepairItem(repairItem);
    }

    /*按照查询条件分页查询报修项目记录*/
    public ArrayList<RepairItem> queryRepairItem(int currentPage) throws Exception { 
     	String where = "where 1=1";
    	int startIndex = (currentPage-1) * this.rows;
    	return repairItemMapper.queryRepairItem(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<RepairItem> queryRepairItem() throws Exception  { 
     	String where = "where 1=1";
    	return repairItemMapper.queryRepairItemList(where);
    }

    /*查询所有报修项目记录*/
    public ArrayList<RepairItem> queryAllRepairItem()  throws Exception {
        return repairItemMapper.queryRepairItemList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber() throws Exception {
     	String where = "where 1=1";
        recordNumber = repairItemMapper.queryRepairItemCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取报修项目记录*/
    public RepairItem getRepairItem(int itemId) throws Exception  {
        RepairItem repairItem = repairItemMapper.getRepairItem(itemId);
        return repairItem;
    }

    /*更新报修项目记录*/
    public void updateRepairItem(RepairItem repairItem) throws Exception {
        repairItemMapper.updateRepairItem(repairItem);
    }

    /*删除一条报修项目记录*/
    public void deleteRepairItem (int itemId) throws Exception {
        repairItemMapper.deleteRepairItem(itemId);
    }

    /*删除多条报修项目信息*/
    public int deleteRepairItems (String itemIds) throws Exception {
    	String _itemIds[] = itemIds.split(",");
    	for(String _itemId: _itemIds) {
    		repairItemMapper.deleteRepairItem(Integer.parseInt(_itemId));
    	}
    	return _itemIds.length;
    }
}
