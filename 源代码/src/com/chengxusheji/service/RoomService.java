package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Building;
import com.chengxusheji.po.Room;

import com.chengxusheji.mapper.RoomMapper;
@Service
public class RoomService {

	@Resource RoomMapper roomMapper;
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

    /*添加宿舍记录*/
    public void addRoom(Room room) throws Exception {
    	roomMapper.addRoom(room);
    }

    /*按照查询条件分页查询宿舍记录*/
    public ArrayList<Room> queryRoom(Building buildingObj,String roomNo,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != buildingObj && buildingObj.getBuildingId()!= null && buildingObj.getBuildingId()!= 0)  where += " and t_room.buildingObj=" + buildingObj.getBuildingId();
    	if(!roomNo.equals("")) where = where + " and t_room.roomNo like '%" + roomNo + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return roomMapper.queryRoom(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Room> queryRoom(Building buildingObj,String roomNo) throws Exception  { 
     	String where = "where 1=1";
    	if(null != buildingObj && buildingObj.getBuildingId()!= null && buildingObj.getBuildingId()!= 0)  where += " and t_room.buildingObj=" + buildingObj.getBuildingId();
    	if(!roomNo.equals("")) where = where + " and t_room.roomNo like '%" + roomNo + "%'";
    	return roomMapper.queryRoomList(where);
    }

    /*查询所有宿舍记录*/
    public ArrayList<Room> queryAllRoom()  throws Exception {
        return roomMapper.queryRoomList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Building buildingObj,String roomNo) throws Exception {
     	String where = "where 1=1";
    	if(null != buildingObj && buildingObj.getBuildingId()!= null && buildingObj.getBuildingId()!= 0)  where += " and t_room.buildingObj=" + buildingObj.getBuildingId();
    	if(!roomNo.equals("")) where = where + " and t_room.roomNo like '%" + roomNo + "%'";
        recordNumber = roomMapper.queryRoomCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取宿舍记录*/
    public Room getRoom(int roomId) throws Exception  {
        Room room = roomMapper.getRoom(roomId);
        return room;
    }

    /*更新宿舍记录*/
    public void updateRoom(Room room) throws Exception {
        roomMapper.updateRoom(room);
    }

    /*删除一条宿舍记录*/
    public void deleteRoom (int roomId) throws Exception {
        roomMapper.deleteRoom(roomId);
    }

    /*删除多条宿舍信息*/
    public int deleteRooms (String roomIds) throws Exception {
    	String _roomIds[] = roomIds.split(",");
    	for(String _roomId: _roomIds) {
    		roomMapper.deleteRoom(Integer.parseInt(_roomId));
    	}
    	return _roomIds.length;
    }
}
