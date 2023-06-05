package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Building;

import com.chengxusheji.mapper.BuildingMapper;
@Service
public class BuildingService {

	@Resource BuildingMapper buildingMapper;
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

    /*添加宿舍楼记录*/
    public void addBuilding(Building building) throws Exception {
    	buildingMapper.addBuilding(building);
    }

    /*按照查询条件分页查询宿舍楼记录*/
    public ArrayList<Building> queryBuilding(String buildingName,String buildingType,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!buildingName.equals("")) where = where + " and t_building.buildingName like '%" + buildingName + "%'";
    	if(!buildingType.equals("")) where = where + " and t_building.buildingType like '%" + buildingType + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return buildingMapper.queryBuilding(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Building> queryBuilding(String buildingName,String buildingType) throws Exception  { 
     	String where = "where 1=1";
    	if(!buildingName.equals("")) where = where + " and t_building.buildingName like '%" + buildingName + "%'";
    	if(!buildingType.equals("")) where = where + " and t_building.buildingType like '%" + buildingType + "%'";
    	return buildingMapper.queryBuildingList(where);
    }

    /*查询所有宿舍楼记录*/
    public ArrayList<Building> queryAllBuilding()  throws Exception {
        return buildingMapper.queryBuildingList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String buildingName,String buildingType) throws Exception {
     	String where = "where 1=1";
    	if(!buildingName.equals("")) where = where + " and t_building.buildingName like '%" + buildingName + "%'";
    	if(!buildingType.equals("")) where = where + " and t_building.buildingType like '%" + buildingType + "%'";
        recordNumber = buildingMapper.queryBuildingCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取宿舍楼记录*/
    public Building getBuilding(int buildingId) throws Exception  {
        Building building = buildingMapper.getBuilding(buildingId);
        return building;
    }

    /*更新宿舍楼记录*/
    public void updateBuilding(Building building) throws Exception {
        buildingMapper.updateBuilding(building);
    }

    /*删除一条宿舍楼记录*/
    public void deleteBuilding (int buildingId) throws Exception {
        buildingMapper.deleteBuilding(buildingId);
    }

    /*删除多条宿舍楼信息*/
    public int deleteBuildings (String buildingIds) throws Exception {
    	String _buildingIds[] = buildingIds.split(",");
    	for(String _buildingId: _buildingIds) {
    		buildingMapper.deleteBuilding(Integer.parseInt(_buildingId));
    	}
    	return _buildingIds.length;
    }
}
