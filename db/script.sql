-- Sistema de compras

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema compra
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `compra` ;

-- -----------------------------------------------------
-- Schema compra
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `compra` DEFAULT CHARACTER SET utf8 ;
USE `compra` ;

-- -----------------------------------------------------
-- Table `compra`.`Suplidor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `compra`.`Suplidor` ;

CREATE TABLE IF NOT EXISTS `compra`.`Suplidor` (
  `SuplidorId` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `SuplidorNombre` VARCHAR(100) NOT NULL,
  `SuplidorEmail` VARCHAR(254) NULL DEFAULT NULL,
  `SuplidorTelefono` VARCHAR(15) NULL DEFAULT NULL,
  `SuplidorDireccion` VARCHAR(95) NULL DEFAULT NULL,
  `SuplidorCodigoPostal` VARCHAR(10) NULL DEFAULT NULL,
  `SuplidorCiudad` VARCHAR(35) NULL DEFAULT NULL,
  `SuplidorPais` VARCHAR(70) NULL DEFAULT NULL,
  `SuplidorEstatus` CHAR(1) NOT NULL DEFAULT 'A',
  PRIMARY KEY (`SuplidorId`),
  UNIQUE INDEX `SuplidorTelefono_UNIQUE` (`SuplidorTelefono` ASC),
  UNIQUE INDEX `SuplidorEmail_UNIQUE` (`SuplidorEmail` ASC),
  UNIQUE INDEX `SuplidorDireccion_UNIQUE` (`SuplidorDireccion` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 28
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `compra`.`Orden`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `compra`.`Orden` ;

CREATE TABLE IF NOT EXISTS `compra`.`Orden` (
  `OrdenId` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `OrdenNumero` INT(10) UNSIGNED NOT NULL,
  `SuplidorId` INT(10) UNSIGNED NULL DEFAULT NULL,
  `OrdenFecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `OrdenEstatus` CHAR(1) NULL DEFAULT 'P',
  `OrdenTotalBruto` DECIMAL(12,2) NULL DEFAULT '0.00',
  `OrdenTotalDescto` DECIMAL(8,2) NULL DEFAULT '0.00',
  `OrdenTotalImpuesto` DECIMAL(8,2) NULL DEFAULT '0.00',
  `OrdenTotalCargo` DECIMAL(8,2) NULL DEFAULT '200.00',
  `OrdenTotalNeto` DECIMAL(12,2) NULL DEFAULT '0.00',
  PRIMARY KEY (`OrdenId`),
  INDEX `FK_SuplidorId_idx` (`SuplidorId` ASC),
  CONSTRAINT `FK_SuplidorId`
    FOREIGN KEY (`SuplidorId`)
    REFERENCES `compra`.`Suplidor` (`SuplidorId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 30
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `compra`.`Producto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `compra`.`Producto` ;

CREATE TABLE IF NOT EXISTS `compra`.`Producto` (
  `ProductoId` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `ProductoCodigo` CHAR(24) NULL DEFAULT NULL,
  `ProductoNombre` VARCHAR(40) NOT NULL,
  `ProductoDescripcion` VARCHAR(70) NOT NULL,
  `ProductoUnidadesStock` INT(10) UNSIGNED NULL DEFAULT NULL,
  `ProductoPrecio` DECIMAL(10,2) UNSIGNED NOT NULL,
  `ProductoEstatus` CHAR(1) NULL DEFAULT 'A',
  PRIMARY KEY (`ProductoId`),
  UNIQUE INDEX `UNIQUE_ProductoCodigo` (`ProductoCodigo` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `compra`.`OrdenDetalle`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `compra`.`OrdenDetalle` ;

CREATE TABLE IF NOT EXISTS `compra`.`OrdenDetalle` (
  `OrdenDetalleLinea` SMALLINT(1) UNSIGNED NOT NULL AUTO_INCREMENT,
  `OrdenId` INT(10) UNSIGNED NOT NULL,
  `ProductoId` INT(10) UNSIGNED NULL DEFAULT NULL,
  `OrdenDetalleCantidad` INT(10) UNSIGNED NOT NULL,
  `OrdenDetallePrecio` DECIMAL(10,2) UNSIGNED NOT NULL,
  `OrdenDetalleDescuento` DECIMAL(8,2) NULL DEFAULT '300.00',
  `OrdenDetalleImpuesto` DECIMAL(8,2) NULL DEFAULT '35.00',
  `OrdenDetalleNeto` DECIMAL(10,2) UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`OrdenDetalleLinea`, `OrdenId`),
  INDEX `FK_ProductoId_idx` (`ProductoId` ASC),
  INDEX `FK_OrdenId_idx` (`OrdenId` ASC),
  CONSTRAINT `FK_OrdenId`
    FOREIGN KEY (`OrdenId`)
    REFERENCES `compra`.`Orden` (`OrdenId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ProductoId`
    FOREIGN KEY (`ProductoId`)
    REFERENCES `compra`.`Producto` (`ProductoId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 17
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `compra`.`ProductoHis`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `compra`.`ProductoHis` ;

CREATE TABLE IF NOT EXISTS `compra`.`ProductoHis` (
  `ProductoId` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `ProductoCodigo` CHAR(24) NULL DEFAULT NULL,
  `ProductoNombre` VARCHAR(40) NOT NULL,
  `ProductoDescripcion` VARCHAR(70) NOT NULL,
  `ProductoUnidadesStock` INT(10) UNSIGNED NULL DEFAULT NULL,
  `ProductoPrecio` DECIMAL(10,2) UNSIGNED NOT NULL,
  `ProductoEstatus` CHAR(1) NULL DEFAULT 'A',
  `ProductoHisFecha` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ProductoId`),
  UNIQUE INDEX `UNIQUE_ProductoCodigo` (`ProductoCodigo` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `compra`.`productolog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `compra`.`productolog` ;

CREATE TABLE IF NOT EXISTS `compra`.`productolog` (
  `ProductoId` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `ProductoCodigo` CHAR(24) NULL DEFAULT NULL,
  `ProductoNombre` VARCHAR(40) NOT NULL,
  `ProductoDescripcion` VARCHAR(70) NOT NULL,
  `ProductoUnidadesStock` INT(10) UNSIGNED NULL DEFAULT NULL,
  `ProductoPrecio` DECIMAL(10,2) UNSIGNED NOT NULL,
  `ProductoEstatus` CHAR(1) NULL DEFAULT 'A',
  `ProductoLogUsuario` VARCHAR(45) NOT NULL,
  `ProductoLogFecha` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ProductoId`),
  UNIQUE INDEX `UNIQUE_ProductoCodigo` (`ProductoCodigo` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `compra`.`suplidorhis`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `compra`.`suplidorhis` ;

CREATE TABLE IF NOT EXISTS `compra`.`suplidorhis` (
  `SuplidorId` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `SuplidorNombre` VARCHAR(100) NOT NULL,
  `SuplidorEmail` VARCHAR(254) NULL DEFAULT NULL,
  `SuplidorTelefono` VARCHAR(15) NULL DEFAULT NULL,
  `SuplidorDireccion` VARCHAR(95) NULL DEFAULT NULL,
  `SuplidorCodigoPostal` VARCHAR(10) NULL DEFAULT NULL,
  `SuplidorCiudad` VARCHAR(35) NULL DEFAULT NULL,
  `SuplidorPais` VARCHAR(70) NULL DEFAULT NULL,
  `SuplidorEstatus` CHAR(1) NOT NULL DEFAULT 'A',
  `SuplidorHisFecha` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`SuplidorId`),
  UNIQUE INDEX `SuplidorTelefono_UNIQUE` (`SuplidorTelefono` ASC),
  UNIQUE INDEX `SuplidorEmail_UNIQUE` (`SuplidorEmail` ASC),
  UNIQUE INDEX `SuplidorDireccion_UNIQUE` (`SuplidorDireccion` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `compra`.`SuplidorLog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `compra`.`SuplidorLog` ;

CREATE TABLE IF NOT EXISTS `compra`.`SuplidorLog` (
  `SuplidorId` INT(10) UNSIGNED NOT NULL,
  `SuplidorNombre` VARCHAR(100) NOT NULL,
  `SuplidorEmail` VARCHAR(254) NULL DEFAULT NULL,
  `SuplidorTelefono` VARCHAR(15) NULL DEFAULT NULL,
  `SuplidorDireccion` VARCHAR(95) NULL DEFAULT NULL,
  `SuplidorCodigoPostal` VARCHAR(10) NULL DEFAULT NULL,
  `SuplidorCiudad` VARCHAR(35) NULL DEFAULT NULL,
  `SuplidorPais` VARCHAR(70) NULL DEFAULT NULL,
  `SuplidorEstatus` CHAR(1) NOT NULL DEFAULT 'A',
  `SuplidorLogFecha` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `SuplidorLogUsuario` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`SuplidorId`),
  UNIQUE INDEX `SuplidorTelefono_UNIQUE` (`SuplidorTelefono` ASC),
  UNIQUE INDEX `SuplidorEmail_UNIQUE` (`SuplidorEmail` ASC),
  UNIQUE INDEX `SuplidorDireccion_UNIQUE` (`SuplidorDireccion` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

USE `compra` ;

-- -----------------------------------------------------
-- Placeholder table for view `compra`.`producto_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `compra`.`producto_view` (`Id` INT, `Codigo` INT, `Nombre` INT, `Descripcion` INT, `Unidades_Stock` INT, `Precio` INT);

-- -----------------------------------------------------
-- Placeholder table for view `compra`.`suplidor_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `compra`.`suplidor_view` (`Id` INT, `Nombre` INT, `Email` INT, `Telefono` INT, `Direccion` INT, `Codigo_Postal` INT, `Ciudad` INT, `Pais` INT);

-- -----------------------------------------------------
-- procedure OrdenDetalle_Insertar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`OrdenDetalle_Insertar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `OrdenDetalle_Insertar`(
	id INT,
    productoId INT,
    precio DECIMAL(10,2),
    cantidad INT,
    descuento DECIMAL(8,2),
    impuesto DECIMAL(8,2)
)
BEGIN

	INSERT INTO OrdenDetalle(OrdenId, ProductoId,
    OrdenDetallePrecio, OrdenDetalleCantidad, OrdenDetalleDescuento, 
    OrdenDetalleImpuesto, OrdenDetalleNeto)
    VALUES
    (
		id, productoId, precio, cantidad, descuento, impuesto, 
        (precio * cantidad) + impuesto - descuento);
        
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure OrdenDetalle_Mostrar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`OrdenDetalle_Mostrar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `OrdenDetalle_Mostrar`(
	numero INT
)
BEGIN
SELECT  OrdenDetalleLinea AS Linea, 
ProductoCodigo AS Codigo_Producto, 
ProductoNombre AS Producto, 
ProductoDescripcion AS Descripcion, 
OrdenDetalleCantidad AS Cantidad,
OrdenDetallePrecio AS Precio,
OrdenDetalleDescuento AS Descuento, 
OrdenDetalleImpuesto AS Impuesto, 
OrdenDetalleNeto AS Neto
FROM OrdenDetalle d
INNER JOIN Producto p
ON d.ProductoId = p.ProductoId
INNER JOIN Orden o
ON o.OrdenId = d.OrdenId
WHERE o.OrdenNumero = numero
ORDER BY OrdenDetalleLinea ASC
LIMIT 15;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Orden_Eliminar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Orden_Eliminar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Orden_Eliminar`(
id INT
)
BEGIN

UPDATE Orden
SET OrdenEstatus = 'C'
WHERE OrdenId = id;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Orden_Insertar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Orden_Insertar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Orden_Insertar`(
OUT id INT,
suplidorId INT)
BEGIN
INSERT INTO Orden(OrdenNumero, SuplidorId)
VALUES
(0, suplidorId);
SET id = last_insert_id();
UPDATE Orden
SET OrdenNumero = id
WHERE OrdenId = id;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Orden_Mostrar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Orden_Mostrar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Orden_Mostrar`()
BEGIN
SELECT s.SuplidorId AS No, 
s.SuplidorNombre AS Nombre, 
OrdenId AS Id, OrdenNumero AS Numero,  
cast(OrdenFecha AS DATE) AS Fecha, cast(OrdenFecha AS TIME) AS Hora,
OrdenTotalBruto AS Total_Bruto, 
OrdenTotalDescto AS Total_Descuento, 
OrdenTotalImpuesto AS Total_Impuesto,
OrdenTotalCargo AS Total_Cargo, 
(OrdenTotalNeto + OrdenTotalCargo) AS 
Total_Neto
FROM Orden o 
INNER JOIN Suplidor s
ON o.SuplidorId = s.SuplidorId
WHERE OrdenEstatus = 'P'
ORDER BY OrdenId DESC
LIMIT 200; 
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure ProductoLog_Insertar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`ProductoLog_Insertar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `ProductoLog_Insertar`(
id INT,
codigo CHAR(24),
nombre VARCHAR(40),
descripcion VARCHAR(70), 
precio DECIMAL(10,2),
unidadesStock INT,
estatus CHAR(1)
)
BEGIN
INSERT INTO ProductoLog(ProductoId, ProductoCodigo, ProductoNombre, ProductoDescripcion,
 ProductoPrecio, ProductoUnidadesStock, ProductoEstatus, ProductoLogUsuario)
VALUES (id, codigo, nombre, descripcion, precio, unidadesStock, estatus, user());
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Producto_Actualizar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Producto_Actualizar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Producto_Actualizar`(
id INT,
codigo CHAR(24),
nombre VARCHAR(40),
descripcion VARCHAR(70), 
precio DECIMAL(10,2),
unidadesStock INT,
estatus CHAR(1))
BEGIN
UPDATE Producto
SET ProductoNombre = nombre,
ProductoDescripcion = descripcion,
ProductoPrecio = precio,
ProductoUnidadesStock = unidadesStock,
ProductoCodigo = codigo,
ProductoEstatus = estatus
WHERE ProductoId = id;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Producto_Buscar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Producto_Buscar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Producto_Buscar`(
nombre VARCHAR(40))
BEGIN
SELECT ProductoId as ID,
ProductoCodigo AS Codigo,
ProductoNombre AS Nombre,
ProductoDescripcion AS Descripcion,
ProductoUnidadesStock AS Unidades_Stock,
ProductoPrecio AS Precio, 
ProductoEstatus AS Estatus 
FROM Producto
WHERE ProductoNombre LIKE CONCAT(nombre, '%');
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Producto_Eliminar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Producto_Eliminar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Producto_Eliminar`(id int
)
BEGIN
UPDATE Producto
SET ProductoEstatus = 'I'
WHERE ProductoId = id;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Producto_Insertar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Producto_Insertar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Producto_Insertar`(
codigo CHAR(24),
nombre VARCHAR(40),
descripcion VARCHAR(70), 
precio DECIMAL(10,2),
unidadesStock INT
)
BEGIN
INSERT INTO Producto(ProductoCodigo, ProductoNombre, ProductoDescripcion,
ProductoPrecio, ProductoUnidadesStock)
VALUES (codigo, nombre, descripcion, precio, unidadesStock);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Producto_Mostrar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Producto_Mostrar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Producto_Mostrar`()
BEGIN
SELECT ProductoId as ID,
ProductoCodigo AS Codigo,
ProductoNombre AS Nombre,
ProductoDescripcion AS Descripcion,
ProductoUnidadesStock AS Unidades_Stock,
ProductoPrecio AS Precio, 
ProductoEstatus AS Estatus 
FROM Producto
ORDER BY ProductoId DESC
LIMIT 200;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Producto_MostrarActivos
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Producto_MostrarActivos`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Producto_MostrarActivos`()
BEGIN
SELECT * FROM producto_view;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure SuplidorLog_Insertar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`SuplidorLog_Insertar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `SuplidorLog_Insertar`(
id INT,
nombre VARCHAR(100),
direccion VARCHAR(95), 
ciudad VARCHAR(35),
email VARCHAR(254),
telefono VARCHAR(15),
codigoPostal VARCHAR(10),
pais VARCHAR(70),
estatus CHAR(1)
)
BEGIN
INSERT INTO SuplidorLog(SuplidorId,
SuplidorNombre, SuplidorDireccion,
SuplidorCiudad, 
SuplidorEmail, 
SuplidorTelefono, 
SuplidorCodigoPostal,
SuplidorPais, SuplidorEstatus, SuplidorLogUsuario)

VALUES (id,
nombre, direccion, ciudad, email, telefono, 
codigoPostal, pais, estatus, user());

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Suplidor_Actualizar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Suplidor_Actualizar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Suplidor_Actualizar`(
id INT ,
nombre VARCHAR(100),
direccion NVARCHAR(60), 
ciudad VARCHAR(15),
email VARCHAR(254),
telefono VARCHAR(15),
codigoPostal VARCHAR(10),
pais VARCHAR(55),
estatus CHAR(1)
)
BEGIN
UPDATE Suplidor
SET SuplidorNombre = nombre,
SuplidorDireccion = direccion,
SuplidorCiudad = ciudad,
SuplidorEmail = email,
SuplidorTelefono = telefono,
SuplidorCodigoPostal = codigoPostal,
SuplidorPais = pais,
SuplidorEstatus = estatus
WHERE SuplidorId = id;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Suplidor_Buscar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Suplidor_Buscar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Suplidor_Buscar`(
nombre VARCHAR(100)
)
BEGIN
SELECT SuplidorId AS Id, 
SuplidorNombre AS Nombre, 
SuplidorEmail AS Email, 
SuplidorTelefono AS Telefono,
SuplidorDireccion AS Direccion, 
SuplidorCodigoPostal AS Codigo_Postal, 
SuplidorCiudad AS Ciudad,
SuplidorPais AS Pais,
SuplidorEstatus AS Estatus
FROM Suplidor
WHERE SuplidorNombre LIKE CONCAT(nombre, '%');
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Suplidor_BuscarActivos
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Suplidor_BuscarActivos`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Suplidor_BuscarActivos`(
nombre VARCHAR(100)
)
BEGIN
SELECT SuplidorId AS Id, 
SuplidorNombre AS Nombre, 
SuplidorEmail AS Email, 
SuplidorTelefono AS Telefono,
SuplidorDireccion AS Direccion, 
SuplidorCodigoPostal AS Codigo_Postal, 
SuplidorCiudad AS Ciudad,
SuplidorPais AS Pais
FROM Suplidor
WHERE SuplidorNombre LIKE CONCAT(nombre, '%')
AND SuplidorEstatus = 'A';
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Suplidor_Eliminar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Suplidor_Eliminar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Suplidor_Eliminar`(
id int
)
BEGIN
UPDATE Suplidor
SET SuplidorEstatus = 'I'
WHERE SuplidorId = id;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Suplidor_Insertar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Suplidor_Insertar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Suplidor_Insertar`(
nombre VARCHAR(100),
direccion VARCHAR(95), 
ciudad VARCHAR(35),
email VARCHAR(254),
telefono VARCHAR(15),
codigoPostal VARCHAR(10),
pais VARCHAR(70)
)
BEGIN

INSERT INTO Suplidor(
SuplidorNombre, SuplidorDireccion,
SuplidorCiudad, 
SuplidorEmail, 
SuplidorTelefono, 
SuplidorCodigoPostal,
SuplidorPais)

VALUES (
nombre, direccion, ciudad, email, telefono, 
codigoPostal, pais);

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Suplidor_Mostrar
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Suplidor_Mostrar`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Suplidor_Mostrar`()
BEGIN
SELECT SuplidorId AS Id, 
SuplidorNombre AS Nombre, 
SuplidorEmail AS Email,
SuplidorTelefono AS Telefono,
SuplidorDireccion AS Direccion, 
SuplidorCodigoPostal AS Codigo_Postal,
SuplidorCiudad AS Ciudad,
SuplidorPais AS Pais,
SuplidorEstatus AS Estatus
FROM Suplidor
ORDER BY SuplidorId DESC
LIMIT 200;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure Suplidor_MostrarActivos
-- -----------------------------------------------------

USE `compra`;
DROP procedure IF EXISTS `compra`.`Suplidor_MostrarActivos`;

DELIMITER $$
USE `compra`$$
CREATE DEFINER=`student`@`%` PROCEDURE `Suplidor_MostrarActivos`()
BEGIN
SELECT * FROM suplidor_view;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- View `compra`.`producto_view`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `compra`.`producto_view` ;
DROP TABLE IF EXISTS `compra`.`producto_view`;
USE `compra`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`student`@`%` SQL SECURITY DEFINER VIEW `compra`.`producto_view` AS select `p`.`ProductoId` AS `Id`,`p`.`ProductoCodigo` AS `Codigo`,`p`.`ProductoNombre` AS `Nombre`,`p`.`ProductoDescripcion` AS `Descripcion`,`p`.`ProductoUnidadesStock` AS `Unidades_Stock`,`p`.`ProductoPrecio` AS `Precio` from `compra`.`producto` `p` where ((`p`.`ProductoEstatus` = 'A') and (`p`.`ProductoUnidadesStock` > 0)) order by `p`.`ProductoId`,`p`.`ProductoCodigo`,`p`.`ProductoNombre`;

-- -----------------------------------------------------
-- View `compra`.`suplidor_view`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `compra`.`suplidor_view` ;
DROP TABLE IF EXISTS `compra`.`suplidor_view`;
USE `compra`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`student`@`%` SQL SECURITY DEFINER VIEW `compra`.`suplidor_view` AS select `compra`.`suplidor`.`SuplidorId` AS `Id`,`compra`.`suplidor`.`SuplidorNombre` AS `Nombre`,`compra`.`suplidor`.`SuplidorEmail` AS `Email`,`compra`.`suplidor`.`SuplidorTelefono` AS `Telefono`,`compra`.`suplidor`.`SuplidorDireccion` AS `Direccion`,`compra`.`suplidor`.`SuplidorCodigoPostal` AS `Codigo_Postal`,`compra`.`suplidor`.`SuplidorCiudad` AS `Ciudad`,`compra`.`suplidor`.`SuplidorPais` AS `Pais` from `compra`.`suplidor` where (`compra`.`suplidor`.`SuplidorEstatus` = 'A') order by `compra`.`suplidor`.`SuplidorId`,`compra`.`suplidor`.`SuplidorNombre`;
USE `compra`;

DELIMITER $$

USE `compra`$$
DROP TRIGGER IF EXISTS `compra`.`suplidor_AFTER_INSERT` $$
USE `compra`$$
CREATE
DEFINER=`student`@`%`
TRIGGER `compra`.`suplidor_AFTER_INSERT`
AFTER INSERT ON `compra`.`suplidor`
FOR EACH ROW
BEGIN
DECLARE id INT;
DECLARE nombre VARCHAR(100);
DECLARE direccion VARCHAR(95); 
DECLARE ciudad VARCHAR(35);
DECLARE email VARCHAR(254);
DECLARE telefono VARCHAR(15);
DECLARE codigoPostal VARCHAR(10);
DECLARE pais VARCHAR(70);
DECLARE estatus CHAR(1);

SET id = NEW.SuplidorId;
SET nombre = NEW.SuplidorNombre;
SET direccion = NEW.SuplidorDireccion;
SET ciudad = NEW.SuplidorCiudad;
SET email = NEW.SuplidorEmail;
SET telefono = NEW.SuplidorTelefono;
SET codigoPostal = NEW.SuplidorCodigoPostal;
SET pais = NEW.SuplidorPais;
SET estatus = NEW.SuplidorEstatus;
CALL SuplidorLog_Insertar(id, nombre, direccion, ciudad, email, 
telefono, codigoPostal, pais, estatus);
END$$


USE `compra`$$
DROP TRIGGER IF EXISTS `compra`.`suplidor_AFTER_UPDATE` $$
USE `compra`$$
CREATE
DEFINER=`student`@`%`
TRIGGER `compra`.`suplidor_AFTER_UPDATE`
AFTER UPDATE ON `compra`.`suplidor`
FOR EACH ROW
BEGIN
DECLARE id INT;
DECLARE nombre VARCHAR(100);
DECLARE direccion VARCHAR(95); 
DECLARE ciudad VARCHAR(35);
DECLARE email VARCHAR(254);
DECLARE telefono VARCHAR(15);
DECLARE codigoPostal VARCHAR(10);
DECLARE pais VARCHAR(70);
DECLARE estatus CHAR(1);

SET id = NEW.SuplidorId;
SET nombre = NEW.SuplidorNombre;
SET direccion = NEW.SuplidorDireccion;
SET ciudad = NEW.SuplidorCiudad;
SET email = NEW.SuplidorEmail;
SET telefono = NEW.SuplidorTelefono;
SET codigoPostal = NEW.SuplidorCodigoPostal;
SET pais = NEW.SuplidorPais;
SET estatus = NEW.SuplidorEstatus;
CALL SuplidorLog_Insertar(id, nombre, direccion, ciudad, email, 
telefono, codigoPostal, pais, estatus);
END$$


USE `compra`$$
DROP TRIGGER IF EXISTS `compra`.`suplidor_AFTER_DELETE` $$
USE `compra`$$
CREATE
DEFINER=`student`@`%`
TRIGGER `compra`.`suplidor_AFTER_DELETE`
AFTER DELETE ON `compra`.`suplidor`
FOR EACH ROW
BEGIN
INSERT INTO SuplidorHis(SuplidorId,
SuplidorNombre, SuplidorDireccion,
SuplidorCiudad, 
SuplidorEmail, 
SuplidorTelefono, 
SuplidorCodigoPostal,
SuplidorPais, SuplidorEstatus)

VALUES (OLD.SuplidorId,
OLD.SuplidorNombre, OLD.SuplidorDireccion, OLD.SuplidorCiudad, OLD.SuplidorEmail, OLD.SuplidorTelefono, 
OLD.SuplidorCodigoPostal, OLD.SuplidorPais, OLD.SuplidorEstatus);

END$$


USE `compra`$$
DROP TRIGGER IF EXISTS `compra`.`producto_AFTER_INSERT` $$
USE `compra`$$
CREATE
DEFINER=`student`@`%`
TRIGGER `compra`.`producto_AFTER_INSERT`
AFTER INSERT ON `compra`.`producto`
FOR EACH ROW
BEGIN
DECLARE id INT;
DECLARE codigo CHAR(24);
DECLARE nombre VARCHAR(40);
DECLARE descripcion VARCHAR(70); 
DECLARE precio DECIMAL(10,2);
DECLARE unidadesStock INT;
DECLARE estatus CHAR(1);
SET id = NEW.ProductoId;
SET codigo = NEW.ProductoCodigo;
SET nombre = NEW.ProductoNombre;
SET descripcion = NEW.ProductoDescripcion;
SET precio = NEW.ProductoPrecio;
SET unidadesStock = NEW.ProductoUnidadesStock;
SET estatus = NEW.ProductoEstatus;

CALL ProductoLog_Insertar(id, codigo, nombre, descripcion, precio,
unidadesStock, estatus);

END$$


USE `compra`$$
DROP TRIGGER IF EXISTS `compra`.`producto_AFTER_UPDATE` $$
USE `compra`$$
CREATE
DEFINER=`student`@`%`
TRIGGER `compra`.`producto_AFTER_UPDATE`
AFTER UPDATE ON `compra`.`producto`
FOR EACH ROW
BEGIN
DECLARE id INT;
DECLARE codigo CHAR(24);
DECLARE nombre VARCHAR(40);
DECLARE descripcion VARCHAR(70); 
DECLARE precio DECIMAL(10,2);
DECLARE unidadesStock INT;
DECLARE estatus CHAR(1);
SET id = NEW.ProductoId;
SET codigo = NEW.ProductoCodigo;
SET nombre = NEW.ProductoNombre;
SET descripcion = NEW.ProductoDescripcion;
SET precio = NEW.ProductoPrecio;
SET unidadesStock = NEW.ProductoUnidadesStock;
SET estatus = NEW.ProductoEstatus;

CALL ProductoLog_Insertar(id, codigo, nombre, descripcion, precio,
unidadesStock, estatus);
END$$


USE `compra`$$
DROP TRIGGER IF EXISTS `compra`.`producto_AFTER_DELETE` $$
USE `compra`$$
CREATE
DEFINER=`student`@`%`
TRIGGER `compra`.`producto_AFTER_DELETE`
AFTER DELETE ON `compra`.`producto`
FOR EACH ROW
BEGIN
INSERT INTO ProductoHis(ProductoId, 
ProductoCodigo, ProductoNombre, ProductoDescripcion,
ProductoPrecio, ProductoUnidadesStock, ProductoEstatus)
VALUES (OLD.ProductoId, OLD.ProductoCodigo, 
OLD.ProductoNombre, OLD.ProductoDescripcion, 
OLD.ProductoPrecio, OLD.ProductoUnidadesStock, OLD.ProductoEstatus);
END$$


USE `compra`$$
DROP TRIGGER IF EXISTS `compra`.`CalcularOrdenTotal` $$
USE `compra`$$
CREATE
DEFINER=`student`@`%`
TRIGGER `compra`.`CalcularOrdenTotal`
AFTER INSERT ON `compra`.`ordendetalle`
FOR EACH ROW
BEGIN

UPDATE Orden
SET OrdenTotalBruto = 
OrdenTotalBruto + NEW.OrdenDetallePrecio,
OrdenTotalImpuesto = 
OrdenTotalImpuesto + NEW.OrdenDetalleImpuesto,
OrdenTotalDescto = 
OrdenTotalDescto + NEW.OrdenDetalleDescuento,
OrdenTotalNeto = 
OrdenTotalNeto + NEW.OrdenDetalleNeto
WHERE Orden.OrdenId = NEW.OrdenId;

UPDATE Producto
SET ProductoUnidadesStock = 
ProductoUnidadesStock - NEW.OrdenDetalleCantidad
WHERE Producto.ProductoId = NEW.ProductoId;

END$$


DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

CREATE USER 'user'@'%' IDENTIFIED BY 'user123';

GRANT SELECT, INSERT, UPDATE, TRIGGER, EXECUTE ON compra.* TO 'user'@'%'

INSERT INTO Suplidor(SuplidorNombre, SuplidorEmail)
VALUES ('Apple Inc.', 'apple@example.com');


INSERT INTO Producto(ProductoCodigo, ProductoNombre, ProductoPrecio, ProductoUnidadesStock)
VALUES ('DW347QESAA79QWE36WE1', 'iPhone 7', 37000.00, 3);

INSERT INTO Orden(SuplidorId, OrdenNumero)
VALUES(1, 12);

INSERT INTO OrdenDetalle(OrdenId, ProductoId, OrdenDetalleCantidad)
VALUES(1, 1, 2);

CALL Suplidor_Mostrar();
CALL Producto_Mostrar();
CALL Orden_Mostrar();
CALL OrdenDetalle_Mostrar(1);