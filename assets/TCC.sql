-- MySQL Script generated by MySQL Workbench
-- 11/05/14 09:38:35
-- Model: New Model    Version: 1.0
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema TCC
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `TCC` ;
CREATE SCHEMA IF NOT EXISTS `TCC` DEFAULT CHARACTER SET utf8 ;
USE `TCC` ;

-- -----------------------------------------------------
-- Table `TCC`.`STATUS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TCC`.`STATUS` (
  `ID_STATUS` BIGINT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(40) NOT NULL,
  `DESCRIPTION` VARCHAR(255) NULL,
  PRIMARY KEY (`ID_STATUS`),
  UNIQUE INDEX `STATUS_NAME_UK` (`NAME` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TCC`.`USERS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TCC`.`USERS` (
  `ID_USER` BIGINT NOT NULL AUTO_INCREMENT,
  `ID_STATUS` BIGINT NOT NULL,
  `NICKNAME` VARCHAR(40) NOT NULL,
  `PASSWORD` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`ID_USER`),
  UNIQUE INDEX `USERS_NICKNAME_UK` (`NICKNAME` ASC),
  INDEX `USERS_ID_STATUS_FK_idx` (`ID_STATUS` ASC),
  CONSTRAINT `USERS_ID_STATUS_FK`
    FOREIGN KEY (`ID_STATUS`)
    REFERENCES `TCC`.`STATUS` (`ID_STATUS`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TCC`.`ESTABLISHMENTS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TCC`.`ESTABLISHMENTS` (
  `ID_ESTABLISHMENT` BIGINT NOT NULL AUTO_INCREMENT,
  `ID_USER` BIGINT NOT NULL,
  `NAME` VARCHAR(120) NOT NULL COMMENT '		',
  `ADDRESS` VARCHAR(120) NOT NULL,
  `ABOUT` VARCHAR(255) NOT NULL,
  `LATITUDE` DECIMAL(10,8) NOT NULL,
  `LONGITUDE` DECIMAL(11,8) NOT NULL,
  `AVATAR` TEXT NULL,
  PRIMARY KEY (`ID_ESTABLISHMENT`),
  UNIQUE INDEX `ESTABLISHMENTS_NAME_LAT_LONG_ADDRESS_UK` (`NAME` ASC, `LATITUDE` ASC, `LONGITUDE` ASC, `ADDRESS` ASC),
  UNIQUE INDEX `ESTABLISHMENTS_ID_USER_UK` (`ID_USER` ASC),
  CONSTRAINT `ESTABLISHMENTS_ID_USER_FK`
    FOREIGN KEY (`ID_USER`)
    REFERENCES `TCC`.`USERS` (`ID_USER`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TCC`.`PROMOTIONS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TCC`.`PROMOTIONS` (
  `ID_PROMOTION` BIGINT NOT NULL AUTO_INCREMENT,
  `ID_ESTABLISHMENT` BIGINT NOT NULL,
  `TITLE` VARCHAR(40) NOT NULL,
  `LIMIT_SCORE` BIGINT NOT NULL,
  `DESCRIPTION` VARCHAR(255) NOT NULL,
  `CREATION` DATETIME NOT NULL,
  `ACTIVE` BIT NOT NULL,
  `AVATAR` TEXT NULL,
  PRIMARY KEY (`ID_PROMOTION`),
  UNIQUE INDEX `PROMOTIONS_ID_ESTABLISHMENT_TITLE_UK` (`ID_ESTABLISHMENT` ASC, `TITLE` ASC),
  CONSTRAINT `PROMOTIONS_ID_ESTABLISHMENT_FK`
    FOREIGN KEY (`ID_ESTABLISHMENT`)
    REFERENCES `TCC`.`ESTABLISHMENTS` (`ID_ESTABLISHMENT`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TCC`.`CLIENTS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TCC`.`CLIENTS` (
  `ID_CLIENT` BIGINT NOT NULL AUTO_INCREMENT,
  `ID_USER` BIGINT NOT NULL,
  `NAME` VARCHAR(120) NOT NULL,
  `BIRTHDAY` DATE NOT NULL,
  `EMAIL` VARCHAR(255) NOT NULL,
  `SEX` CHAR(1) NOT NULL,
  PRIMARY KEY (`ID_CLIENT`),
  UNIQUE INDEX `CLIENTS_EMAIL_UK` (`EMAIL` ASC),
  UNIQUE INDEX `CLIENTS_ID_USER_UK` (`ID_USER` ASC),
  CONSTRAINT `CLIENTS_ID_USER_FK`
    FOREIGN KEY (`ID_USER`)
    REFERENCES `TCC`.`USERS` (`ID_USER`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TCC`.`CLIENTS_PROMOTIONS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TCC`.`CLIENTS_PROMOTIONS` (
  `ID_CLIENTS_PROMOTIONS` BIGINT NOT NULL AUTO_INCREMENT,
  `ID_CLIENT` BIGINT NOT NULL,
  `ID_PROMOTION` BIGINT NOT NULL,
  `CURRENT_SCORE` BIGINT NOT NULL,
  `COMPLETE` BIT NOT NULL,
  `UPDATE_TIME` DATETIME NOT NULL,
  INDEX `CLIENTS_PROMOTIONS_ID_PROMOTION_IDX` (`ID_PROMOTION` ASC),
  PRIMARY KEY (`ID_CLIENTS_PROMOTIONS`),
  CONSTRAINT `CLIENTS_PROMOTIONS_ID_CLIENT_FK`
    FOREIGN KEY (`ID_CLIENT`)
    REFERENCES `TCC`.`CLIENTS` (`ID_CLIENT`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `CLIENTS_PROMOTIONS_ID_PROMOTION_FK`
    FOREIGN KEY (`ID_PROMOTION`)
    REFERENCES `TCC`.`PROMOTIONS` (`ID_PROMOTION`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TCC`.`CLIENTS_PROMOTIONS_HISTORY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TCC`.`CLIENTS_PROMOTIONS_HISTORY` (
  `ID_CLIENTS_PROMOTIONS` BIGINT NOT NULL,
  `ID_CLIENT` BIGINT NOT NULL,
  `ID_PROMOTION` BIGINT NOT NULL,
  `DATE` DATETIME NOT NULL,
  `SCORE` BIGINT NOT NULL,
  PRIMARY KEY (`ID_CLIENT`, `ID_PROMOTION`, `DATE`, `ID_CLIENTS_PROMOTIONS`),
  INDEX `CLIENTS_PROMOTIONS_HISTORY_ID_PROMOTION_IDX` (`ID_PROMOTION` ASC),
  INDEX `CLIENTS_PROMOTIONS_HISTORY_ID_CLIENTS_PROMOTIONS_FK_idx` (`ID_CLIENTS_PROMOTIONS` ASC),
  CONSTRAINT `CLIENTS_PROMOTIONS_HISTORY_ID_CLIENT_FK`
    FOREIGN KEY (`ID_CLIENT`)
    REFERENCES `TCC`.`CLIENTS` (`ID_CLIENT`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `CLIENTS_PROMOTIONS_HISTORY_ID_PROMOTION_FK`
    FOREIGN KEY (`ID_PROMOTION`)
    REFERENCES `TCC`.`PROMOTIONS` (`ID_PROMOTION`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `CLIENTS_PROMOTIONS_HISTORY_ID_CLIENTS_PROMOTIONS_FK`
    FOREIGN KEY (`ID_CLIENTS_PROMOTIONS`)
    REFERENCES `TCC`.`CLIENTS_PROMOTIONS` (`ID_CLIENTS_PROMOTIONS`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TCC`.`ROLES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TCC`.`ROLES` (
  `ID_ROLE` BIGINT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(40) NOT NULL,
  `DESCRIPTION` VARCHAR(255) NULL,
  PRIMARY KEY (`ID_ROLE`),
  UNIQUE INDEX `ROLES_NAME_UK` (`NAME` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TCC`.`USERS_ROLES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TCC`.`USERS_ROLES` (
  `ID_USER` BIGINT NOT NULL,
  `ID_ROLE` BIGINT NOT NULL,
  PRIMARY KEY (`ID_USER`, `ID_ROLE`),
  INDEX `USERS_ROLES_ID_ROLE_IDX` (`ID_ROLE` ASC),
  CONSTRAINT `USERS_ROLES_ID_USER_FK`
    FOREIGN KEY (`ID_USER`)
    REFERENCES `TCC`.`USERS` (`ID_USER`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `USERS_ROLES_ID_ROLE_FK`
    FOREIGN KEY (`ID_ROLE`)
    REFERENCES `TCC`.`ROLES` (`ID_ROLE`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
