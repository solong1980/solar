-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.19-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 solar 的数据库结构
DROP DATABASE IF EXISTS `solar`;
CREATE DATABASE IF NOT EXISTS `solar` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `solar`;

-- 导出  表 solar.areas 结构
DROP TABLE IF EXISTS `areas`;
CREATE TABLE IF NOT EXISTS `areas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `areaid` varchar(20) NOT NULL,
  `area` varchar(50) NOT NULL,
  `cityid` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3145 DEFAULT CHARSET=utf8 COMMENT='行政区域县区信息表';

-- 数据导出被取消选择。
-- 导出  表 solar.cities 结构
DROP TABLE IF EXISTS `cities`;
CREATE TABLE IF NOT EXISTS `cities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cityid` varchar(20) NOT NULL,
  `city` varchar(50) NOT NULL,
  `provinceid` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=346 DEFAULT CHARSET=utf8 COMMENT='行政区域地州市信息表';

-- 数据导出被取消选择。
-- 导出  过程 solar.clean_test_data 结构
DROP PROCEDURE IF EXISTS `clean_test_data`;
DELIMITER //
CREATE DEFINER=`longlh`@`localhost` PROCEDURE `clean_test_data`()
BEGIN
	delete from so_account where id>3;
	delete from so_customer where id>10000;
	delete from so_devices;
	delete from so_working_mode;
END//
DELIMITER ;

-- 导出  过程 solar.create_test_data 结构
DROP PROCEDURE IF EXISTS `create_test_data`;
DELIMITER //
CREATE DEFINER=`longlh`@`localhost` PROCEDURE `create_test_data`()
BEGIN
	
	DECLARE count int default 10000; 
   DECLARE cid long;
   set @i=1;

   while @i<=500 do
      -- 创建客户
   	INSERT INTO so_customer (cust_name, type, create_time) VALUES (concat('蓝本科技',@i), 2, now());
   	select last_insert_id() into cid;
   	INSERT INTO so_account (cust_id, account, password, role, create_time) VALUES (cid, concat('cust_',@i), 'e10adc3949ba59abbe56e057f20f883e', 30, now());
   	INSERT INTO so_working_mode ( cust_id, dev_id, continuous, timing, point_of_time, fixed_timing_length, time_interval, emergency, create_time, create_by) VALUES (cid, '0', b'0', b'1', '16:14:54', b'1', 30, b'1', now(), 10000);
   	set @d=0;
   	while @d<20 do
			INSERT INTO so_devices (dev_no, cust_id, gps_info, ip_addr, data_server_ip, data_server_port, create_time) VALUES (concat('000',@i,cid,@d), cid, '$GPRMC,204700,A,3403.868,N,11709.432,W,001.9,336.9,170698,013.6,E*6E', '123.56.76.77', '123.56.76.77', 10122, now());
   		set @d = @d+1;
   	 end while;
		set @i=@i+1;
	end while;
END//
DELIMITER ;

-- 导出  表 solar.provinces 结构
DROP TABLE IF EXISTS `provinces`;
CREATE TABLE IF NOT EXISTS `provinces` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provinceid` varchar(20) NOT NULL,
  `province` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COMMENT='省份信息表';

-- 数据导出被取消选择。
-- 导出  表 solar.so_account 结构
DROP TABLE IF EXISTS `so_account`;
CREATE TABLE IF NOT EXISTS `so_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` smallint(6) DEFAULT '100' COMMENT '10:待审核,50:审核通过, 60:审核不通过',
  `account` varchar(50) DEFAULT NULL,
  `name` varchar(60) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  `type` smallint(6) NOT NULL COMMENT '10.Admin,20.Operator,30.User,40.Unknown',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_account` (`account`),
  KEY `cust_id` (`name`),
  KEY `index_account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=531 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 solar.so_account_find 结构
DROP TABLE IF EXISTS `so_account_find`;
CREATE TABLE IF NOT EXISTS `so_account_find` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `old_phone` varchar(15) NOT NULL,
  `location_ids` varchar(1024) NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  `vcode` varchar(50) NOT NULL,
  `type` smallint(6) NOT NULL COMMENT '10.Admin,20.Operator,30.User,40.Unknown',
  `status` smallint(6) NOT NULL DEFAULT '10' COMMENT '10:待审核, 50.审核不通过,60.审核通过',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `cust_id` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=526 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 solar.so_account_location 结构
