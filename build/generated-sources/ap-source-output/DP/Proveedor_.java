package DP;

import DP.Existencia;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-15T19:16:21")
@StaticMetamodel(Proveedor.class)
public class Proveedor_ { 

    public static volatile ListAttribute<Proveedor, Existencia> existenciaList;
    public static volatile SingularAttribute<Proveedor, Date> pvdfechaingreso;
    public static volatile SingularAttribute<Proveedor, String> pvdemail;
    public static volatile SingularAttribute<Proveedor, String> pvdnombre;
    public static volatile SingularAttribute<Proveedor, String> pvddireccion;
    public static volatile SingularAttribute<Proveedor, Short> pvdestado;
    public static volatile SingularAttribute<Proveedor, String> pvdruc;
    public static volatile SingularAttribute<Proveedor, String> pvdtelefono;

}