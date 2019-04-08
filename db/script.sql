DROP DATABASE IF EXISTS compra;

CREATE DATABASE compra
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

-- **********************SUPLIDOR*******************

-- SEQUENCE: suplidor_suplidorid_seq

-- DROP SEQUENCE suplidor_suplidorid_seq;

CREATE SEQUENCE suplidor_suplidorid_seq;

ALTER SEQUENCE suplidor_suplidorid_seq
    OWNER TO postgres;

-- Table: suplidor

-- DROP TABLE suplidor;

CREATE TABLE suplidor
(
    suplidorid integer NOT NULL DEFAULT nextval('suplidor_suplidorid_seq'::regclass),
    suplidornombre character varying(100) COLLATE pg_catalog."default" NOT NULL,
    suplidoremail character varying(254) COLLATE pg_catalog."default",
    suplidortelefono character varying(15) COLLATE pg_catalog."default",
    suplidordireccion character varying(95) COLLATE pg_catalog."default",
    suplidorcodigopostal character varying(10) COLLATE pg_catalog."default",
    suplidorciudad character varying(35) COLLATE pg_catalog."default",
    suplidorpais character varying(70) COLLATE pg_catalog."default",
    suplidorestatus character(1) COLLATE pg_catalog."default" DEFAULT 'A'::bpchar,
    CONSTRAINT pk_suplidor PRIMARY KEY (suplidorid),
    CONSTRAINT un_suplidordireccion UNIQUE (suplidordireccion)
,
    CONSTRAINT un_suplidoremail UNIQUE (suplidoremail)
,
    CONSTRAINT un_suplidortelefono UNIQUE (suplidortelefono)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE suplidor
    OWNER to postgres;

-- Table: suplidorhis

-- DROP TABLE suplidorhis;

CREATE TABLE suplidorhis
(
    suplidorhisfecha timestamp with time zone DEFAULT CURRENT_TIMESTAMP(0)
)
    INHERITS (suplidor)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE suplidorhis
    OWNER to postgres;

-- Table: suplidorlog

-- DROP TABLE suplidorlog;

CREATE TABLE suplidorlog
(
    suplidorlogusuario character varying(45) COLLATE pg_catalog."default",
    suplidorlogfecha timestamp with time zone DEFAULT CURRENT_TIMESTAMP(0)
)
    INHERITS (suplidor)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE suplidorlog
    OWNER to postgres;

-- FUNCTION: "Suplidor_HISTORICO"()

-- DROP FUNCTION "Suplidor_HISTORICO"();

CREATE FUNCTION "Suplidor_HISTORICO"()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF 
AS $BODY$ BEGIN
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

END;$BODY$;

ALTER FUNCTION "Suplidor_HISTORICO"()
    OWNER TO postgres;

-- Trigger: HISTORICO

-- DROP TRIGGER "HISTORICO" ON suplidor;

CREATE TRIGGER "HISTORICO"
    AFTER DELETE
    ON suplidor
    FOR EACH ROW
    EXECUTE PROCEDURE "Suplidor_HISTORICO"();

-- FUNCTION: "Suplidor_LOG"()

-- DROP FUNCTION "Suplidor_LOG"();

CREATE FUNCTION "Suplidor_LOG"()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF 
AS $BODY$ BEGIN

INSERT INTO SuplidorLog(SuplidorId,
SuplidorNombre, SuplidorDireccion,
SuplidorCiudad, 
SuplidorEmail, 
SuplidorTelefono, 
SuplidorCodigoPostal,
SuplidorPais, SuplidorEstatus, SuplidorLogUsuario)

VALUES (NEW.SuplidorId,
NEW.SuplidorNombre, NEW.SuplidorDireccion, NEW.SuplidorCiudad, NEW.SuplidorEmail, NEW.SuplidorTelefono, 
NEW.SuplidorCodigoPostal, NEW.SuplidorPais, NEW.SuplidorEstatus, current_user);
RETURN NEW;
END;$BODY$;

ALTER FUNCTION "Suplidor_LOG"()
    OWNER TO postgres;

-- Trigger: LOG

-- DROP TRIGGER "LOG" ON suplidor;

CREATE TRIGGER "LOG"
    AFTER INSERT OR UPDATE 
    ON suplidor
    FOR EACH ROW
    EXECUTE PROCEDURE "Suplidor_LOG"();

-- ***************** PRODUCTO******************

-- SEQUENCE: producto_productoid_seq

-- DROP SEQUENCE producto_productoid_seq;

CREATE SEQUENCE producto_productoid_seq;

ALTER SEQUENCE producto_productoid_seq
    OWNER TO postgres;

-- *************-FUNCIONES Suplidor-***************

-- FUNCTION: suplidor_buscar(character varying)

-- DROP FUNCTION suplidor_buscar(character varying);

CREATE OR REPLACE FUNCTION suplidor_buscar(
	nom character varying)
    RETURNS TABLE(id integer, nombre character varying, email character varying, telefono character varying, direccion character varying, codigopostal character varying, ciudad character varying, pais character varying, estatus character) 
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$ BEGIN
RETURN QUERY
SELECT suplidorid id, 
suplidornombre nombre, 
suplidoremail email,
suplidortelefono telefono,
suplidordireccion direccion, 
suplidorcodigopostal codigopostal,
suplidorciudad ciudad,
suplidorpais pais,
suplidorestatus estatus
FROM suplidor
WHERE SuplidorNombre LIKE CONCAT(nom, '%')
ORDER BY suplidorid DESC
LIMIT 200;
END;$BODY$;


ALTER FUNCTION suplidor_buscar(character varying)
    OWNER TO postgres;

-- FUNCTION: suplidor_buscaractivos(character varying)

-- DROP FUNCTION suplidor_buscaractivos(character varying);

CREATE OR REPLACE FUNCTION suplidor_buscaractivos(
	nom character varying)
    RETURNS TABLE(id integer, nombre character varying, email character varying, telefono character varying, direccion character varying, codigopostal character varying, ciudad character varying, pais character varying) 
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$BEGIN
RETURN QUERY
SELECT SuplidorId AS Id, 
SuplidorNombre AS Nombre, 
SuplidorEmail AS Email, 
SuplidorTelefono AS Telefono,
SuplidorDireccion AS Direccion, 
SuplidorCodigoPostal AS CodigoPostal, 
SuplidorCiudad AS Ciudad,
SuplidorPais AS Pais
FROM Suplidor
WHERE SuplidorNombre LIKE CONCAT(nombre, '%')
AND SuplidorEstatus = 'A';
END;$BODY$;

ALTER FUNCTION suplidor_buscaractivos(character varying)
    OWNER TO postgres;

-- FUNCTION: suplidor_mostrar()

-- DROP FUNCTION suplidor_mostrar();

CREATE OR REPLACE FUNCTION suplidor_mostrar(
	)
    RETURNS TABLE(id integer, nombre character varying, email character varying, telefono character varying, direccion character varying, codigopostal character varying, ciudad character varying, pais character varying, estatus character) 
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$ BEGIN
RETURN QUERY
SELECT suplidorid, 
suplidornombre, 
suplidoremail,
suplidortelefono,
suplidordireccion, 
suplidorcodigopostal,
suplidorciudad,
suplidorpais,
suplidorestatus
FROM suplidor
ORDER BY suplidorid DESC
LIMIT 200;
END;$BODY$;

ALTER FUNCTION suplidor_mostrar()
    OWNER TO postgres;


-- FUNCTION: suplidor_mostraractivos()

-- DROP FUNCTION suplidor_mostraractivos();

CREATE OR REPLACE FUNCTION suplidor_mostraractivos(
	)
    RETURNS TABLE(id integer, nombre character varying, email character varying, telefono character varying, direccion character varying, codigopostal character varying, ciudad character varying, pais character varying, estatus character) 
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$ BEGIN
RETURN QUERY
SELECT suplidorid, 
suplidornombre, 
suplidoremail,
suplidortelefono,
suplidordireccion, 
suplidorcodigopostal,
suplidorciudad,
suplidorpais,
suplidorestatus
FROM suplidor
WHERE SuplidorEstatus = 'A'
ORDER BY suplidorid DESC
LIMIT 200;
END;$BODY$;

ALTER FUNCTION suplidor_mostraractivos()
    OWNER TO postgres;

-- PRODUCTO
-- Table: producto

-- DROP TABLE producto;

CREATE TABLE producto
(
    productoid integer NOT NULL DEFAULT nextval('producto_productoid_seq'::regclass),
    productocodigo character(24) COLLATE pg_catalog."default",
    productonombre character varying(40) COLLATE pg_catalog."default" NOT NULL,
    productodescripcion character varying(70) COLLATE pg_catalog."default",
    productounidadesstock integer,
    productoprecio numeric(10,2),
    productoestatus character(1) COLLATE pg_catalog."default" DEFAULT 'A'::bpchar,
    CONSTRAINT pk_producto PRIMARY KEY (productoid),
    CONSTRAINT un_productocodigo UNIQUE (productocodigo)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE producto
    OWNER to postgres;

-- FUNCTION: "Producto_HISTORICO"()

-- DROP FUNCTION "Producto_HISTORICO"();

CREATE FUNCTION "Producto_HISTORICO"()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF 
AS $BODY$ BEGIN
INSERT INTO ProductoHis(ProductoId, 
ProductoCodigo, ProductoNombre, ProductoDescripcion,
ProductoPrecio, ProductoUnidadesStock, ProductoEstatus)
VALUES (OLD.ProductoId, OLD.ProductoCodigo, 
OLD.ProductoNombre, OLD.ProductoDescripcion, 
OLD.ProductoPrecio, OLD.ProductoUnidadesStock, OLD.ProductoEstatus);
END;$BODY$;

ALTER FUNCTION "Producto_HISTORICO"()
    OWNER TO postgres;

-- Trigger: HISTORICO

-- DROP TRIGGER "HISTORICO" ON producto;

CREATE TRIGGER "HISTORICO"
    AFTER DELETE
    ON producto
    FOR EACH ROW
    EXECUTE PROCEDURE "Producto_HISTORICO"();


-- FUNCTION: "Producto_LOG"()

-- DROP FUNCTION "Producto_LOG"();

CREATE FUNCTION "Producto_LOG"()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF 
AS $BODY$ BEGIN
INSERT INTO ProductoLog(ProductoId, ProductoCodigo, ProductoNombre, ProductoDescripcion,
 ProductoPrecio, ProductoUnidadesStock, ProductoEstatus, ProductoLogUsuario)
VALUES (NEW.ProductoId, NEW.ProductoCodigo, NEW.ProductoNombre, NEW.ProductoDescripcion, NEW.ProductoPrecio, NEW.ProductoUnidadesStock, NEW.ProductoEstatus, current_user);
RETURN NEW;
END;$BODY$;

ALTER FUNCTION "Producto_LOG"()
    OWNER TO postgres;

-- Trigger: LOG

-- DROP TRIGGER "LOG" ON producto;

CREATE TRIGGER "LOG"
    AFTER INSERT OR UPDATE 
    ON producto
    FOR EACH ROW
    EXECUTE PROCEDURE "Producto_LOG"();

-- Table: productolog

-- DROP TABLE productolog;

CREATE TABLE productolog
(
    productologusuario character varying(45) COLLATE pg_catalog."default",
    productologfecha timestamp with time zone DEFAULT CURRENT_TIMESTAMP(0)
)
    INHERITS (producto)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE productolog
    OWNER to postgres;

-- Table: productohis

-- DROP TABLE productohis;

CREATE TABLE productohis
(
    productohisfecha timestamp with time zone DEFAULT CURRENT_TIMESTAMP(0)
)
    INHERITS (producto)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE productohis
    OWNER to postgres;

-- -------------------- FUNCIONES PRODUCTO ---------------------

-- FUNCTION: public.producto_buscar(character varying)

-- DROP FUNCTION public.producto_buscar(character varying);

CREATE OR REPLACE FUNCTION public.producto_buscar(
	nom character varying)
    RETURNS TABLE(id integer, codigo character, nombre character varying, descripcion character varying, unidades_stock integer, precio numeric, estatus character) 
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$BEGIN
RETURN QUERY
SELECT ProductoId ID,
ProductoCodigo Codigo,
ProductoNombre Nombre,
ProductoDescripcion Descripcion,
ProductoUnidadesStock Unidades_Stock,
ProductoPrecio Precio, 
ProductoEstatus Estatus 
FROM Producto
WHERE ProductoNombre LIKE CONCAT(nom, '%');
END;$BODY$;

ALTER FUNCTION public.producto_buscar(character varying)
    OWNER TO postgres;

-- FUNCTION: producto_mostrar()

-- DROP FUNCTION producto_mostrar();

CREATE OR REPLACE FUNCTION producto_mostrar(
	)
    RETURNS TABLE(id integer, codigo character, nombre character varying, descripcion character varying, unidades_stock integer, precio numeric, estatus character) 
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$ BEGIN
RETURN QUERY
SELECT ProductoId,
ProductoCodigo,
ProductoNombre,
ProductoDescripcion,
ProductoUnidadesStock,
ProductoPrecio, 
ProductoEstatus 
FROM Producto
ORDER BY ProductoId DESC
LIMIT 200;
END;$BODY$;

ALTER FUNCTION producto_mostrar()
    OWNER TO postgres;

-- FUNCTION: producto_mostraractivos()

-- DROP FUNCTION producto_mostraractivos();

CREATE OR REPLACE FUNCTION producto_mostraractivos(
	)
    RETURNS TABLE(id integer, codigo character, nombre character varying, descripcion character varying, unidades_stock integer, precio numeric, estatus character) 
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$ BEGIN
RETURN QUERY
SELECT ProductoId ID,
ProductoCodigo Codigo,
ProductoNombre Nombre,
ProductoDescripcion Descripcion,
ProductoUnidadesStock Unidades_Stock,
ProductoPrecio Precio, 
ProductoEstatus Estatus 
FROM Producto
WHERE ProductoEstatus = 'A'
ORDER BY ProductoId DESC
LIMIT 200;
END;$BODY$;

ALTER FUNCTION producto_mostraractivos()
    OWNER TO postgres;


-- ************** ORDEN *********************
-- SEQUENCE: orden_ordenid_seq

-- DROP SEQUENCE orden_ordenid_seq;

CREATE SEQUENCE orden_ordenid_seq;

ALTER SEQUENCE orden_ordenid_seq
    OWNER TO postgres;

CREATE TABLE public.orden
(
    ordenid integer NOT NULL DEFAULT nextval('orden_ordenid_seq'::regclass),
    ordennumero integer NOT NULL DEFAULT nextval('orden_ordenid_seq'::regclass),
    suplidorid integer,
    ordenfecha timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    ordenestatus character(1) COLLATE pg_catalog."default" DEFAULT 'P'::bpchar,
    ordentotalbruto numeric(12,2) DEFAULT 0.00,
    ordentotaldescto numeric(8,2) DEFAULT 0.00,
    ordentotalimpuesto numeric(8,2) DEFAULT 0.00,
    ordentotalcargo numeric(8,2) DEFAULT 200.00,
    ordentotalneto numeric(12,2) DEFAULT 0.00,
    CONSTRAINT pk_orden PRIMARY KEY (ordenid),
    CONSTRAINT "FK_SuplidorId" FOREIGN KEY (suplidorid)
        REFERENCES public.suplidor (suplidorid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.orden
    OWNER to postgres;

-- Table: public.orden

-- DROP TABLE public.orden;

CREATE TABLE public.orden
(
    ordenid integer NOT NULL DEFAULT nextval('orden_ordenid_seq'::regclass),
    ordennumero integer NOT NULL DEFAULT nextval('orden_ordenid_seq'::regclass),
    suplidorid integer,
    ordenfecha timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    ordenestatus character(1) COLLATE pg_catalog."default" DEFAULT 'P'::bpchar,
    ordentotalbruto numeric(12,2) DEFAULT 0.00,
    ordentotaldescto numeric(8,2) DEFAULT 0.00,
    ordentotalimpuesto numeric(8,2) DEFAULT 0.00,
    ordentotalcargo numeric(8,2) DEFAULT 200.00,
    ordentotalneto numeric(12,2) DEFAULT 0.00,
    CONSTRAINT pk_orden PRIMARY KEY (ordenid),
    CONSTRAINT "FK_SuplidorId" FOREIGN KEY (suplidorid)
        REFERENCES public.suplidor (suplidorid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.orden
    OWNER to postgres;

-- SEQUENCE: ordendetalle_ordendetallelinea_seq

-- DROP SEQUENCE ordendetalle_ordendetallelinea_seq;

CREATE SEQUENCE ordendetalle_ordendetallelinea_seq;

ALTER SEQUENCE ordendetalle_ordendetallelinea_seq
    OWNER TO postgres;

-- Table: ordendetalle

-- DROP TABLE ordendetalle;

CREATE TABLE ordendetalle
(
    ordendetallelinea smallint NOT NULL DEFAULT nextval('ordendetalle_ordendetallelinea_seq'::regclass),
    ordenid integer NOT NULL,
    productoid integer,
    ordendetallecantidad integer,
    ordendetalleprecio numeric(10,2),
    ordendetalledescuento numeric(8,2) DEFAULT 300.00,
    ordendetalleimpuesto numeric(8,2) DEFAULT 35.00,
    ordendetalleneto numeric(10,2) DEFAULT 0.00,
    CONSTRAINT pk_ordendetalle PRIMARY KEY (ordendetallelinea, ordenid),
    CONSTRAINT fk_ordenid FOREIGN KEY (ordenid)
        REFERENCES orden (ordenid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_productoid FOREIGN KEY (productoid)
        REFERENCES producto (productoid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE ordendetalle
    OWNER to postgres;

-- FUNCTION: "Calcular_ORDEN_TOTAL"()

-- DROP FUNCTION "Calcular_ORDEN_TOTAL"();

CREATE FUNCTION "Calcular_ORDEN_TOTAL"()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF 
AS $BODY$ BEGIN

UPDATE Orden
SET OrdenTotalBruto = 
OrdenTotalBruto + NEW.OrdenDetallePrecio * NEW.OrdenDetalleCantidad,
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
WHERE ProductoId = NEW.ProductoId;
RETURN NULL;
END;$BODY$;

ALTER FUNCTION "Calcular_ORDEN_TOTAL"()
    OWNER TO postgres;

-- Trigger: ORDEN_TOTAL

-- DROP TRIGGER "ORDEN_TOTAL" ON ordendetalle;

CREATE TRIGGER "ORDEN_TOTAL"
    AFTER INSERT
    ON ordendetalle
    FOR EACH ROW
    EXECUTE PROCEDURE "Calcular_ORDEN_TOTAL"();

-- *************FUNCIONES DE ORDEN**********
-- FUNCTION: orden_mostrar()

-- DROP FUNCTION orden_mostrar();

CREATE OR REPLACE FUNCTION orden_mostrar(
	)
    RETURNS TABLE(no integer, nombre character varying, id integer, numero integer, fecha date, hora time without time zone, total_bruto numeric, total_descuento numeric, total_impuesto numeric, total_cargo numeric, total_neto numeric) 
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$BEGIN
RETURN QUERY
SELECT s.SuplidorId N, 
s.SuplidorNombre Nombre, 
OrdenId Id, OrdenNumero Numero,  
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
END;$BODY$;

ALTER FUNCTION orden_mostrar()
    OWNER TO postgres;

-- FUNCTION: ordendetalle_insertar(integer, integer, numeric, integer, numeric, numeric)

-- DROP FUNCTION ordendetalle_insertar(integer, integer, numeric, integer, numeric, numeric);

CREATE OR REPLACE FUNCTION ordendetalle_insertar(
	id integer,
	productoid integer,
	precio numeric,
	cantidad integer,
	descuento numeric,
	impuesto numeric)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$ BEGIN
INSERT INTO ordendetalle(ordenid, productoid,
    OrdenDetallePrecio, OrdenDetalleCantidad, OrdenDetalleDescuento, 
    OrdenDetalleImpuesto, OrdenDetalleNeto)
    VALUES
    (
		id, productoid, precio, cantidad, descuento, impuesto, 
        (precio * cantidad) + impuesto - descuento);
END;   $BODY$;

ALTER FUNCTION ordendetalle_insertar(integer, integer, numeric, integer, numeric, numeric)
    OWNER TO postgres;

-- FUNCTION: ordendetalle_mostrar(integer)

-- DROP FUNCTION ordendetalle_mostrar(integer);

CREATE OR REPLACE FUNCTION ordendetalle_mostrar(
	numero integer)
    RETURNS TABLE(linea smallint, codigo_producto character, producto character varying, descripcion character varying, cantidad integer, precio numeric, descuento numeric, impuesto numeric, neto numeric) 
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
    ROWS 1000
AS $BODY$ BEGIN
RETURN QUERY
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
END;$BODY$;

ALTER FUNCTION ordendetalle_mostrar(integer)
    OWNER TO postgres;


-- VIEWS