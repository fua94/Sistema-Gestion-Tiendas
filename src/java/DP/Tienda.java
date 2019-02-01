/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name = "TIENDAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tienda.findAll", query = "SELECT t FROM Tienda t")
    , @NamedQuery(name = "Tienda.findByTiecodigo", query = "SELECT t FROM Tienda t WHERE t.tiecodigo = :tiecodigo")
    , @NamedQuery(name = "Tienda.findByTieciudad", query = "SELECT t FROM Tienda t WHERE t.tieciudad = :tieciudad")
    , @NamedQuery(name = "Tienda.findByTiedireccion", query = "SELECT t FROM Tienda t WHERE t.tiedireccion = :tiedireccion")
    , @NamedQuery(name = "Tienda.findByTietelefono", query = "SELECT t FROM Tienda t WHERE t.tietelefono = :tietelefono")
    , @NamedQuery(name = "Tienda.findByTieestado", query = "SELECT t FROM Tienda t WHERE t.tieestado = :tieestado")
    , @NamedQuery(name = "Tienda.findByTiefechaingreso", query = "SELECT t FROM Tienda t WHERE t.tiefechaingreso = :tiefechaingreso")})
public class Tienda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "TIECODIGO")
    private String tiecodigo;
    @Size(max = 30)
    @Column(name = "TIECIUDAD")
    private String tieciudad;
    @Size(max = 100)
    @Column(name = "TIEDIRECCION")
    private String tiedireccion;
    @Size(max = 10)
    @Column(name = "TIETELEFONO")
    private String tietelefono;
    @Column(name = "TIEESTADO")
    private Short tieestado;
    @Column(name = "TIEFECHAINGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tiefechaingreso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiecodigo")
    private List<Cliente> clienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiecodigo")
    private List<Empleado> empleadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiecodigo")
    private List<Producto> productoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiecodigo")
    private List<Venta> ventaList;

    public Tienda() {
    }

    public Tienda(String tiecodigo) {
        this.tiecodigo = tiecodigo;
    }

    public String getTiecodigo() {
        return tiecodigo;
    }

    public void setTiecodigo(String tiecodigo) {
        this.tiecodigo = tiecodigo;
    }

    public String getTieciudad() {
        return tieciudad;
    }

    public void setTieciudad(String tieciudad) {
        this.tieciudad = tieciudad;
    }

    public String getTiedireccion() {
        return tiedireccion;
    }

    public void setTiedireccion(String tiedireccion) {
        this.tiedireccion = tiedireccion;
    }

    public String getTietelefono() {
        return tietelefono;
    }

    public void setTietelefono(String tietelefono) {
        this.tietelefono = tietelefono;
    }

    public Short getTieestado() {
        return tieestado;
    }

    public void setTieestado(Short tieestado) {
        this.tieestado = tieestado;
    }

    public Date getTiefechaingreso() {
        return tiefechaingreso;
    }

    public void setTiefechaingreso(Date tiefechaingreso) {
        this.tiefechaingreso = tiefechaingreso;
    }

    @XmlTransient
    public List<Cliente> getClienteList() {
        return clienteList;
    }

    public void setClienteList(List<Cliente> clienteList) {
        this.clienteList = clienteList;
    }

    @XmlTransient
    public List<Empleado> getEmpleadoList() {
        return empleadoList;
    }

    public void setEmpleadoList(List<Empleado> empleadoList) {
        this.empleadoList = empleadoList;
    }

    @XmlTransient
    public List<Producto> getProductoList() {
        return productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }

    @XmlTransient
    public List<Venta> getVentaList() {
        return ventaList;
    }

    public void setVentaList(List<Venta> ventaList) {
        this.ventaList = ventaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tiecodigo != null ? tiecodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tienda)) {
            return false;
        }
        Tienda other = (Tienda) object;
        if ((this.tiecodigo == null && other.tiecodigo != null) || (this.tiecodigo != null && !this.tiecodigo.equals(other.tiecodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DP.Tienda[ tiecodigo=" + tiecodigo + " ]";
    }
    
}
