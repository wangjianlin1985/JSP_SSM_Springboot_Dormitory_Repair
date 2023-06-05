<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/repair.css" /> 

<div id="repair_manage"></div>
<div id="repair_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="repair_manage_tool.edit();">处理报修</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="repair_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="repair_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="repair_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="repair_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="repairQueryForm" method="post">
			宿舍楼：<input class="textbox" type="text" id="buildingObj_buildingId_query" name="buildingObj.buildingId" style="width: auto"/>
			宿舍号：<input type="text" class="textbox" id="roomNo" name="roomNo" style="width:110px" />
			报修项目：<input class="textbox" type="text" id="repairItemObj_itemId_query" name="repairItemObj.itemId" style="width: auto"/>
			上报学生：<input class="textbox" type="text" id="studentObj_studentNo_query" name="studentObj.studentNo" style="width: auto"/>
			上报时间：<input type="text" id="addTime" name="addTime" class="easyui-datebox" editable="false" style="width:100px">
			维修状态：<input class="textbox" type="text" id="repairStateObj_stateId_query" name="repairStateObj.stateId" style="width: auto"/>
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="repair_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="repairEditDiv">
	<form id="repairEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">报修id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="repair_repairId_edit" name="repair.repairId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">宿舍楼:</span>
			<span class="inputControl">
				<input class="textbox"  id="repair_buildingObj_buildingId_edit" name="repair.buildingObj.buildingId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">宿舍号:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="repair_roomNo_edit" name="repair.roomNo" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">报修项目:</span>
			<span class="inputControl">
				<input class="textbox"  id="repair_repairItemObj_itemId_edit" name="repair.repairItemObj.itemId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">问题描述:</span>
			<span class="inputControl">
				<textarea id="repair_repairDesc_edit" name="repair.repairDesc" rows="8" cols="60"></textarea>

			</span>

		</div>
		<div>
			<span class="label">上报学生:</span>
			<span class="inputControl">
				<input class="textbox"  id="repair_studentObj_studentNo_edit" name="repair.studentObj.studentNo" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">上报时间:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="repair_addTime_edit" name="repair.addTime" />

			</span>

		</div>
		<div>
			<span class="label">维修状态:</span>
			<span class="inputControl">
				<input class="textbox"  id="repair_repairStateObj_stateId_edit" name="repair.repairStateObj.stateId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">处理结果:</span>
			<span class="inputControl">
				<textarea id="repair_handleResult_edit" name="repair.handleResult" rows="8" cols="60"></textarea>

			</span>

		</div>
	</form>
</div>
<script type="text/javascript" src="Repair/js/repair_manage.js"></script> 
