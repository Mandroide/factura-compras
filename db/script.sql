--
-- PostgreSQL database dump
--

-- Dumped from database version 13.1
-- Dumped by pg_dump version 13.1

-- Started on 2020-11-22 11:39:07

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE compra;
--
-- TOC entry 3094 (class 1262 OID 16394)
-- Name: compra; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE compra WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'English_United States.1252';


ALTER DATABASE compra OWNER TO postgres;

\connect compra

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 233 (class 1255 OID 16796)
-- Name: Calcular_ORDEN_TOTAL(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."Calcular_ORDEN_TOTAL"() RETURNS trigger
    LANGUAGE plpgsql
AS $$ BEGIN

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
END;$$;


ALTER FUNCTION public."Calcular_ORDEN_TOTAL"() OWNER TO postgres;

--
-- TOC entry 234 (class 1255 OID 16735)
-- Name: Producto_HISTORICO(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."Producto_HISTORICO"() RETURNS trigger
    LANGUAGE plpgsql
AS $$ BEGIN
    INSERT INTO ProductoHis(ProductoId,
                            ProductoCodigo, ProductoNombre, ProductoDescripcion,
                            ProductoPrecio, ProductoUnidadesStock, ProductoEstatus)
    VALUES (OLD.ProductoId, OLD.ProductoCodigo,
            OLD.ProductoNombre, OLD.ProductoDescripcion,
            OLD.ProductoPrecio, OLD.ProductoUnidadesStock, OLD.ProductoEstatus);
END;$$;


ALTER FUNCTION public."Producto_HISTORICO"() OWNER TO postgres;

--
-- TOC entry 235 (class 1255 OID 16737)
-- Name: Producto_LOG(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."Producto_LOG"() RETURNS trigger
    LANGUAGE plpgsql
AS $$ BEGIN
    INSERT INTO ProductoLog(ProductoId, ProductoCodigo, ProductoNombre, ProductoDescripcion,
                            ProductoPrecio, ProductoUnidadesStock, ProductoEstatus, ProductoLogUsuario)
    VALUES (NEW.ProductoId, NEW.ProductoCodigo, NEW.ProductoNombre, NEW.ProductoDescripcion, NEW.ProductoPrecio, NEW.ProductoUnidadesStock, NEW.ProductoEstatus, current_user);
    RETURN NEW;
END;$$;


ALTER FUNCTION public."Producto_LOG"() OWNER TO postgres;

--
-- TOC entry 236 (class 1255 OID 16716)
-- Name: Suplidor_HISTORICO(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."Suplidor_HISTORICO"() RETURNS trigger
    LANGUAGE plpgsql
AS $$ BEGIN
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

END;$$;


ALTER FUNCTION public."Suplidor_HISTORICO"() OWNER TO postgres;

--
-- TOC entry 237 (class 1255 OID 16718)
-- Name: Suplidor_LOG(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public."Suplidor_LOG"() RETURNS trigger
    LANGUAGE plpgsql
AS $$ BEGIN

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
END;$$;


ALTER FUNCTION public."Suplidor_LOG"() OWNER TO postgres;

--
-- TOC entry 223 (class 1255 OID 16798)
-- Name: orden_mostrar(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.orden_mostrar() RETURNS TABLE(no integer, nombre character varying, id integer, numero integer, fecha date, hora time without time zone, total_bruto numeric, total_descuento numeric, total_impuesto numeric, total_cargo numeric, total_neto numeric)
    LANGUAGE plpgsql
AS $$BEGIN
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
END;$$;


ALTER FUNCTION public.orden_mostrar() OWNER TO postgres;

--
-- TOC entry 224 (class 1255 OID 16799)
-- Name: ordendetalle_insertar(integer, integer, numeric, integer, numeric, numeric); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.ordendetalle_insertar(id integer, productoid integer, precio numeric, cantidad integer, descuento numeric, impuesto numeric) RETURNS void
    LANGUAGE plpgsql
AS $$ BEGIN
    INSERT INTO ordendetalle(ordenid, productoid,
                             OrdenDetallePrecio, OrdenDetalleCantidad, OrdenDetalleDescuento,
                             OrdenDetalleImpuesto, OrdenDetalleNeto)
    VALUES
    (
        id, productoid, precio, cantidad, descuento, impuesto,
        (precio * cantidad) + impuesto - descuento);
END;   $$;


ALTER FUNCTION public.ordendetalle_insertar(id integer, productoid integer, precio numeric, cantidad integer, descuento numeric, impuesto numeric) OWNER TO postgres;

--
-- TOC entry 225 (class 1255 OID 16800)
-- Name: ordendetalle_mostrar(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.ordendetalle_mostrar(numero integer) RETURNS TABLE(linea smallint, codigo_producto character, producto character varying, descripcion character varying, cantidad integer, precio numeric, descuento numeric, impuesto numeric, neto numeric)
    LANGUAGE plpgsql
AS $$ BEGIN
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
END;$$;


ALTER FUNCTION public.ordendetalle_mostrar(numero integer) OWNER TO postgres;

--
-- TOC entry 226 (class 1255 OID 16751)
-- Name: producto_buscar(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.producto_buscar(nom character varying) RETURNS TABLE(id integer, codigo character, nombre character varying, descripcion character varying, unidades_stock integer, precio numeric, estatus character)
    LANGUAGE plpgsql
AS $$BEGIN
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
END;$$;


ALTER FUNCTION public.producto_buscar(nom character varying) OWNER TO postgres;

--
-- TOC entry 227 (class 1255 OID 16752)
-- Name: producto_mostrar(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.producto_mostrar() RETURNS TABLE(id integer, codigo character, nombre character varying, descripcion character varying, unidades_stock integer, precio numeric, estatus character)
    LANGUAGE plpgsql
AS $$ BEGIN
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
END;$$;


ALTER FUNCTION public.producto_mostrar() OWNER TO postgres;

--
-- TOC entry 228 (class 1255 OID 16753)
-- Name: producto_mostraractivos(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.producto_mostraractivos() RETURNS TABLE(id integer, codigo character, nombre character varying, descripcion character varying, unidades_stock integer, precio numeric, estatus character)
    LANGUAGE plpgsql
AS $$ BEGIN
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
END;$$;


ALTER FUNCTION public.producto_mostraractivos() OWNER TO postgres;

--
-- TOC entry 229 (class 1255 OID 16722)
-- Name: suplidor_buscar(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.suplidor_buscar(nom character varying) RETURNS TABLE(id integer, nombre character varying, email character varying, telefono character varying, direccion character varying, codigopostal character varying, ciudad character varying, pais character varying, estatus character)
    LANGUAGE plpgsql
AS $$ BEGIN
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
END;$$;


ALTER FUNCTION public.suplidor_buscar(nom character varying) OWNER TO postgres;

--
-- TOC entry 230 (class 1255 OID 16723)
-- Name: suplidor_buscaractivos(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.suplidor_buscaractivos(nom character varying) RETURNS TABLE(id integer, nombre character varying, email character varying, telefono character varying, direccion character varying, codigopostal character varying, ciudad character varying, pais character varying)
    LANGUAGE plpgsql
AS $$BEGIN
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
END;$$;


ALTER FUNCTION public.suplidor_buscaractivos(nom character varying) OWNER TO postgres;

--
-- TOC entry 231 (class 1255 OID 16724)
-- Name: suplidor_mostrar(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.suplidor_mostrar() RETURNS TABLE(id integer, nombre character varying, email character varying, telefono character varying, direccion character varying, codigopostal character varying, ciudad character varying, pais character varying, estatus character)
    LANGUAGE plpgsql
AS $$ BEGIN
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
END;$$;


ALTER FUNCTION public.suplidor_mostrar() OWNER TO postgres;

--
-- TOC entry 232 (class 1255 OID 16725)
-- Name: suplidor_mostraractivos(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.suplidor_mostraractivos() RETURNS TABLE(id integer, nombre character varying, email character varying, telefono character varying, direccion character varying, codigopostal character varying, ciudad character varying, pais character varying, estatus character)
    LANGUAGE plpgsql
AS $$ BEGIN
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
END;$$;


ALTER FUNCTION public.suplidor_mostraractivos() OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 16754)
-- Name: orden_ordenid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.orden_ordenid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.orden_ordenid_seq OWNER TO postgres;

SET default_table_access_method = heap;

--
-- TOC entry 209 (class 1259 OID 16756)
-- Name: orden; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orden (
                              ordenid integer DEFAULT nextval('public.orden_ordenid_seq'::regclass) NOT NULL,
                              ordennumero integer DEFAULT nextval('public.orden_ordenid_seq'::regclass) NOT NULL,
                              suplidorid integer,
                              ordenfecha timestamp with time zone DEFAULT CURRENT_TIMESTAMP(0) NOT NULL,
                              ordenestatus character(1) DEFAULT 'P'::bpchar,
                              ordentotalbruto numeric(12,2) DEFAULT 0.00,
                              ordentotaldescto numeric(8,2) DEFAULT 0.00,
                              ordentotalimpuesto numeric(8,2) DEFAULT 0.00,
                              ordentotalcargo numeric(8,2) DEFAULT 200.00,
                              ordentotalneto numeric(12,2) DEFAULT 0.00
);


ALTER TABLE public.orden OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 16775)
-- Name: ordendetalle_ordendetallelinea_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ordendetalle_ordendetallelinea_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ordendetalle_ordendetallelinea_seq OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 16777)
-- Name: ordendetalle; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ordendetalle (
                                     ordendetallelinea smallint DEFAULT nextval('public.ordendetalle_ordendetallelinea_seq'::regclass) NOT NULL,
                                     ordenid integer NOT NULL,
                                     productoid integer,
                                     ordendetallecantidad integer,
                                     ordendetalleprecio numeric(10,2),
                                     ordendetalledescuento numeric(8,2) DEFAULT 300.00,
                                     ordendetalleimpuesto numeric(8,2) DEFAULT 35.00,
                                     ordendetalleneto numeric(10,2) DEFAULT 0.00
);


ALTER TABLE public.ordendetalle OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 16720)
-- Name: producto_productoid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.producto_productoid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.producto_productoid_seq OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 16726)
-- Name: producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto (
                                 productoid integer DEFAULT nextval('public.producto_productoid_seq'::regclass) NOT NULL,
                                 productocodigo character(24),
                                 productonombre character varying(40) NOT NULL,
                                 productodescripcion character varying(70),
                                 productounidadesstock integer,
                                 productoprecio numeric(10,2),
                                 productoestatus character(1) DEFAULT 'A'::bpchar
);


ALTER TABLE public.producto OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 16745)
-- Name: productohis; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.productohis (
    productohisfecha timestamp with time zone DEFAULT CURRENT_TIMESTAMP(0)
)
    INHERITS (public.producto);


ALTER TABLE public.productohis OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 16739)
-- Name: productolog; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.productolog (
                                    productologusuario character varying(45),
                                    productologfecha timestamp with time zone DEFAULT CURRENT_TIMESTAMP(0)
)
    INHERITS (public.producto);


ALTER TABLE public.productolog OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16680)
-- Name: suplidor_suplidorid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.suplidor_suplidorid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.suplidor_suplidorid_seq OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 16682)
-- Name: suplidor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.suplidor (
                                 suplidorid integer DEFAULT nextval('public.suplidor_suplidorid_seq'::regclass) NOT NULL,
                                 suplidornombre character varying(100) NOT NULL,
                                 suplidoremail character varying(254),
                                 suplidortelefono character varying(15),
                                 suplidordireccion character varying(95),
                                 suplidorcodigopostal character varying(10),
                                 suplidorciudad character varying(35),
                                 suplidorpais character varying(70),
                                 suplidorestatus character(1) DEFAULT 'A'::bpchar
);


ALTER TABLE public.suplidor OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16698)
-- Name: suplidorhis; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.suplidorhis (
    suplidorhisfecha timestamp with time zone DEFAULT CURRENT_TIMESTAMP(0)
)
    INHERITS (public.suplidor);


ALTER TABLE public.suplidorhis OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 16707)
-- Name: suplidorlog; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.suplidorlog (
                                    suplidorlogusuario character varying(45),
                                    suplidorlogfecha timestamp with time zone DEFAULT CURRENT_TIMESTAMP(0)
)
    INHERITS (public.suplidor);


ALTER TABLE public.suplidorlog OWNER TO postgres;

--
-- TOC entry 2919 (class 2604 OID 16748)
-- Name: productohis productoid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productohis ALTER COLUMN productoid SET DEFAULT nextval('public.producto_productoid_seq'::regclass);


--
-- TOC entry 2920 (class 2604 OID 16749)
-- Name: productohis productoestatus; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productohis ALTER COLUMN productoestatus SET DEFAULT 'A'::bpchar;


--
-- TOC entry 2916 (class 2604 OID 16742)
-- Name: productolog productoid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productolog ALTER COLUMN productoid SET DEFAULT nextval('public.producto_productoid_seq'::regclass);


--
-- TOC entry 2917 (class 2604 OID 16743)
-- Name: productolog productoestatus; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productolog ALTER COLUMN productoestatus SET DEFAULT 'A'::bpchar;


--
-- TOC entry 2908 (class 2604 OID 16701)
-- Name: suplidorhis suplidorid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suplidorhis ALTER COLUMN suplidorid SET DEFAULT nextval('public.suplidor_suplidorid_seq'::regclass);


--
-- TOC entry 2909 (class 2604 OID 16702)
-- Name: suplidorhis suplidorestatus; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suplidorhis ALTER COLUMN suplidorestatus SET DEFAULT 'A'::bpchar;


--
-- TOC entry 2911 (class 2604 OID 16710)
-- Name: suplidorlog suplidorid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suplidorlog ALTER COLUMN suplidorid SET DEFAULT nextval('public.suplidor_suplidorid_seq'::regclass);


--
-- TOC entry 2912 (class 2604 OID 16711)
-- Name: suplidorlog suplidorestatus; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suplidorlog ALTER COLUMN suplidorestatus SET DEFAULT 'A'::bpchar;


--
-- TOC entry 2948 (class 2606 OID 16769)
-- Name: orden pk_orden; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orden
    ADD CONSTRAINT pk_orden PRIMARY KEY (ordenid);


--
-- TOC entry 2950 (class 2606 OID 16785)
-- Name: ordendetalle pk_ordendetalle; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordendetalle
    ADD CONSTRAINT pk_ordendetalle PRIMARY KEY (ordendetallelinea, ordenid);


--
-- TOC entry 2944 (class 2606 OID 16732)
-- Name: producto pk_producto; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT pk_producto PRIMARY KEY (productoid);


--
-- TOC entry 2936 (class 2606 OID 16691)
-- Name: suplidor pk_suplidor; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suplidor
    ADD CONSTRAINT pk_suplidor PRIMARY KEY (suplidorid);


--
-- TOC entry 2946 (class 2606 OID 16734)
-- Name: producto un_productocodigo; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT un_productocodigo UNIQUE (productocodigo);


--
-- TOC entry 2938 (class 2606 OID 16693)
-- Name: suplidor un_suplidordireccion; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suplidor
    ADD CONSTRAINT un_suplidordireccion UNIQUE (suplidordireccion);


--
-- TOC entry 2940 (class 2606 OID 16695)
-- Name: suplidor un_suplidoremail; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suplidor
    ADD CONSTRAINT un_suplidoremail UNIQUE (suplidoremail);


--
-- TOC entry 2942 (class 2606 OID 16697)
-- Name: suplidor un_suplidortelefono; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suplidor
    ADD CONSTRAINT un_suplidortelefono UNIQUE (suplidortelefono);


--
-- TOC entry 2956 (class 2620 OID 16736)
-- Name: producto HISTORICO; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "HISTORICO" AFTER DELETE ON public.producto FOR EACH ROW EXECUTE FUNCTION public."Producto_HISTORICO"();


--
-- TOC entry 2954 (class 2620 OID 16717)
-- Name: suplidor HISTORICO; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "HISTORICO" AFTER DELETE ON public.suplidor FOR EACH ROW EXECUTE FUNCTION public."Suplidor_HISTORICO"();


--
-- TOC entry 2957 (class 2620 OID 16738)
-- Name: producto LOG; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "LOG" AFTER INSERT OR UPDATE ON public.producto FOR EACH ROW EXECUTE FUNCTION public."Producto_LOG"();


--
-- TOC entry 2955 (class 2620 OID 16719)
-- Name: suplidor LOG; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "LOG" AFTER INSERT OR UPDATE ON public.suplidor FOR EACH ROW EXECUTE FUNCTION public."Suplidor_LOG"();


--
-- TOC entry 2958 (class 2620 OID 16797)
-- Name: ordendetalle ORDEN_TOTAL; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "ORDEN_TOTAL" AFTER INSERT ON public.ordendetalle FOR EACH ROW EXECUTE FUNCTION public."Calcular_ORDEN_TOTAL"();


--
-- TOC entry 2951 (class 2606 OID 16770)
-- Name: orden FK_SuplidorId; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orden
    ADD CONSTRAINT "FK_SuplidorId" FOREIGN KEY (suplidorid) REFERENCES public.suplidor(suplidorid);


--
-- TOC entry 2952 (class 2606 OID 16786)
-- Name: ordendetalle fk_ordenid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordendetalle
    ADD CONSTRAINT fk_ordenid FOREIGN KEY (ordenid) REFERENCES public.orden(ordenid) ON DELETE CASCADE;


--
-- TOC entry 2953 (class 2606 OID 16791)
-- Name: ordendetalle fk_productoid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordendetalle
    ADD CONSTRAINT fk_productoid FOREIGN KEY (productoid) REFERENCES public.producto(productoid);


--
-- TOC entry 3095 (class 0 OID 0)
-- Dependencies: 3094
-- Name: DATABASE compra; Type: ACL; Schema: -; Owner: postgres
--

GRANT CONNECT ON DATABASE compra TO "user";


--
-- TOC entry 3096 (class 0 OID 0)
-- Dependencies: 233
-- Name: FUNCTION "Calcular_ORDEN_TOTAL"(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public."Calcular_ORDEN_TOTAL"() TO "user";


--
-- TOC entry 3097 (class 0 OID 0)
-- Dependencies: 234
-- Name: FUNCTION "Producto_HISTORICO"(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public."Producto_HISTORICO"() TO "user";


--
-- TOC entry 3098 (class 0 OID 0)
-- Dependencies: 235
-- Name: FUNCTION "Producto_LOG"(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public."Producto_LOG"() TO "user";


--
-- TOC entry 3099 (class 0 OID 0)
-- Dependencies: 236
-- Name: FUNCTION "Suplidor_HISTORICO"(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public."Suplidor_HISTORICO"() TO "user";


--
-- TOC entry 3100 (class 0 OID 0)
-- Dependencies: 237
-- Name: FUNCTION "Suplidor_LOG"(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public."Suplidor_LOG"() TO "user";


--
-- TOC entry 3101 (class 0 OID 0)
-- Dependencies: 223
-- Name: FUNCTION orden_mostrar(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.orden_mostrar() TO "user";


--
-- TOC entry 3102 (class 0 OID 0)
-- Dependencies: 224
-- Name: FUNCTION ordendetalle_insertar(id integer, productoid integer, precio numeric, cantidad integer, descuento numeric, impuesto numeric); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.ordendetalle_insertar(id integer, productoid integer, precio numeric, cantidad integer, descuento numeric, impuesto numeric) TO "user";


--
-- TOC entry 3103 (class 0 OID 0)
-- Dependencies: 225
-- Name: FUNCTION ordendetalle_mostrar(numero integer); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.ordendetalle_mostrar(numero integer) TO "user";


--
-- TOC entry 3104 (class 0 OID 0)
-- Dependencies: 226
-- Name: FUNCTION producto_buscar(nom character varying); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.producto_buscar(nom character varying) TO "user";


--
-- TOC entry 3105 (class 0 OID 0)
-- Dependencies: 227
-- Name: FUNCTION producto_mostrar(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.producto_mostrar() TO "user";


--
-- TOC entry 3106 (class 0 OID 0)
-- Dependencies: 228
-- Name: FUNCTION producto_mostraractivos(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.producto_mostraractivos() TO "user";


--
-- TOC entry 3107 (class 0 OID 0)
-- Dependencies: 229
-- Name: FUNCTION suplidor_buscar(nom character varying); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.suplidor_buscar(nom character varying) TO "user";


--
-- TOC entry 3108 (class 0 OID 0)
-- Dependencies: 230
-- Name: FUNCTION suplidor_buscaractivos(nom character varying); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.suplidor_buscaractivos(nom character varying) TO "user";


--
-- TOC entry 3109 (class 0 OID 0)
-- Dependencies: 231
-- Name: FUNCTION suplidor_mostrar(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.suplidor_mostrar() TO "user";


--
-- TOC entry 3110 (class 0 OID 0)
-- Dependencies: 232
-- Name: FUNCTION suplidor_mostraractivos(); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.suplidor_mostraractivos() TO "user";


--
-- TOC entry 3111 (class 0 OID 0)
-- Dependencies: 208
-- Name: SEQUENCE orden_ordenid_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.orden_ordenid_seq TO "user";


--
-- TOC entry 3112 (class 0 OID 0)
-- Dependencies: 209
-- Name: TABLE orden; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,REFERENCES,TRIGGER,UPDATE ON TABLE public.orden TO "user";


--
-- TOC entry 3113 (class 0 OID 0)
-- Dependencies: 210
-- Name: SEQUENCE ordendetalle_ordendetallelinea_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.ordendetalle_ordendetallelinea_seq TO "user";


--
-- TOC entry 3114 (class 0 OID 0)
-- Dependencies: 211
-- Name: TABLE ordendetalle; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,REFERENCES,TRIGGER,UPDATE ON TABLE public.ordendetalle TO "user";


--
-- TOC entry 3115 (class 0 OID 0)
-- Dependencies: 204
-- Name: SEQUENCE producto_productoid_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.producto_productoid_seq TO "user";


--
-- TOC entry 3116 (class 0 OID 0)
-- Dependencies: 205
-- Name: TABLE producto; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,REFERENCES,TRIGGER,UPDATE ON TABLE public.producto TO "user";


--
-- TOC entry 3117 (class 0 OID 0)
-- Dependencies: 207
-- Name: TABLE productohis; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,REFERENCES,TRIGGER,UPDATE ON TABLE public.productohis TO "user";


--
-- TOC entry 3118 (class 0 OID 0)
-- Dependencies: 206
-- Name: TABLE productolog; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,REFERENCES,TRIGGER,UPDATE ON TABLE public.productolog TO "user";


--
-- TOC entry 3119 (class 0 OID 0)
-- Dependencies: 200
-- Name: SEQUENCE suplidor_suplidorid_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.suplidor_suplidorid_seq TO "user";


--
-- TOC entry 3120 (class 0 OID 0)
-- Dependencies: 201
-- Name: TABLE suplidor; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,REFERENCES,TRIGGER,UPDATE ON TABLE public.suplidor TO "user";


--
-- TOC entry 3121 (class 0 OID 0)
-- Dependencies: 202
-- Name: TABLE suplidorhis; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,REFERENCES,TRIGGER,UPDATE ON TABLE public.suplidorhis TO "user";


--
-- TOC entry 3122 (class 0 OID 0)
-- Dependencies: 203
-- Name: TABLE suplidorlog; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,REFERENCES,TRIGGER,UPDATE ON TABLE public.suplidorlog TO "user";


--
-- TOC entry 1764 (class 826 OID 16803)
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: -; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres GRANT ALL ON SEQUENCES  TO "user";


--
-- TOC entry 1766 (class 826 OID 16805)
-- Name: DEFAULT PRIVILEGES FOR TYPES; Type: DEFAULT ACL; Schema: -; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres GRANT ALL ON TYPES  TO "user";


--
-- TOC entry 1765 (class 826 OID 16804)
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: -; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres GRANT ALL ON FUNCTIONS  TO "user";


--
-- TOC entry 1763 (class 826 OID 16802)
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: -; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres GRANT SELECT,INSERT,REFERENCES,TRIGGER,UPDATE ON TABLES  TO "user";


-- Completed on 2020-11-22 11:39:07

--
-- PostgreSQL database dump complete
--