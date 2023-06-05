<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Room" %>
<%@ page import="com.chengxusheji.po.Building" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Room> roomList = (List<Room>)request.getAttribute("roomList");
    //获取所有的buildingObj信息
    List<Building> buildingList = (List<Building>)request.getAttribute("buildingList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    Building buildingObj = (Building)request.getAttribute("buildingObj");
    String roomNo = (String)request.getAttribute("roomNo"); //宿舍号查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>宿舍查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="col-md-9 wow fadeInLeft">
		<ul class="breadcrumb">
  			<li><a href="<%=basePath %>index.jsp">首页</a></li>
  			<li><a href="<%=basePath %>Room/frontlist">宿舍信息列表</a></li>
  			<li class="active">查询结果显示</li>
  			<a class="pull-right" href="<%=basePath %>Room/room_frontAdd.jsp" style="display:none;">添加宿舍</a>
		</ul>
		<div class="row">
			<%
				/*计算起始序号*/
				int startIndex = (currentPage -1) * 5;
				/*遍历记录*/
				for(int i=0;i<roomList.size();i++) {
            		int currentIndex = startIndex + i + 1; //当前记录的序号
            		Room room = roomList.get(i); //获取到宿舍对象
            		String clearLeft = "";
            		if(i%4 == 0) clearLeft = "style=\"clear:left;\"";
			%>
			<div class="col-md-3 bottom15" <%=clearLeft %>>
			  <a  href="<%=basePath  %>Room/<%=room.getRoomId() %>/frontshow"><img class="img-responsive" src="<%=basePath%><%=room.getRoomPhoto()%>" /></a>
			     <div class="showFields">
			     	<div class="field">
	            		所在宿舍楼:<%=room.getBuildingObj().getBuildingName() %>
			     	</div>
			     	<div class="field">
	            		宿舍号:<%=room.getRoomNo() %>
			     	</div>
			     	<div class="field">
	            		床位数:<%=room.getPersonNum() %>
			     	</div>
			        <a class="btn btn-primary top5" href="<%=basePath %>Room/<%=room.getRoomId() %>/frontshow">详情</a>
			        <a class="btn btn-primary top5" onclick="roomEdit('<%=room.getRoomId() %>');" style="display:none;">修改</a>
			        <a class="btn btn-primary top5" onclick="roomDelete('<%=room.getRoomId() %>');" style="display:none;">删除</a>
			     </div>
			</div>
			<%  } %>

			<div class="row">
				<div class="col-md-12">
					<nav class="pull-left">
						<ul class="pagination">
							<li><a href="#" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
							<%
								int startPage = currentPage - 5;
								int endPage = currentPage + 5;
								if(startPage < 1) startPage=1;
								if(endPage > totalPage) endPage = totalPage;
								for(int i=startPage;i<=endPage;i++) {
							%>
							<li class="<%= currentPage==i?"active":"" %>"><a href="#"  onclick="GoToPage(<%=i %>,<%=totalPage %>);"><%=i %></a></li>
							<%  } %> 
							<li><a href="#" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);"><span aria-hidden="true">&raquo;</span></a></li>
						</ul>
					</nav>
					<div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>宿舍查询</h1>
		</div>
		<form name="roomQueryForm" id="roomQueryForm" action="<%=basePath %>Room/frontlist" class="mar_t15">
            <div class="form-group">
            	<label for="buildingObj_buildingId">所在宿舍楼：</label>
                <select id="buildingObj_buildingId" name="buildingObj.buildingId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(Building buildingTemp:buildingList) {
	 					String selected = "";
 					if(buildingObj!=null && buildingObj.getBuildingId()!=null && buildingObj.getBuildingId().intValue()==buildingTemp.getBuildingId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=buildingTemp.getBuildingId() %>" <%=selected %>><%=buildingTemp.getBuildingName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="roomNo">宿舍号:</label>
				<input type="text" id="roomNo" name="roomNo" value="<%=roomNo %>" class="form-control" placeholder="请输入宿舍号">
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
</div>
<div id="roomEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" style="width:900px;" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;宿舍信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="roomEditForm" id="roomEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="room_roomId_edit" class="col-md-3 text-right">记录id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="room_roomId_edit" name="room.roomId" class="form-control" placeholder="请输入记录id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="room_buildingObj_buildingId_edit" class="col-md-3 text-right">所在宿舍楼:</label>
		  	 <div class="col-md-9">
			    <select id="room_buildingObj_buildingId_edit" name="room.buildingObj.buildingId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="room_roomNo_edit" class="col-md-3 text-right">宿舍号:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="room_roomNo_edit" name="room.roomNo" class="form-control" placeholder="请输入宿舍号">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="room_roomPhoto_edit" class="col-md-3 text-right">宿舍照片:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="room_roomPhotoImg" border="0px"/><br/>
			    <input type="hidden" id="room_roomPhoto" name="room.roomPhoto"/>
			    <input id="roomPhotoFile" name="roomPhotoFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="room_personNum_edit" class="col-md-3 text-right">床位数:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="room_personNum_edit" name="room.personNum" class="form-control" placeholder="请输入床位数">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="room_roomDesc_edit" class="col-md-3 text-right">房间详情:</label>
		  	 <div class="col-md-9">
			 	<textarea name="room.roomDesc" id="room_roomDesc_edit" style="width:100%;height:500px;"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#roomEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxRoomModify();">提交</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/lang/zh-cn/zh-cn.js"></script>
<script>
//实例化编辑器
var room_roomDesc_edit = UE.getEditor('room_roomDesc_edit'); //房间详情编辑器
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.roomQueryForm.currentPage.value = currentPage;
    document.roomQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.roomQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.roomQueryForm.currentPage.value = pageValue;
    documentroomQueryForm.submit();
}

/*弹出修改宿舍界面并初始化数据*/
function roomEdit(roomId) {
	$.ajax({
		url :  basePath + "Room/" + roomId + "/update",
		type : "get",
		dataType: "json",
		success : function (room, response, status) {
			if (room) {
				$("#room_roomId_edit").val(room.roomId);
				$.ajax({
					url: basePath + "Building/listAll",
					type: "get",
					success: function(buildings,response,status) { 
						$("#room_buildingObj_buildingId_edit").empty();
						var html="";
		        		$(buildings).each(function(i,building){
		        			html += "<option value='" + building.buildingId + "'>" + building.buildingName + "</option>";
		        		});
		        		$("#room_buildingObj_buildingId_edit").html(html);
		        		$("#room_buildingObj_buildingId_edit").val(room.buildingObjPri);
					}
				});
				$("#room_roomNo_edit").val(room.roomNo);
				$("#room_roomPhoto").val(room.roomPhoto);
				$("#room_roomPhotoImg").attr("src", basePath +　room.roomPhoto);
				$("#room_personNum_edit").val(room.personNum);
				room_roomDesc_edit.setContent(room.roomDesc, false);
				$('#roomEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除宿舍信息*/
function roomDelete(roomId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Room/deletes",
			data : {
				roomIds : roomId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#roomQueryForm").submit();
					//location.href= basePath + "Room/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交宿舍信息表单给服务器端修改*/
function ajaxRoomModify() {
	$.ajax({
		url :  basePath + "Room/" + $("#room_roomId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#roomEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#roomQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
	/*小屏幕导航点击关闭菜单*/
    $('.navbar-collapse a').click(function(){
        $('.navbar-collapse').collapse('hide');
    });
    new WOW().init();

})
</script>
</body>
</html>

