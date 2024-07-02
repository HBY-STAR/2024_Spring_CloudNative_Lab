/*
 Navicat Premium Data Transfer

 Source Server         : 本地mysql
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:13306
 Source Schema         : ase_conference_service_prod

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 29/05/2024 22:30:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `ase_conference_service_prod` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `ase_conference_service_prod`;

-- ----------------------------
-- Table structure for conference
-- ----------------------------
DROP TABLE IF EXISTS `conference`;
CREATE TABLE `conference` (
  `conference_name` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `conference_abbr` varchar(255) DEFAULT NULL,
  `conference_end_at` datetime(6) DEFAULT NULL,
  `conference_start_at` datetime(6) DEFAULT NULL,
  `conference_status` varchar(255) DEFAULT NULL,
  `review_result_announce_at` datetime(6) DEFAULT NULL,
  `submission_deadline` datetime(6) DEFAULT NULL,
  `topics` varbinary(4096) DEFAULT NULL,
  `venue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`conference_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for conference_application
-- ----------------------------
DROP TABLE IF EXISTS `conference_application`;
CREATE TABLE `conference_application` (
  `applicationid` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `applicant_username` varchar(255) DEFAULT NULL,
  `application_status` varchar(255) DEFAULT NULL,
  `conference_abbr` varchar(255) DEFAULT NULL,
  `conference end_at` datetime(6) DEFAULT NULL,
  `conference_name` varchar(255) DEFAULT NULL,
  `conference start_at` datetime(6) DEFAULT NULL,
  `topics` varbinary(4096) DEFAULT NULL,
  `venue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`applicationid`)
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
