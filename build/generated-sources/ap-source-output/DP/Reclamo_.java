package DP;

import DP.Venta;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-15T19:16:21")
@StaticMetamodel(Reclamo.class)
public class Reclamo_ { 

    public static volatile SingularAttribute<Reclamo, Date> recfechafin;
    public static volatile SingularAttribute<Reclamo, Venta> vencodigo;
    public static volatile SingularAttribute<Reclamo, Date> recfechaingreso;
    public static volatile SingularAttribute<Reclamo, String> recdescripcion;
    public static volatile SingularAttribute<Reclamo, String> recestado;
    public static volatile SingularAttribute<Reclamo, String> reccodigo;

}