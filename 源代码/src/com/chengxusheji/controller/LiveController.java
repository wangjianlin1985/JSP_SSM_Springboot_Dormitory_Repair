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
import com.chengxusheji.service.LiveService;
import com.chengxusheji.po.Live;
import com.chengxusheji.service.BuildingService;
import com.chengxusheji.po.Building;
import com.chengxusheji.service.StudentService;
import com.chengxusheji.po.Student;

//Live管理控制层
@Controller
@RequestMapping("/Live")
public class LiveController extends BaseController {

    /*业务层对象*/
    @Resource LiveService liveService;

    @Resource BuildingService buildingService;
    @Resource StudentService studentService;
	@InitBinder("buildingObj")
	public void initBinderbuildingObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("buildingObj.");
	}
	@InitBinder("studentObj")
	public void initBinderstudentObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("studentObj.");
	}
	@InitBinder("live")
	public void initBinderLive(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("live.");
	}
	/*跳转到添加Live视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Live());
		/*查询所有的Building信息*/
		List<Building> buildingList = buildingService.queryAllBuilding();
		request.setAttribute("buildingList", buildingList);
		/*查询所有的Student信息*/
		List<Student> studentList = studentService.queryAllStudent();
		request.setAttribute("studentList", studentList);
		return "Live_add";
	}

	/*客户端ajax方式提交添加住宿信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Live live, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        liveService.addLive(live);
        message = "住宿添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询住宿信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("buildingObj") Building buildingObj,String roomNo,@ModelAttribute("studentObj") Student studentObj,String inDate,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (roomNo == null) roomNo = "";
		if (inDate == null) inDate = "";
		if(rows != 0)liveService.setRows(rows);
		List<Live> liveList = liveService.queryLive(buildingObj, roomNo, studentObj, inDate, page);
	    /*计算总的页数和总的记录数*/
	    liveService.queryTotalPageAndRecordNumber(buildingObj, roomNo, studentObj, inDate);
	    /*获取到总的页码数目*/
	    int totalPage = liveService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = liveService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Live live:liveList) {
			JSONObject jsonLive = live.getJsonObject();
			jsonArray.put(jsonLive);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询住宿信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Live> liveList = liveService.queryAllLive();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Live live:liveList) {
			JSONObject jsonLive = new JSONObject();
			jsonLive.accumulate("liveId", live.getLiveId());
			jsonArray.put(jsonLive);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询住宿信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("buildingObj") Building buildingObj,String roomNo,@ModelAttribute("studentObj") Student studentObj,String inDate,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (roomNo == null) roomNo = "";
		if (inDate == null) inDate = "";
		List<Live> liveList = liveService.queryLive(buildingObj, roomNo, studentObj, inDate, currentPage);
	    /*计算总的页数和总的记录数*/
	    liveService.queryTotalPageAndRecordNumber(buildingObj, roomNo, studentObj, inDate);
	    /*获取到总的页码数目*/
	    int totalPage = liveService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = liveService.getRecordNumber();
	    request.setAttribute("liveList",  liveList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("buildingObj", buildingObj);
	    request.setAttribute("roomNo", roomNo);
	    request.setAttribute("studentObj", studentObj);
	    request.setAttribute("inDate", inDate);
	    List<Building> buildingList = buildingService.queryAllBuilding();
	    request.setAttribute("buildingList", buildingList);
	    List<Student> studentList = studentService.queryAllStudent();
	    request.setAttribute("studentList", studentList);
		return "Live/live_frontquery_result"; 
	}
	
	/*前台按照查询条件分页查询住宿信息*/
	@RequestMapping(value = { "/userFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String userFrontlist(@ModelAttribute("buildingObj") Building buildingObj,String roomNo,@ModelAttribute("studentObj") Student studentObj,String inDate,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (roomNo == null) roomNo = "";
		if (inDate == null) inDate = "";
		studentObj = new Student();
		studentObj.setStudentNo(session.getAttribute("user_name").toString());
		
		List<Live> liveList = liveService.queryLive(buildingObj, roomNo, studentObj, inDate, currentPage);
	    /*计算总的页数和总的记录数*/
	    liveService.queryTotalPageAndRecordNumber(buildingObj, roomNo, studentObj, inDate);
	    /*获取到总的页码数目*/
	    int totalPage = liveService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = liveService.getRecordNumber();
	    request.setAttribute("liveList",  liveList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("buildingObj", buildingObj);
	    request.setAttribute("roomNo", roomNo);
	    request.setAttribute("studentObj", studentObj);
	    request.setAttribute("inDate", inDate);
	    List<Building> buildingList = buildingService.queryAllBuilding();
	    request.setAttribute("buildingList", buildingList);
	    List<Student> studentList = studentService.queryAllStudent();
	    request.setAttribute("studentList", studentList);
		return "Live/live_userFrontquery_result"; 
	}
	

     /*前台查询Live信息*/
	@RequestMapping(value="/{liveId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer liveId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键liveId获取Live对象*/
        Live live = liveService.getLive(liveId);

        List<Building> buildingList = buildingService.queryAllBuilding();
        request.setAttribute("buildingList", buildingList);
        List<Student> studentList = studentService.queryAllStudent();
        request.setAttribute("studentList", studentList);
        request.setAttribute("live",  live);
        return "Live/live_frontshow";
	}

	/*ajax方式显示住宿修改jsp视图页*/
	@RequestMapping(value="/{liveId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer liveId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键liveId获取Live对象*/
        Live live = liveService.getLive(liveId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonLive = live.getJsonObject();
		out.println(jsonLive.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新住宿信息*/
	@RequestMapping(value = "/{liveId}/update", method = RequestMethod.POST)
	public void update(@Validated Live live, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			liveService.updateLive(live);
			message = "住宿更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "住宿更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除住宿信息*/
	@RequestMapping(value="/{liveId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer liveId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  liveService.deleteLive(liveId);
	            request.setAttribute("message", "住宿删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "住宿删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条住宿记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String liveIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = liveService.deleteLives(liveIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出住宿信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("buildingObj") Building buildingObj,String roomNo,@ModelAttribute("studentObj") Student studentObj,String inDate, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(roomNo == null) roomNo = "";
        if(inDate == null) inDate = "";
        List<Live> liveList = liveService.queryLive(buildingObj,roomNo,studentObj,inDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Live信息记录"; 
        String[] headers = { "记录id","入住宿舍楼","入住宿舍号","入住学生","入住日期"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<liveList.size();i++) {
        	Live live = liveList.get(i); 
        	dataset.add(new String[]{live.getLiveId() + "",live.getBuildingObj().getBuildingName(),live.getRoomNo(),live.getStudentObj().getName(),live.getInDate()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Live.xls");//filename是下载的xls的名，建议最好用英文 
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
