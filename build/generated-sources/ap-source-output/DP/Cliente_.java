package DP;

import DP.Tienda;
import DP.Venta;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-15T19:16:21")
@StaticMetamodel(Cliente.class)
public class Cliente_ { 

    public static volatile SingularAttribute<Cliente, String> clinombre;
    public static volatile SingularAttribute<Cliente, Date> clifechaingreso;
    public static volatile SingularAttribute<Cliente, String> clitelefono;
    public static volatile SingularAttribute<Cliente, String> clidireccion;
    public static volatile ListAttribute<Cliente, Venta> ventaList;
    public static volatile SingularAttribute<Cliente, Tienda> tiecodigo;
    public static volatile SingularAttribute<Cliente, String> cliemail;
    public static volatile SingularAttribute<Cliente, String> clicedula;
    public static volatile SingularAttribute<Cliente, Short> cliestado;

}