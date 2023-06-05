package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Live;

public interface LiveMapper {
	/*添加住宿信息*/
	public void addLive(Live live) throws Exception;

	/*按照查询条件分页查询住宿记录*/
	public ArrayList<Live> queryLive(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有住宿记录*/
	public ArrayList<Live> queryLiveList(@Param("where") String where) throws Exception;

	/*按照查询条件的住宿记录数*/
	public int queryLiveCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条住宿记录*/
	public Live getLive(int liveId) throws Exception;

	/*更新住宿记录*/
	public void updateLive(Live live) throws Exception;

	/*删除住宿记录*/
	public void deleteLive(int liveId) throws Exception;

}
