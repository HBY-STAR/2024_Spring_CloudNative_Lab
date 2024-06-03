/*
 Navicat Premium Data Transfer

 Source Server         : 本地mysql
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:13306
 Source Schema         : ase_contribute_service_prod

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 29/05/2024 22:39:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `ase_contribute_service_prod` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `ase_contribute_service_prod`;

-- ----------------------------
-- Table structure for contribution
-- ----------------------------
DROP TABLE IF EXISTS `contribution`;
CREATE TABLE `contribution` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `abstract_content` varchar(4096) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `conference_name` varchar(255) DEFAULT NULL,
  `contribute_time` varchar(255) DEFAULT NULL,
  `essay_id` varchar(255) DEFAULT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `topics` varchar(4096) DEFAULT NULL,
  `update_time` varchar(255) DEFAULT NULL,
  `writers` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

SET FOREIGN_KEY_CHECKS = 1;
