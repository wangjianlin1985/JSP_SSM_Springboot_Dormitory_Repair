# JSP_SSM_Springboot_Dormitory_Repair
JSP_SSM_HTML5学生宿舍报修管理网可升级SpringBoot毕业源码案例设计
## 程序开发环境：myEclipse/Eclipse/Idea都可以 + mysql数据库
## 前台技术框架： Bootstrap  后台架构框架: SSM
## 管理员登录：
管理员个人账号模块：管理员能查看以及修改个人信息、密码。还有个退出系统功能。
### 宿舍楼管理模块：（输入宿舍楼号查看/）
查看：可以查看宿舍楼基本信息：几号宿舍楼、南苑\北苑，男\女\男女混。管理员可以修改宿舍楼的属性。
增加/删除：管理员可以增加/删除指定宿舍楼的宿舍号(若该宿舍有学生居住，那居住在该宿舍的学生的入住信息全改为“无”)。
（北苑的宿舍楼有18-24，每栋分a，b,像22a，22b这样。南苑宿舍有1-6，不分a，b。放几个例子进去吧。）
### 宿舍管理模块：
查看：能通过输入宿舍楼+宿舍号检索到该宿舍的宿舍信息（几人宿舍，宿舍报修情况）和人员名单以及具体信息（名单，个人学号，专业，年级。
退宿：输入学号，对某同学进行退宿（此人的个人信息的入住信息为“无”）
### 学生信息模块：
查看：通过输入检索学号查询学生个人信息，可以修改学生信息（个人信息和入住的宿舍，还有一个备注）。
### 后勤服务模块：
报修录入：可以录入每个宿舍的报修情况。（宿舍楼、月份、宿舍号、备注：此处填具体报修情况，金额，已修/未修）
报修查询：可以通过宿舍楼/月份/已修/未修查询报修记录，能删除或修改记录。
公告管理：查看/修改公告
## 学生登录：
可以查看公告，查看和修改个人信息，查看宿舍信息（显示出来的和宿管显示的一样）但不能修改，可以提交报修信息但不能查看。（提交成功显示已提交）
## 实体ER属性：
学生: 学号,登录密码,所在专业,年级,姓名,性别,出生日期,学生照片,联系电话,邮箱,家庭地址,注册时间

宿舍楼: 宿舍楼id,宿舍楼名称,宿舍楼类型,宿舍楼介绍

宿舍: 记录id,所在宿舍楼,宿舍号,宿舍照片,床位数,房间详情

住宿: 记录id,入住宿舍楼,入住宿舍号,入住学生,入住日期,备注信息

报修: 报修id,宿舍楼,宿舍号,报修项目,问题描述,上报学生,上报时间,维修状态,处理结果

报修项目: 项目id,项目名称

报修状态: 状态id,状态名称

新闻公告: 公告id,标题,公告内容,发布时间


