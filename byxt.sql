/*
MySQL Data Transfer
Source Host: localhost
Source Database: byxt
Target Host: localhost
Target Database: byxt
Date: 2026/4/22 14:58:29
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for root
-- ----------------------------
DROP TABLE IF EXISTS `root`;
CREATE TABLE `root` (
  `rootid` varchar(30) NOT NULL,
  `rootpwd` varchar(60) default NULL,
  PRIMARY KEY  (`rootid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL auto_increment,
  `xh` varchar(20) default NULL,
  `xm` varchar(20) default NULL,
  `pwd` varchar(50) default NULL,
  `zy` varchar(20) default NULL,
  `bj` varchar(20) default NULL,
  `email` varchar(20) default NULL,
  `phone` varchar(20) default NULL,
  `qq` varchar(20) default NULL,
  `adminid` varchar(40) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` int(11) NOT NULL auto_increment,
  `gh` varchar(20) default NULL,
  `xm` varchar(20) default NULL,
  `pwd` varchar(50) default NULL,
  `zhicheng` varchar(10) default NULL,
  `email` varchar(30) default NULL,
  `phone` varchar(20) default NULL,
  `qq` varchar(20) default NULL,
  `bgdd` varchar(30) default NULL,
  `shangxian` int(11) default '0',
  `adminid` varchar(40) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tm
-- ----------------------------
DROP TABLE IF EXISTS `tm`;
CREATE TABLE `tm` (
  `id` int(11) NOT NULL auto_increment,
  `gh` varchar(20) default NULL,
  `txm` varchar(20) default NULL,
  `tm` varchar(30) default NULL,
  `bz` text,
  `xh` varchar(20) default NULL,
  `sxm` varchar(20) default NULL,
  `zy` varchar(20) default NULL,
  `bj` varchar(20) default NULL,
  `adminid` varchar(40) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userid` varchar(30) default NULL,
  `userpwd` varchar(50) default NULL,
  `dp` varchar(50) default NULL,
  `lastlogin` varchar(50) default NULL,
  `xuhao` int(11) default '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for zt
-- ----------------------------
DROP TABLE IF EXISTS `zt`;
CREATE TABLE `zt` (
  `zt` tinyint(4) default NULL,
  `adminid` varchar(40) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `root` VALUES ('root', '63a9f0ea7bb98050796b649e85481845');
