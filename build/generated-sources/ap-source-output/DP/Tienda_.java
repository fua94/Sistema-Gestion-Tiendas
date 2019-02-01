package DP;

import DP.Cliente;
import DP.Empleado;
import DP.Producto;
import DP.Venta;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-15T19:16:21")
@StaticMetamodel(Tienda.class)
public class Tienda_ { 

    public static volatile ListAttribute<Tienda, Cliente> clienteList;
    public static volatile SingularAttribute<Tienda, Short> tieestado;
    public static volatile SingularAttribute<Tienda, Date> tiefechaingreso;
    public static volatile ListAttribute<Tienda, Producto> productoList;
    public static volatile ListAttribute<Tienda, Venta> ventaList;
    public static volatile SingularAttribute<Tienda, String> tiecodigo;
    public static volatile ListAttribute<Tienda, Empleado> empleadoList;
    public static volatile SingularAttribute<Tienda, String> tieciudad;
    public static volatile SingularAttribute<Tienda, String> tietelefono;
    public static volatile SingularAttribute<Tienda, String> tiedireccion;

}