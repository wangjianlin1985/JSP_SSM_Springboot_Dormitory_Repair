/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : room_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-06-28 22:07:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL default '',
  `password` varchar(32) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_building`
-- ----------------------------
DROP TABLE IF EXISTS `t_building`;
CREATE TABLE `t_building` (
  `buildingId` int(11) NOT NULL auto_increment COMMENT '宿舍楼id',
  `buildingName` varchar(20) NOT NULL COMMENT '宿舍楼名称',
  `buildingType` varchar(20) NOT NULL COMMENT '宿舍楼类型',
  `buildingDesc` varchar(2000) NOT NULL COMMENT '宿舍楼介绍',
  PRIMARY KEY  (`buildingId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_building
-- ----------------------------
INSERT INTO `t_building` VALUES ('1', '北苑18a', '男女混', '最新的大楼');

-- ----------------------------
-- Table structure for `t_live`
-- ----------------------------
DROP TABLE IF EXISTS `t_live`;
CREATE TABLE `t_live` (
  `liveId` int(11) NOT NULL auto_increment COMMENT '记录id',
  `buildingObj` int(11) NOT NULL COMMENT '入住宿舍楼',
  `roomNo` varchar(20) NOT NULL COMMENT '入住宿舍号',
  `studentObj` varchar(30) NOT NULL COMMENT '入住学生',
  `inDate` varchar(20) default NULL COMMENT '入住日期',
  `liveMemo` varchar(500) default NULL COMMENT '备注信息',
  PRIMARY KEY  (`liveId`),
  KEY `buildingObj` (`buildingObj`),
  KEY `studentObj` (`studentObj`),
  CONSTRAINT `t_live_ibfk_1` FOREIGN KEY (`buildingObj`) REFERENCES `t_building` (`buildingId`),
  CONSTRAINT `t_live_ibfk_2` FOREIGN KEY (`studentObj`) REFERENCES `t_student` (`studentNo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_live
-- ----------------------------
INSERT INTO `t_live` VALUES ('1', '1', '224', 'STU001', '2018-01-03', 'test');
INSERT INTO `t_live` VALUES ('2', '1', '223', 'STU002', '2018-06-06', '入住了大学宿舍');

-- ----------------------------
-- Table structure for `t_notice`
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
  `noticeId` int(11) NOT NULL auto_increment COMMENT '公告id',
  `title` varchar(80) NOT NULL COMMENT '标题',
  `content` varchar(5000) NOT NULL COMMENT '公告内容',
  `publishDate` varchar(20) default NULL COMMENT '发布时间',
  PRIMARY KEY  (`noticeId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_notice
-- ----------------------------
INSERT INTO `t_notice` VALUES ('1', '学生宿舍报修系统成立了', '<p>同学们，大家好，以后你们的宿舍除了什么问题，可以在本网站系统提交处理哦！</p>', '2018-01-25 17:50:20');

-- ----------------------------
-- Table structure for `t_repair`
-- ----------------------------
DROP TABLE IF EXISTS `t_repair`;
CREATE TABLE `t_repair` (
  `repairId` int(11) NOT NULL auto_increment COMMENT '报修id',
  `buildingObj` int(11) NOT NULL COMMENT '宿舍楼',
  `roomNo` varchar(20) NOT NULL COMMENT '宿舍号',
  `repairItemObj` int(11) NOT NULL COMMENT '报修项目',
  `repairDesc` varchar(500) NOT NULL COMMENT '问题描述',
  `studentObj` varchar(30) NOT NULL COMMENT '上报学生',
  `addTime` varchar(20) default NULL COMMENT '上报时间',
  `repairStateObj` int(11) NOT NULL COMMENT '维修状态',
  `handleResult` varchar(500) default NULL COMMENT '处理结果',
  PRIMARY KEY  (`repairId`),
  KEY `buildingObj` (`buildingObj`),
  KEY `repairItemObj` (`repairItemObj`),
  KEY `studentObj` (`studentObj`),
  KEY `repairStateObj` (`repairStateObj`),
  CONSTRAINT `t_repair_ibfk_1` FOREIGN KEY (`buildingObj`) REFERENCES `t_building` (`buildingId`),
  CONSTRAINT `t_repair_ibfk_2` FOREIGN KEY (`repairItemObj`) REFERENCES `t_repairitem` (`itemId`),
  CONSTRAINT `t_repair_ibfk_3` FOREIGN KEY (`studentObj`) REFERENCES `t_student` (`studentNo`),
  CONSTRAINT `t_repair_ibfk_4` FOREIGN KEY (`repairStateObj`) REFERENCES `t_repairstate` (`stateId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_repair
-- ----------------------------
INSERT INTO `t_repair` VALUES ('1', '1', '224', '1', '水管的水很小，麻烦派人来看下！', 'STU001', '2018-01-09 00:01:12', '1', '--');
INSERT INTO `t_repair` VALUES ('2', '1', '224', '1', '寝室灯没原来亮，想换个！', 'STU001', '2018-01-25 18:53:59', '2', '正在派工人路途中。。');

-- ----------------------------
-- Table structure for `t_repairitem`
-- ----------------------------
DROP TABLE IF EXISTS `t_repairitem`;
CREATE TABLE `t_repairitem` (
  `itemId` int(11) NOT NULL auto_increment COMMENT '项目id',
  `itemName` varchar(20) NOT NULL COMMENT '项目名称',
  PRIMARY KEY  (`itemId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_repairitem
-- ----------------------------
INSERT INTO `t_repairitem` VALUES ('1', '水电问题');
INSERT INTO `t_repairitem` VALUES ('2', '门窗问题');

-- ----------------------------
-- Table structure for `t_repairstate`
-- ----------------------------
DROP TABLE IF EXISTS `t_repairstate`;
CREATE TABLE `t_repairstate` (
  `stateId` int(11) NOT NULL auto_increment COMMENT '状态id',
  `stateName` varchar(20) NOT NULL COMMENT '状态名称',
  PRIMARY KEY  (`stateId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_repairstate
-- ----------------------------
INSERT INTO `t_repairstate` VALUES ('1', '待维修');
INSERT INTO `t_repairstate` VALUES ('2', '维修中');
INSERT INTO `t_repairstate` VALUES ('3', '维修完毕');

-- ----------------------------
-- Table structure for `t_room`
-- ----------------------------
DROP TABLE IF EXISTS `t_room`;
CREATE TABLE `t_room` (
  `roomId` int(11) NOT NULL auto_increment COMMENT '记录id',
  `buildingObj` int(11) NOT NULL COMMENT '所在宿舍楼',
  `roomNo` varchar(20) NOT NULL COMMENT '宿舍号',
  `roomPhoto` varchar(60) NOT NULL COMMENT '宿舍照片',
  `personNum` int(11) NOT NULL COMMENT '床位数',
  `roomDesc` varchar(5000) NOT NULL COMMENT '房间详情',
  PRIMARY KEY  (`roomId`),
  KEY `buildingObj` (`buildingObj`),
  CONSTRAINT `t_room_ibfk_1` FOREIGN KEY (`buildingObj`) REFERENCES `t_building` (`buildingId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_room
-- ----------------------------
INSERT INTO `t_room` VALUES ('1', '1', '224', 'upload/a13202bc-0423-4e09-ac04-32d190e072cc.jpg', '8', '<p>精装的寝室</p>');

-- ----------------------------
-- Table structure for `t_student`
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student` (
  `studentNo` varchar(30) NOT NULL COMMENT 'studentNo',
  `password` varchar(30) NOT NULL COMMENT '登录密码',
  `speicalName` varchar(20) NOT NULL COMMENT '所在专业',
  `gradeInfo` varchar(20) NOT NULL COMMENT '年级',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `gender` varchar(4) NOT NULL COMMENT '性别',
  `birthDate` varchar(20) default NULL COMMENT '出生日期',
  `studentPhoto` varchar(60) NOT NULL COMMENT '学生照片',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `address` varchar(80) default NULL COMMENT '家庭地址',
  `regTime` varchar(20) default NULL COMMENT '注册时间',
  PRIMARY KEY  (`studentNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_student
-- ----------------------------
INSERT INTO `t_student` VALUES ('STU001', '123', '计算机专业', '2017', '双鱼林', '男', '2018-01-03', 'upload/10623443-829e-48da-9463-db6ee65b6233.jpg', '13598342934', 'dashen@163.com', '四川成都红星路', '2018-01-15 23:59:35');
INSERT INTO `t_student` VALUES ('STU002', '123', '电子信息技术', '2017', '王萌萌', '女', '2018-01-02', 'upload/66b932f1-0bf7-4704-83ef-e493f57a21f7.jpg', '13958342342', 'mengmeng@163.com', '福建南平市建安街13号', '2018-01-25 22:12:15');
