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
import com.chengxusheji.service.RepairService;
import com.chengxusheji.po.Repair;
import com.chengxusheji.service.BuildingService;
import com.chengxusheji.po.Building;
import com.chengxusheji.service.RepairItemService;
import com.chengxusheji.po.RepairItem;
import com.chengxusheji.service.RepairStateService;
import com.chengxusheji.po.RepairState;
import com.chengxusheji.service.StudentService;
import com.chengxusheji.po.Student;

//Repair管理控制层
@Controller
@RequestMapping("/Repair")
public class RepairController extends BaseController {

    /*业务层对象*/
    @Resource RepairService repairService;

    @Resource BuildingService buildingService;
    @Resource RepairItemService repairItemService;
    @Resource RepairStateService repairStateService;
    @Resource StudentService studentService;
	@InitBinder("buildingObj")
	public void initBinderbuildingObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("buildingObj.");
	}
	@InitBinder("repairItemObj")
	public void initBinderrepairItemObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("repairItemObj.");
	}
	@InitBinder("studentObj")
	public void initBinderstudentObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("studentObj.");
	}
	@InitBinder("repairStateObj")
	public void initBinderrepairStateObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("repairStateObj.");
	}
	@InitBinder("repair")
	public void initBinderRepair(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("repair.");
	}
	/*跳转到添加Repair视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Repair());
		/*查询所有的Building信息*/
		List<Building> buildingList = buildingService.queryAllBuilding();
		request.setAttribute("buildingList", buildingList);
		/*查询所有的RepairItem信息*/
		List<RepairItem> repairItemList = repairItemService.queryAllRepairItem();
		request.setAttribute("repairItemList", repairItemList);
		/*查询所有的RepairState信息*/
		List<RepairState> repairStateList = repairStateService.queryAllRepairState();
		request.setAttribute("repairStateList", repairStateList);
		/*查询所有的Student信息*/
		List<Student> studentList = studentService.queryAllStudent();
		request.setAttribute("studentList", studentList);
		return "Repair_add";
	}

	/*客户端ajax方式提交添加报修信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Repair repair, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        repairService.addRepair(repair);
        message = "报修添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	/*客户端ajax方式提交添加报修信息*/
	@RequestMapping(value = "/userAdd", method = RequestMethod.POST)
	public void userAdd(Repair repair, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		Student studentObj = new Student();
		studentObj.setStudentNo(session.getAttribute("user_name").toString());
		repair.setStudentObj(studentObj);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		repair.setAddTime(sdf.format(new java.util.Date()));
		
		repair.setHandleResult("--");
		
		
        repairService.addRepair(repair);
        message = "报修添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	/*ajax方式按照查询条件分页查询报修信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("buildingObj") Building buildingObj,String roomNo,@ModelAttribute("repairItemObj") RepairItem repairItemObj,@ModelAttribute("studentObj") Student studentObj,String addTime,@ModelAttribute("repairStateObj") RepairState repairStateObj,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (roomNo == null) roomNo = "";
		if (addTime == null) addTime = "";
		if(rows != 0)repairService.setRows(rows);
		List<Repair> repairList = repairService.queryRepair(buildingObj, roomNo, repairItemObj, studentObj, addTime, repairStateObj, page);
	    /*计算总的页数和总的记录数*/
	    repairService.queryTotalPageAndRecordNumber(buildingObj, roomNo, repairItemObj, studentObj, addTime, repairStateObj);
	    /*获取到总的页码数目*/
	    int totalPage = repairService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = repairService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Repair repair:repairList) {
			JSONObject jsonRepair = repair.getJsonObject();
			jsonArray.put(jsonRepair);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询报修信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Repair> repairList = repairService.queryAllRepair();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Repair repair:repairList) {
			JSONObject jsonRepair = new JSONObject();
			jsonRepair.accumulate("repairId", repair.getRepairId());
			jsonRepair.accumulate("repairDesc", repair.getRepairDesc());
			jsonArray.put(jsonRepair);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询报修信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("buildingObj") Building buildingObj,String roomNo,@ModelAttribute("repairItemObj") RepairItem repairItemObj,@ModelAttribute("studentObj") Student studentObj,String addTime,@ModelAttribute("repairStateObj") RepairState repairStateObj,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (roomNo == null) roomNo = "";
		if (addTime == null) addTime = "";
		List<Repair> repairList = repairService.queryRepair(buildingObj, roomNo, repairItemObj, studentObj, addTime, repairStateObj, currentPage);
	    /*计算总的页数和总的记录数*/
	    repairService.queryTotalPageAndRecordNumber(buildingObj, roomNo, repairItemObj, studentObj, addTime, repairStateObj);
	    /*获取到总的页码数目*/
	    int totalPage = repairService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = repairService.getRecordNumber();
	    request.setAttribute("repairList",  repairList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("buildingObj", buildingObj);
	    request.setAttribute("roomNo", roomNo);
	    request.setAttribute("repairItemObj", repairItemObj);
	    request.setAttribute("studentObj", studentObj);
	    request.setAttribute("addTime", addTime);
	    request.setAttribute("repairStateObj", repairStateObj);
	    List<Building> buildingList = buildingService.queryAllBuilding();
	    request.setAttribute("buildingList", buildingList);
	    List<RepairItem> repairItemList = repairItemService.queryAllRepairItem();
	    request.setAttribute("repairItemList", repairItemList);
	    List<RepairState> repairStateList = repairStateService.queryAllRepairState();
	    request.setAttribute("repairStateList", repairStateList);
	    List<Student> studentList = studentService.queryAllStudent();
	    request.setAttribute("studentList", studentList);
		return "Repair/repair_frontquery_result"; 
	}
	
	
	/*前台按照查询条件分页查询报修信息*/
	@RequestMapping(value = { "/userFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String userFrontlist(@ModelAttribute("buildingObj") Building buildingObj,String roomNo,@ModelAttribute("repairItemObj") RepairItem repairItemObj,@ModelAttribute("studentObj") Student studentObj,String addTime,@ModelAttribute("repairStateObj") RepairState repairStateObj,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (roomNo == null) roomNo = "";
		if (addTime == null) addTime = "";
		studentObj = new Student();
		studentObj.setStudentNo(session.getAttribute("user_name").toString());
		
		List<Repair> repairList = repairService.queryRepair(buildingObj, roomNo, repairItemObj, studentObj, addTime, repairStateObj, currentPage);
	    /*计算总的页数和总的记录数*/
	    repairService.queryTotalPageAndRecordNumber(buildingObj, roomNo, repairItemObj, studentObj, addTime, repairStateObj);
	    /*获取到总的页码数目*/
	    int totalPage = repairService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = repairService.getRecordNumber();
	    request.setAttribute("repairList",  repairList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("buildingObj", buildingObj);
	    request.setAttribute("roomNo", roomNo);
	    request.setAttribute("repairItemObj", repairItemObj);
	    request.setAttribute("studentObj", studentObj);
	    request.setAttribute("addTime", addTime);
	    request.setAttribute("repairStateObj", repairStateObj);
	    List<Building> buildingList = buildingService.queryAllBuilding();
	    request.setAttribute("buildingList", buildingList);
	    List<RepairItem> repairItemList = repairItemService.queryAllRepairItem();
	    request.setAttribute("repairItemList", repairItemList);
	    List<RepairState> repairStateList = repairStateService.queryAllRepairState();
	    request.setAttribute("repairStateList", repairStateList);
	    List<Student> studentList = studentService.queryAllStudent();
	    request.setAttribute("studentList", studentList);
		return "Repair/repair_userFrontquery_result"; 
	}
	

     /*前台查询Repair信息*/
	@RequestMapping(value="/{repairId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer repairId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键repairId获取Repair对象*/
        Repair repair = repairService.getRepair(repairId);

        List<Building> buildingList = buildingService.queryAllBuilding();
        request.setAttribute("buildingList", buildingList);
        List<RepairItem> repairItemList = repairItemService.queryAllRepairItem();
        request.setAttribute("repairItemList", repairItemList);
        List<RepairState> repairStateList = repairStateService.queryAllRepairState();
        request.setAttribute("repairStateList", repairStateList);
        List<Student> studentList = studentService.queryAllStudent();
        request.setAttribute("studentList", studentList);
        request.setAttribute("repair",  repair);
        return "Repair/repair_frontshow";
	}

	/*ajax方式显示报修修改jsp视图页*/
	@RequestMapping(value="/{repairId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer repairId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键repairId获取Repair对象*/
        Repair repair = repairService.getRepair(repairId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonRepair = repair.getJsonObject();
		out.println(jsonRepair.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新报修信息*/
	@RequestMapping(value = "/{repairId}/update", method = RequestMethod.POST)
	public void update(@Validated Repair repair, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			repairService.updateRepair(repair);
			message = "报修更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "报修更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除报修信息*/
	@RequestMapping(value="/{repairId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer repairId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  repairService.deleteRepair(repairId);
	            request.setAttribute("message", "报修删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "报修删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条报修记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String repairIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = repairService.deleteRepairs(repairIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出报修信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("buildingObj") Building buildingObj,String roomNo,@ModelAttribute("repairItemObj") RepairItem repairItemObj,@ModelAttribute("studentObj") Student studentObj,String addTime,@ModelAttribute("repairStateObj") RepairState repairStateObj, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(roomNo == null) roomNo = "";
        if(addTime == null) addTime = "";
        List<Repair> repairList = repairService.queryRepair(buildingObj,roomNo,repairItemObj,studentObj,addTime,repairStateObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Repair信息记录"; 
        String[] headers = { "报修id","宿舍楼","宿舍号","报修项目","上报学生","上报时间","维修状态"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<repairList.size();i++) {
        	Repair repair = repairList.get(i); 
        	dataset.add(new String[]{repair.getRepairId() + "",repair.getBuildingObj().getBuildingName(),repair.getRoomNo(),repair.getRepairItemObj().getItemName(),repair.getStudentObj().getName(),repair.getAddTime(),repair.getRepairStateObj().getStateName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Repair.xls");//filename是下载的xls的名，建议最好用英文 
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
