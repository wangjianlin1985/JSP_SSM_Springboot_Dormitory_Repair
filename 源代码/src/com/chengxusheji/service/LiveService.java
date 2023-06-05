package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Building;
import com.chengxusheji.po.Student;
import com.chengxusheji.po.Live;

import com.chengxusheji.mapper.LiveMapper;
@Service
public class LiveService {

	@Resource LiveMapper liveMapper;
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

    /*添加住宿记录*/
    public void addLive(Live live) throws Exception {
    	liveMapper.addLive(live);
    }

    /*按照查询条件分页查询住宿记录*/
    public ArrayList<Live> queryLive(Building buildingObj,String roomNo,Student studentObj,String inDate,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != buildingObj && buildingObj.getBuildingId()!= null && buildingObj.getBuildingId()!= 0)  where += " and t_live.buildingObj=" + buildingObj.getBuildingId();
    	if(!roomNo.equals("")) where = where + " and t_live.roomNo like '%" + roomNo + "%'";
    	if(null != studentObj &&  studentObj.getStudentNo() != null  && !studentObj.getStudentNo().equals(""))  where += " and t_live.studentObj='" + studentObj.getStudentNo() + "'";
    	if(!inDate.equals("")) where = where + " and t_live.inDate like '%" + inDate + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return liveMapper.queryLive(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Live> queryLive(Building buildingObj,String roomNo,Student studentObj,String inDate) throws Exception  { 
     	String where = "where 1=1";
    	if(null != buildingObj && buildingObj.getBuildingId()!= null && buildingObj.getBuildingId()!= 0)  where += " and t_live.buildingObj=" + buildingObj.getBuildingId();
    	if(!roomNo.equals("")) where = where + " and t_live.roomNo like '%" + roomNo + "%'";
    	if(null != studentObj &&  studentObj.getStudentNo() != null && !studentObj.getStudentNo().equals(""))  where += " and t_live.studentObj='" + studentObj.getStudentNo() + "'";
    	if(!inDate.equals("")) where = where + " and t_live.inDate like '%" + inDate + "%'";
    	return liveMapper.queryLiveList(where);
    }

    /*查询所有住宿记录*/
    public ArrayList<Live> queryAllLive()  throws Exception {
        return liveMapper.queryLiveList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Building buildingObj,String roomNo,Student studentObj,String inDate) throws Exception {
     	String where = "where 1=1";
    	if(null != buildingObj && buildingObj.getBuildingId()!= null && buildingObj.getBuildingId()!= 0)  where += " and t_live.buildingObj=" + buildingObj.getBuildingId();
    	if(!roomNo.equals("")) where = where + " and t_live.roomNo like '%" + roomNo + "%'";
    	if(null != studentObj &&  studentObj.getStudentNo() != null && !studentObj.getStudentNo().equals(""))  where += " and t_live.studentObj='" + studentObj.getStudentNo() + "'";
    	if(!inDate.equals("")) where = where + " and t_live.inDate like '%" + inDate + "%'";
        recordNumber = liveMapper.queryLiveCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取住宿记录*/
    public Live getLive(int liveId) throws Exception  {
        Live live = liveMapper.getLive(liveId);
        return live;
    }

    /*更新住宿记录*/
    public void updateLive(Live live) throws Exception {
        liveMapper.updateLive(live);
    }

    /*删除一条住宿记录*/
    public void deleteLive (int liveId) throws Exception {
        liveMapper.deleteLive(liveId);
    }

    /*删除多条住宿信息*/
    public int deleteLives (String liveIds) throws Exception {
    	String _liveIds[] = liveIds.split(",");
    	for(String _liveId: _liveIds) {
    		liveMapper.deleteLive(Integer.parseInt(_liveId));
    	}
    	return _liveIds.length;
    }
}
