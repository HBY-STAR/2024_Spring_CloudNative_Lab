/*
 Navicat Premium Data Transfer

 Source Server         : 本地mysql
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:13306
 Source Schema         : ase_review_service_prod

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 29/05/2024 22:40:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `ase_review_service_prod` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `ase_review_service_prod`;

-- ----------------------------
-- Table structure for review_rebuttal
-- ----------------------------
DROP TABLE IF EXISTS `review_rebuttal`;
CREATE TABLE `review_rebuttal` (
  `contribution_id` bigint NOT NULL,
  `seq` int NOT NULL,
  `conference_name` varchar(255) DEFAULT NULL,
  `rebuttal_message` varchar(255) DEFAULT NULL,
  `rebuttal_score` int DEFAULT NULL,
  `review_comment` varchar(255) DEFAULT NULL,
  `review_confidence` int DEFAULT NULL,
  `review_score` int DEFAULT NULL,
  `reviewer_username` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`contribution_id`,`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

SET FOREIGN_KEY_CHECKS = 1;
