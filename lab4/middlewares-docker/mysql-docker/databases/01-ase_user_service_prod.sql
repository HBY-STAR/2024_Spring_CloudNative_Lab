/*
 Navicat Premium Data Transfer

 Source Server         : 本地mysql
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:13306
 Source Schema         : ase_user_service_prod

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 29/05/2024 22:31:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `ase_user_service_prod` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `ase_user_service_prod`;

-- ----------------------------
-- Table structure for responsible_topic
-- ----------------------------
DROP TABLE IF EXISTS `responsible_topic`;
CREATE TABLE `responsible_topic` (
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`name`)
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `username` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `area` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `institution_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user_conference_role
-- ----------------------------
DROP TABLE IF EXISTS `user_conference_role`;
CREATE TABLE `user_conference_role` (
  `conference_name` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`conference_name`,`role`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user_conference_role_responsible_topic
-- ----------------------------
DROP TABLE IF EXISTS `user_conference_role_responsible_topic`;
CREATE TABLE `user_conference_role_responsible_topic` (
  `user_conference_roles_conference_name` varchar(255) NOT NULL,
  `user_conference_roles_role` varchar(255) NOT NULL,
  `user_conference_roles_username` varchar(255) NOT NULL,
  `responsible_topics_name` varchar(255) NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_conference_role_responsible_topic_pk` (`responsible_topics_name`,`user_conference_roles_conference_name`,`user_conference_roles_username`),
  KEY `FK8vwr1feihk325q1l36t5tp4c0` (`user_conference_roles_conference_name`,`user_conference_roles_role`,`user_conference_roles_username`),
  CONSTRAINT `FK63auda58husx51aivuy1w51o` FOREIGN KEY (`responsible_topics_name`) REFERENCES `responsible_topic` (`name`),
  CONSTRAINT `FK8vwr1feihk325q1l36t5tp4c0` FOREIGN KEY (`user_conference_roles_conference_name`, `user_conference_roles_role`, `user_conference_roles_username`) REFERENCES `user_conference_role` (`conference_name`, `role`, `username`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
