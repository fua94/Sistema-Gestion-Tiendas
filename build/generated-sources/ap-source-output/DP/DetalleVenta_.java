package DP;

import DP.Producto;
import DP.Venta;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-15T19:16:21")
@StaticMetamodel(DetalleVenta.class)
public class DetalleVenta_ { 

    public static volatile SingularAttribute<DetalleVenta, Venta> vencodigo;
    public static volatile SingularAttribute<DetalleVenta, Short> detcodigo;
    public static volatile SingularAttribute<DetalleVenta, Producto> procodigo;
    public static volatile SingularAttribute<DetalleVenta, Short> detcantidad;

}