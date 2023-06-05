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
import com.chengxusheji.service.RepairItemService;
import com.chengxusheji.po.RepairItem;

//RepairItem管理控制层
@Controller
@RequestMapping("/RepairItem")
public class RepairItemController extends BaseController {

    /*业务层对象*/
    @Resource RepairItemService repairItemService;

	@InitBinder("repairItem")
	public void initBinderRepairItem(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("repairItem.");
	}
	/*跳转到添加RepairItem视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new RepairItem());
		return "RepairItem_add";
	}

	/*客户端ajax方式提交添加报修项目信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated RepairItem repairItem, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        repairItemService.addRepairItem(repairItem);
        message = "报修项目添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询报修项目信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if(rows != 0)repairItemService.setRows(rows);
		List<RepairItem> repairItemList = repairItemService.queryRepairItem(page);
	    /*计算总的页数和总的记录数*/
	    repairItemService.queryTotalPageAndRecordNumber();
	    /*获取到总的页码数目*/
	    int totalPage = repairItemService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = repairItemService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(RepairItem repairItem:repairItemList) {
			JSONObject jsonRepairItem = repairItem.getJsonObject();
			jsonArray.put(jsonRepairItem);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询报修项目信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<RepairItem> repairItemList = repairItemService.queryAllRepairItem();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(RepairItem repairItem:repairItemList) {
			JSONObject jsonRepairItem = new JSONObject();
			jsonRepairItem.accumulate("itemId", repairItem.getItemId());
			jsonRepairItem.accumulate("itemName", repairItem.getItemName());
			jsonArray.put(jsonRepairItem);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询报修项目信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		List<RepairItem> repairItemList = repairItemService.queryRepairItem(currentPage);
	    /*计算总的页数和总的记录数*/
	    repairItemService.queryTotalPageAndRecordNumber();
	    /*获取到总的页码数目*/
	    int totalPage = repairItemService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = repairItemService.getRecordNumber();
	    request.setAttribute("repairItemList",  repairItemList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
		return "RepairItem/repairItem_frontquery_result"; 
	}

     /*前台查询RepairItem信息*/
	@RequestMapping(value="/{itemId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer itemId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键itemId获取RepairItem对象*/
        RepairItem repairItem = repairItemService.getRepairItem(itemId);

        request.setAttribute("repairItem",  repairItem);
        return "RepairItem/repairItem_frontshow";
	}

	/*ajax方式显示报修项目修改jsp视图页*/
	@RequestMapping(value="/{itemId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer itemId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键itemId获取RepairItem对象*/
        RepairItem repairItem = repairItemService.getRepairItem(itemId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonRepairItem = repairItem.getJsonObject();
		out.println(jsonRepairItem.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新报修项目信息*/
	@RequestMapping(value = "/{itemId}/update", method = RequestMethod.POST)
	public void update(@Validated RepairItem repairItem, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			repairItemService.updateRepairItem(repairItem);
			message = "报修项目更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "报修项目更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除报修项目信息*/
	@RequestMapping(value="/{itemId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer itemId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  repairItemService.deleteRepairItem(itemId);
	            request.setAttribute("message", "报修项目删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "报修项目删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条报修项目记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String itemIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = repairItemService.deleteRepairItems(itemIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出报修项目信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel( Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        List<RepairItem> repairItemList = repairItemService.queryRepairItem();
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "RepairItem信息记录"; 
        String[] headers = { "项目id","项目名称"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<repairItemList.size();i++) {
        	RepairItem repairItem = repairItemList.get(i); 
        	dataset.add(new String[]{repairItem.getItemId() + "",repairItem.getItemName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"RepairItem.xls");//filename是下载的xls的名，建议最好用英文 
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
