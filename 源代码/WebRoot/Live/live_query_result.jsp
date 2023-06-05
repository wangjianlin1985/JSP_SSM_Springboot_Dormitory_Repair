<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/live.css" /> 

<div id="live_manage"></div>
<div id="live_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="live_manage_tool.edit();">修改</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="live_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="live_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="live_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="live_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="liveQueryForm" method="post">
			入住宿舍楼：<input class="textbox" type="text" id="buildingObj_buildingId_query" name="buildingObj.buildingId" style="width: auto"/>
			入住宿舍号：<input type="text" class="textbox" id="roomNo" name="roomNo" style="width:110px" />
			入住学生：<input class="textbox" type="text" id="studentObj_studentNo_query" name="studentObj.studentNo" style="width: auto"/>
			入住日期：<input type="text" id="inDate" name="inDate" class="easyui-datebox" editable="false" style="width:100px">
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="live_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="liveEditDiv">
	<form id="liveEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">记录id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="live_liveId_edit" name="live.liveId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">入住宿舍楼:</span>
			<span class="inputControl">
				<input class="textbox"  id="live_buildingObj_buildingId_edit" name="live.buildingObj.buildingId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">入住宿舍号:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="live_roomNo_edit" name="live.roomNo" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">入住学生:</span>
			<span class="inputControl">
				<input class="textbox"  id="live_studentObj_studentNo_edit" name="live.studentObj.studentNo" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">入住日期:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="live_inDate_edit" name="live.inDate" />

			</span>

		</div>
		<div>
			<span class="label">备注信息:</span>
			<span class="inputControl">
				<textarea id="live_liveMemo_edit" name="live.liveMemo" rows="8" cols="60"></textarea>

			</span>

		</div>
	</form>
</div>
<script type="text/javascript" src="Live/js/live_manage.js"></script> 
