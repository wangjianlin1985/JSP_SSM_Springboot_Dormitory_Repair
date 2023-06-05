package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Room;

public interface RoomMapper {
	/*添加宿舍信息*/
	public void addRoom(Room room) throws Exception;

	/*按照查询条件分页查询宿舍记录*/
	public ArrayList<Room> queryRoom(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有宿舍记录*/
	public ArrayList<Room> queryRoomList(@Param("where") String where) throws Exception;

	/*按照查询条件的宿舍记录数*/
	public int queryRoomCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条宿舍记录*/
	public Room getRoom(int roomId) throws Exception;

	/*更新宿舍记录*/
	public void updateRoom(Room room) throws Exception;

	/*删除宿舍记录*/
	public void deleteRoom(int roomId) throws Exception;

}
