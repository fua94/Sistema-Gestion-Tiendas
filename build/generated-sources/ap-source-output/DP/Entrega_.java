package DP;

import DP.Venta;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-15T19:16:21")
@StaticMetamodel(Entrega.class)
public class Entrega_ { 

    public static volatile SingularAttribute<Entrega, Date> entfechaentrega;
    public static volatile SingularAttribute<Entrega, Venta> vencodigo;
    public static volatile SingularAttribute<Entrega, String> entdestinatario;
    public static volatile SingularAttribute<Entrega, String> entestado;
    public static volatile SingularAttribute<Entrega, String> entcodigo;
    public static volatile SingularAttribute<Entrega, Date> entfechadespacho;
    public static volatile SingularAttribute<Entrega, String> entdireccion;

}