DROP TABLE IF EXISTS `so_account_location`;
CREATE TABLE IF NOT EXISTS `so_account_location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `location_id` varchar(50) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 solar.so_app_version 结构
DROP TABLE IF EXISTS `so_app_version`;
CREATE TABLE IF NOT EXISTS `so_app_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ver_no` int(11) NOT NULL,
  `type` smallint(6) NOT NULL COMMENT '客户端类型 10:apk, 50:dmg,100:mc',
  `info` varchar(256) DEFAULT NULL,
  `path` varchar(512) NOT NULL,
  `file_name` varchar(150) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ver_no` (`ver_no`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='更新信息表';

-- 数据导出被取消选择。
-- 导出  表 solar.so_devices 结构
DROP TABLE IF EXISTS `so_devices`;
CREATE TABLE IF NOT EXISTS `so_devices` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'PKEY',
  `dev_no` varchar(50) NOT NULL COMMENT '设备(终端)号',
  `project_id` bigint(20) NOT NULL,
  `location_id` varchar(20) NOT NULL,
  `sw0` tinyint(4) NOT NULL DEFAULT '2' COMMENT '分机1控制,2为自动控制,0和1为手动控制',
  `sw1` tinyint(4) NOT NULL DEFAULT '2' COMMENT '分机2控制,2为自动控制,0和1为手动控制',
  `sw2` tinyint(4) NOT NULL DEFAULT '2' COMMENT '水泵1控制,2为自动控制,0和1为手动控制',
  `sw3` tinyint(4) NOT NULL DEFAULT '2' COMMENT '水泵2控制,2为自动控制,0和1为手动控制',
  `sw4` tinyint(4) NOT NULL DEFAULT '2' COMMENT '继电器1控制,2为自动控制,0和1为手动控制',
  `sw5` tinyint(4) NOT NULL DEFAULT '2' COMMENT '继电器2控制,2为自动控制,0和1为手动控制',
  `sw6` tinyint(4) NOT NULL DEFAULT '2' COMMENT '继电器3控制,2为自动控制,0和1为手动控制',
  `sw7` tinyint(4) NOT NULL DEFAULT '2' COMMENT '继电器4控制,2为自动控制,0和1为手动控制',
  `gps_info` varchar(100) DEFAULT NULL,
  `ip_addr` varchar(100) DEFAULT NULL,
  `data_server_ip` varchar(100) DEFAULT NULL,
  `data_server_port` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_dev_no` (`dev_no`),
  KEY `index_dev_no` (`dev_no`),
  KEY `cust_id` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 solar.so_device_working_mode 结构
DROP TABLE IF EXISTS `so_device_working_mode`;
CREATE TABLE IF NOT EXISTS `so_device_working_mode` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_id` bigint(20) NOT NULL DEFAULT '0',
  `sw_0` bit(1) NOT NULL DEFAULT b'1',
  `sw_1` bit(1) NOT NULL DEFAULT b'1',
  `sw_2` bit(1) NOT NULL DEFAULT b'1',
  `sw_3` bit(1) NOT NULL DEFAULT b'1',
  `sw_4` bit(1) NOT NULL DEFAULT b'1',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='工作模式配置';

-- 数据导出被取消选择。
-- 导出  表 solar.so_dev_config 结构
DROP TABLE IF EXISTS `so_dev_config`;
CREATE TABLE IF NOT EXISTS `so_dev_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `dev_type` smallint(6) DEFAULT NULL COMMENT '设备种类(10.太阳能板 20.电池 30.曝气系统 40.太阳能控制器)',
  `config` varchar(50) DEFAULT '1' COMMENT '设备配置',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 solar.so_locations 结构
DROP TABLE IF EXISTS `so_locations`;
CREATE TABLE IF NOT EXISTS `so_locations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `province` varchar(20) NOT NULL,
  `city` varchar(20) NOT NULL,
  `county` varchar(20) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 solar.so_privilege 结构
DROP TABLE IF EXISTS `so_privilege`;
CREATE TABLE IF NOT EXISTS `so_privilege` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL COMMENT '运维人员帐号ID',
  `location_id` varchar(20) NOT NULL COMMENT '项目ID',
  `project_id` bigint(20) NOT NULL DEFAULT '20' COMMENT '项目ID',
  `role` smallint(6) NOT NULL DEFAULT '20' COMMENT '10.Admin,20.Operator,30.User,40.Unknown',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 COMMENT='运维人员权限表';

-- 数据导出被取消选择。
-- 导出  表 solar.so_project 结构
DROP TABLE IF EXISTS `so_project`;
CREATE TABLE IF NOT EXISTS `so_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'PKEY 10001开始',
  `project_name` varchar(150) NOT NULL COMMENT '项目名称',
  `type` smallint(6) NOT NULL COMMENT '项目类型(10.太阳能污水处理系统,20.智能运维系统)',
  `capability` smallint(6) NOT NULL COMMENT '设计处理量 (5,10,20,30,50,100)吨',
  `dev_configure` varchar(256) DEFAULT NULL COMMENT '包含设备种配置(10.风机 20.水泵 30.控制器 40.太阳能板 50.电池),逗号连接,多选',
  `emission_standards` smallint(6) NOT NULL COMMENT '排放标准 10.一级A,20.一级B',
  `location_id` bigint(20) NOT NULL,
  `street` varchar(512) DEFAULT NULL,
  `worker_name` varchar(50) DEFAULT NULL COMMENT '运维人员姓名',
  `worker_phone` varchar(50) DEFAULT NULL COMMENT '运维人员联系方式,手机号码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  `state` smallint(6) NOT NULL DEFAULT '0' COMMENT '0,正常,10,删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 solar.so_project_working_mode 结构
DROP TABLE IF EXISTS `so_project_working_mode`;
CREATE TABLE IF NOT EXISTS `so_project_working_mode` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) NOT NULL DEFAULT '0',
  `h_0` bit(1) NOT NULL DEFAULT b'1',
  `h_1` bit(1) NOT NULL DEFAULT b'1',
  `h_2` bit(1) NOT NULL DEFAULT b'1',
  `h_3` bit(1) NOT NULL DEFAULT b'1',
  `h_4` bit(1) NOT NULL DEFAULT b'1',
  `h_5` bit(1) NOT NULL DEFAULT b'1',
  `h_6` bit(1) NOT NULL DEFAULT b'1',
  `h_7` bit(1) NOT NULL DEFAULT b'1',
  `h_8` bit(1) NOT NULL DEFAULT b'1',
  `h_9` bit(1) NOT NULL DEFAULT b'1',
  `h_10` bit(1) NOT NULL DEFAULT b'1',
  `h_11` bit(1) NOT NULL DEFAULT b'1',
  `h_12` bit(1) NOT NULL DEFAULT b'1',
  `h_13` bit(1) NOT NULL DEFAULT b'1',
  `h_14` bit(1) NOT NULL DEFAULT b'1',
  `h_15` bit(1) NOT NULL DEFAULT b'1',
  `h_16` bit(1) NOT NULL DEFAULT b'1',
  `h_17` bit(1) NOT NULL DEFAULT b'1',
  `h_18` bit(1) NOT NULL DEFAULT b'1',
  `h_19` bit(1) NOT NULL DEFAULT b'1',
  `h_20` bit(1) NOT NULL DEFAULT b'1',
  `h_21` bit(1) NOT NULL DEFAULT b'1',
  `h_22` bit(1) NOT NULL DEFAULT b'1',
  `h_23` bit(1) NOT NULL DEFAULT b'1',
  `h_24` bit(1) NOT NULL DEFAULT b'1',
  `h_25` bit(1) NOT NULL DEFAULT b'1',
  `h_26` bit(1) NOT NULL DEFAULT b'1',
  `h_27` bit(1) NOT NULL DEFAULT b'1',
  `h_28` bit(1) NOT NULL DEFAULT b'1',
  `h_29` bit(1) NOT NULL DEFAULT b'1',
  `h_30` bit(1) NOT NULL DEFAULT b'1',
  `h_31` bit(1) NOT NULL DEFAULT b'1',
  `h_32` bit(1) NOT NULL DEFAULT b'1',
  `h_33` bit(1) NOT NULL DEFAULT b'1',
  `h_34` bit(1) NOT NULL DEFAULT b'1',
  `h_35` bit(1) NOT NULL DEFAULT b'1',
  `h_36` bit(1) NOT NULL DEFAULT b'1',
  `h_37` bit(1) NOT NULL DEFAULT b'1',
  `h_38` bit(1) NOT NULL DEFAULT b'1',
  `h_39` bit(1) NOT NULL DEFAULT b'1',
  `h_40` bit(1) NOT NULL DEFAULT b'1',
  `h_41` bit(1) NOT NULL DEFAULT b'1',
  `h_42` bit(1) NOT NULL DEFAULT b'1',
  `h_43` bit(1) NOT NULL DEFAULT b'1',
  `h_44` bit(1) NOT NULL DEFAULT b'1',
  `h_45` bit(1) NOT NULL DEFAULT b'1',
  `h_46` bit(1) NOT NULL DEFAULT b'1',
  `h_47` bit(1) NOT NULL DEFAULT b'1',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `project_id` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='工作模式配置';

-- 数据导出被取消选择。
-- 导出  表 solar.so_running_data 结构
DROP TABLE IF EXISTS `so_running_data`;
CREATE TABLE IF NOT EXISTS `so_running_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'KEY',
  `req` varchar(512) NOT NULL DEFAULT '' COMMENT '原始消息',
  `uuid` varchar(50) NOT NULL DEFAULT '0' COMMENT '设备号',
  `fmid` varchar(50) NOT NULL DEFAULT '0' COMMENT '固件版本',
  `vssun` varchar(50) NOT NULL DEFAULT '0' COMMENT '太阳能板电压',
  `ichg` varchar(40) NOT NULL DEFAULT '0' COMMENT '电池充电电流',
  `vbat` varchar(50) NOT NULL DEFAULT '0' COMMENT '电池电压',
  `level` varchar(50) NOT NULL DEFAULT '0' COMMENT '电池剩余容量',
  `pchg` varchar(50) NOT NULL DEFAULT '0' COMMENT '充电累积度数',
  `pdis` varchar(50) NOT NULL DEFAULT '0' COMMENT '放电累积度数',
  `ild1` varchar(50) NOT NULL DEFAULT '0' COMMENT '负载1电流',
  `ild2` varchar(50) NOT NULL DEFAULT '0' COMMENT '负载2电流',
  `ild3` varchar(50) NOT NULL DEFAULT '0' COMMENT '负载3电流',
  `ild4` varchar(50) NOT NULL DEFAULT '0' COMMENT '负载4电流',
  `temp` varchar(50) NOT NULL DEFAULT '0' COMMENT '环境温度',
  `ain1` varchar(50) NOT NULL DEFAULT '0' COMMENT '第1路4-20mA',
  `ain2` varchar(50) NOT NULL DEFAULT '0' COMMENT '第2路4-20mA',
  `ain3` varchar(50) NOT NULL DEFAULT '0' COMMENT '第3路4-20mA',
  `stat` varchar(50) NOT NULL DEFAULT '0' COMMENT '控制器状态',
  `utctime` varchar(50) NOT NULL DEFAULT '0' COMMENT 'GPS时间',
  `altitude` varchar(50) NOT NULL DEFAULT '0' COMMENT 'GPS纬度',
  `longitude` varchar(50) NOT NULL COMMENT 'GPS经度',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `uuid` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8 COMMENT='太阳能控制板运行数据';

-- 数据导出被取消选择。
-- 导出  表 solar.so_running_data_2017_46 结构
DROP TABLE IF EXISTS `so_running_data_2017_46`;
CREATE TABLE IF NOT EXISTS `so_running_data_2017_46` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'KEY',
  `dev_id` int(11) DEFAULT '0' COMMENT '设备ID',
  `real_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '客户端调用时间',
  `warnning_code` int(11) NOT NULL DEFAULT '0' COMMENT '警告码(0:正常)',
  `ip_addr` varchar(40) NOT NULL DEFAULT '0' COMMENT 'IP地址',
  `charging_voltage` float NOT NULL DEFAULT '0' COMMENT '太阳能充电电压',
  `charging_current` float NOT NULL DEFAULT '0' COMMENT '太阳能充电电流',
  `dc_charging_voltage` float NOT NULL DEFAULT '0' COMMENT 'DC充电电压',
  `dc_charging_current` float NOT NULL DEFAULT '0' COMMENT 'DC充电电流',
  `battery_voltage` float NOT NULL DEFAULT '0' COMMENT '电池电压',
  `battery_charging_current` float NOT NULL DEFAULT '0' COMMENT '电池充电电流',
  `rt_ntc_temperature` float NOT NULL DEFAULT '0' COMMENT 'NTC实时温度值',
  `load_current_1` float NOT NULL DEFAULT '0' COMMENT '负载1工作电流',
  `load_current_2` float NOT NULL DEFAULT '0' COMMENT '负载2工作电流',
  `load_current_3` float NOT NULL DEFAULT '0' COMMENT '负载3工作电流',
  `load_current_4` float NOT NULL DEFAULT '0' COMMENT '负载4工作电流',
  `gps_info` varchar(100) NOT NULL DEFAULT '0' COMMENT 'GPS信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
