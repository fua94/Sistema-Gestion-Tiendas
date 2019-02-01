package DP;

import DP.DetalleVenta;
import DP.Existencia;
import DP.Tienda;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-15T19:16:21")
@StaticMetamodel(Producto.class)
public class Producto_ { 

    public static volatile SingularAttribute<Producto, String> procodigo;
    public static volatile ListAttribute<Producto, Existencia> existenciaList;
    public static volatile SingularAttribute<Producto, String> prodescripcion;
    public static volatile ListAttribute<Producto, DetalleVenta> detalleVentaList;
    public static volatile SingularAttribute<Producto, Short> procantidad;
    public static volatile SingularAttribute<Producto, Tienda> tiecodigo;
    public static volatile SingularAttribute<Producto, BigDecimal> proprecio;
    public static volatile SingularAttribute<Producto, Short> proestado;
    public static volatile SingularAttribute<Producto, Date> profechaingreso;

}