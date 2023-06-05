package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.RoomService;
import com.chengxusheji.po.Room;
import com.chengxusheji.service.BuildingService;
import com.chengxusheji.po.Building;

//Room管理控制层
@Controller
@RequestMapping("/Room")
public class RoomController extends BaseController {

    /*业务层对象*/
    @Resource RoomService roomService;

    @Resource BuildingService buildingService;
	@InitBinder("buildingObj")
	public void initBinderbuildingObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("buildingObj.");
	}
	@InitBinder("room")
	public void initBinderRoom(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("room.");
	}
	/*跳转到添加Room视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Room());
		/*查询所有的Building信息*/
		List<Building> buildingList = buildingService.queryAllBuilding();
		request.setAttribute("buildingList", buildingList);
		return "Room_add";
	}

	/*客户端ajax方式提交添加宿舍信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Room room, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			room.setRoomPhoto(this.handlePhotoUpload(request, "roomPhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
        roomService.addRoom(room);
        message = "宿舍添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询宿舍信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("buildingObj") Building buildingObj,String roomNo,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (roomNo == null) roomNo = "";
		if(rows != 0)roomService.setRows(rows);
		List<Room> roomList = roomService.queryRoom(buildingObj, roomNo, page);
	    /*计算总的页数和总的记录数*/
	    roomService.queryTotalPageAndRecordNumber(buildingObj, roomNo);
	    /*获取到总的页码数目*/
	    int totalPage = roomService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = roomService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Room room:roomList) {
			JSONObject jsonRoom = room.getJsonObject();
			jsonArray.put(jsonRoom);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询宿舍信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Room> roomList = roomService.queryAllRoom();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Room room:roomList) {
			JSONObject jsonRoom = new JSONObject();
			jsonRoom.accumulate("roomId", room.getRoomId());
			jsonRoom.accumulate("roomNo", room.getRoomNo());
			jsonArray.put(jsonRoom);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询宿舍信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("buildingObj") Building buildingObj,String roomNo,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (roomNo == null) roomNo = "";
		List<Room> roomList = roomService.queryRoom(buildingObj, roomNo, currentPage);
	    /*计算总的页数和总的记录数*/
	    roomService.queryTotalPageAndRecordNumber(buildingObj, roomNo);
	    /*获取到总的页码数目*/
	    int totalPage = roomService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = roomService.getRecordNumber();
	    request.setAttribute("roomList",  roomList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("buildingObj", buildingObj);
	    request.setAttribute("roomNo", roomNo);
	    List<Building> buildingList = buildingService.queryAllBuilding();
	    request.setAttribute("buildingList", buildingList);
		return "Room/room_frontquery_result"; 
	}

     /*前台查询Room信息*/
	@RequestMapping(value="/{roomId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer roomId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键roomId获取Room对象*/
        Room room = roomService.getRoom(roomId);

        List<Building> buildingList = buildingService.queryAllBuilding();
        request.setAttribute("buildingList", buildingList);
        request.setAttribute("room",  room);
        return "Room/room_frontshow";
	}

	/*ajax方式显示宿舍修改jsp视图页*/
	@RequestMapping(value="/{roomId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer roomId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键roomId获取Room对象*/
        Room room = roomService.getRoom(roomId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonRoom = room.getJsonObject();
		out.println(jsonRoom.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新宿舍信息*/
	@RequestMapping(value = "/{roomId}/update", method = RequestMethod.POST)
	public void update(@Validated Room room, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String roomPhotoFileName = this.handlePhotoUpload(request, "roomPhotoFile");
		if(!roomPhotoFileName.equals("upload/NoImage.jpg"))room.setRoomPhoto(roomPhotoFileName); 


		try {
			roomService.updateRoom(room);
			message = "宿舍更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "宿舍更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除宿舍信息*/
	@RequestMapping(value="/{roomId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer roomId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  roomService.deleteRoom(roomId);
	            request.setAttribute("message", "宿舍删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "宿舍删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条宿舍记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String roomIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = roomService.deleteRooms(roomIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出宿舍信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("buildingObj") Building buildingObj,String roomNo, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(roomNo == null) roomNo = "";
        List<Room> roomList = roomService.queryRoom(buildingObj,roomNo);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Room信息记录"; 
        String[] headers = { "所在宿舍楼","宿舍号","宿舍照片","床位数"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<roomList.size();i++) {
        	Room room = roomList.get(i); 
        	dataset.add(new String[]{room.getBuildingObj().getBuildingName(),room.getRoomNo(),room.getRoomPhoto(),room.getPersonNum() + ""});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Room.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,_title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
