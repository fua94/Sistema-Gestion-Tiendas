package DP;

import DP.Tienda;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-15T19:16:21")
@StaticMetamodel(Empleado.class)
public class Empleado_ { 

    public static volatile SingularAttribute<Empleado, Short> empestado;
    public static volatile SingularAttribute<Empleado, Date> empfechaingreso;
    public static volatile SingularAttribute<Empleado, String> emptelefono;
    public static volatile SingularAttribute<Empleado, String> empcodigo;
    public static volatile SingularAttribute<Empleado, Tienda> tiecodigo;
    public static volatile SingularAttribute<Empleado, Date> empcedula;
    public static volatile SingularAttribute<Empleado, String> empcargo;
    public static volatile SingularAttribute<Empleado, String> empfechanacimiento;
    public static volatile SingularAttribute<Empleado, String> empnombre;

}