-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema shopping-cart
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema shopping-cart
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `shopping-cart` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `shopping-cart` ;

-- -----------------------------------------------------
-- Table `shopping-cart`.`product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopping-cart`.`product` ;

CREATE TABLE IF NOT EXISTS `shopping-cart`.`product` (
  `pid` VARCHAR(45) NOT NULL,
  `brand` VARCHAR(100) NULL DEFAULT NULL,
  `category` VARCHAR(20) NULL DEFAULT NULL,
  `modal` VARCHAR(350) NULL DEFAULT NULL,
  `price` DECIMAL(12,2) NULL DEFAULT NULL,
  `stock` INT NULL DEFAULT NULL,
  `Discount` INT NULL DEFAULT NULL,
  `image` LONGBLOB NULL DEFAULT NULL,
  PRIMARY KEY (`pid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shopping-cart`.`orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopping-cart`.`orders` ;

CREATE TABLE IF NOT EXISTS `shopping-cart`.`orders` (
  `orderid` VARCHAR(45) NOT NULL,
  `prodid` VARCHAR(45) NOT NULL,
  `quantity` INT NULL DEFAULT NULL,
  `amount` DECIMAL(10,2) NULL DEFAULT NULL,
  `shipped` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`orderid`, `prodid`),
  INDEX `productid_idx` (`prodid` ASC) VISIBLE,
  CONSTRAINT `productid`
    FOREIGN KEY (`prodid`)
    REFERENCES `shopping-cart`.`product` (`pid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shopping-cart`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopping-cart`.`user` ;

CREATE TABLE IF NOT EXISTS `shopping-cart`.`user` (
  `email` VARCHAR(60) NOT NULL,
  `name` VARCHAR(30) NULL DEFAULT NULL,
  `mobile` BIGINT NULL DEFAULT NULL,
  `credit` INT DEFAULT 0,
  `password` VARCHAR(20) NULL DEFAULT NULL,
  PRIMARY KEY (`email`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shopping-cart`.`transactions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopping-cart`.`transactions` ;

CREATE TABLE IF NOT EXISTS `shopping-cart`.`transactions` (
  `transid` VARCHAR(45) NOT NULL,
  `username` VARCHAR(60) NULL DEFAULT NULL,
  `time` DATETIME NULL DEFAULT NULL,
  `amount` DECIMAL(10,2) NULL DEFAULT NULL,
  PRIMARY KEY (`transid`),
  INDEX `truserid_idx` (`username` ASC) VISIBLE,
  CONSTRAINT `truserid`
    FOREIGN KEY (`username`)
    REFERENCES `shopping-cart`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `transorderid`
    FOREIGN KEY (`transid`)
    REFERENCES `shopping-cart`.`orders` (`orderid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shopping-cart`.`user_demand`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopping-cart`.`user_demand` ;

CREATE TABLE IF NOT EXISTS `shopping-cart`.`user_demand` (
  `username` VARCHAR(60) NOT NULL,
  `prodid` VARCHAR(45) NOT NULL,
  `quantity` INT NULL DEFAULT NULL,
  PRIMARY KEY (`username`, `prodid`),
  INDEX `prodid_idx` (`prodid` ASC) VISIBLE,
  CONSTRAINT `userdemailemail`
    FOREIGN KEY (`username`)
    REFERENCES `shopping-cart`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `prodid`
    FOREIGN KEY (`prodid`)
    REFERENCES `shopping-cart`.`product` (`pid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `shopping-cart`.`usercart`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shopping-cart`.`usercart` ;

CREATE TABLE IF NOT EXISTS `shopping-cart`.`usercart` (
  `username` VARCHAR(60) NULL DEFAULT NULL,
  `prodid` VARCHAR(45) NULL DEFAULT NULL,
  `quantity` INT NULL DEFAULT NULL,
  INDEX `useremail_idx` (`username` ASC) VISIBLE,
  INDEX `prodidcart_idx` (`prodid` ASC) VISIBLE,
  CONSTRAINT `useremail`
    FOREIGN KEY (`username`)
    REFERENCES `shopping-cart`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `prodidcart`
    FOREIGN KEY (`prodid`)
    REFERENCES `shopping-cart`.`product` (`pid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

START TRANSACTION;
USE `shopping-cart`;
INSERT INTO `shopping-cart`.`product` (`pid`, `brand`, `category`, `modal`, `price`, `stock`, `image`) VALUES (`P20230423082243`,`Apple`,`Laptop`,70000,8,xx);
INSERT INTO `shopping-cart`.`product` (`pid`, `brand`, `category`, `modal`, `price`, `stock`, `image`) VALUES (`P20230423082244`,`LG`,`TV`,50000,1,xx);

COMMIT;


-- Data for table `shopping-cart`.`orders`
-- -----------------------------------------------------
START TRANSACTION;
USE `shopping-cart`;
INSERT INTO `shopping-cart`.`orders` (`orderid`, `prodid`, `quantity`, `amount`, `shipped`) VALUES ('TR10001', 'P20230423082243', 1, 125999, 0);

COMMIT;


-- -----------------------------------------------------
-- Data for table `shopping-cart`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `shopping-cart`;
INSERT INTO `shopping-cart`.`user` (`email`, `name`, `mobile`, `credit`, `password`) VALUES ('guest@gmail.com', 'Guest User', 9876543234, 8797, 'guest');
INSERT INTO `shopping-cart`.`user` (`email`, `name`, `mobile`, `credit`, `password`) VALUES ('admin@zoho.com', 'Admin User', 9876543459, 0, 'xyzzy');

COMMIT;


-- -----------------------------------------------------
-- Data for table `shopping-cart`.`transactions`
-- -----------------------------------------------------
START TRANSACTION;
USE `shopping-cart`;
INSERT INTO `shopping-cart`.`transactions` (`transid`, `username`, `time`, `amount`) VALUES ('TR10001', 'guest@gmail.com', '2023-04-23 09:26:56', 125999);

COMMIT;


-- -----------------------------------------------------
-- Data for table `shopping-cart`.`user_demand`
-- -----------------------------------------------------
START TRANSACTION;
USE `shopping-cart`;
INSERT INTO `shopping-cart`.`user_demand` (`username`, `prodid`, `quantity`) VALUES ('guest@gmail.com', 'P20230423084144', 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `shopping-cart`.`usercart`
-- -----------------------------------------------------
START TRANSACTION;
USE `shopping-cart`;
INSERT INTO `shopping-cart`.`usercart` (`username`, `prodid`, `quantity`) VALUES ('guest@gmail.com', 'P20230423082243', 2);

COMMIT;

