/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50618
Source Host           : localhost:3306
Source Database       : smart-dao-test

Target Server Type    : MYSQL
Target Server Version : 50618
File Encoding         : 65001

Date: 2018-11-26 21:53:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `book`
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `price` float DEFAULT NULL,
  `author_id` bigint(20) NOT NULL,
  `isbn` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `words` int(11) DEFAULT NULL,
  `print_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
