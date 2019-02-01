package DP;

import DP.Cliente;
import DP.DetalleVenta;
import DP.Entrega;
import DP.Reclamo;
import DP.Tienda;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-15T19:16:21")
@StaticMetamodel(Venta.class)
public class Venta_ { 

    public static volatile ListAttribute<Venta, Reclamo> reclamoList;
    public static volatile SingularAttribute<Venta, String> vencodigo;
    public static volatile ListAttribute<Venta, Entrega> entregaList;
    public static volatile ListAttribute<Venta, DetalleVenta> detalleVentaList;
    public static volatile SingularAttribute<Venta, Tienda> tiecodigo;
    public static volatile SingularAttribute<Venta, Cliente> clicedula;
    public static volatile SingularAttribute<Venta, Date> venfechaingreso;

}