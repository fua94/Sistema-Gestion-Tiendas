package DP;

import DP.Producto;
import DP.Proveedor;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-15T19:16:21")
@StaticMetamodel(Existencia.class)
public class Existencia_ { 

    public static volatile SingularAttribute<Existencia, Producto> procodigo;
    public static volatile SingularAttribute<Existencia, Integer> exicodigo;
    public static volatile SingularAttribute<Existencia, Short> exicantidad;
    public static volatile SingularAttribute<Existencia, BigDecimal> exicosto;
    public static volatile SingularAttribute<Existencia, Date> exifechaingreso;
    public static volatile SingularAttribute<Existencia, Proveedor> pvdruc;

}