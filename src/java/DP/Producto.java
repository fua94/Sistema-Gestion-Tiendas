/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DP;

import MD.ProductoJpaController;
import MD.exceptions.NonexistentEntityException;
import MD.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name = "PRODUCTOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p")
    , @NamedQuery(name = "Producto.findByProcodigo", query = "SELECT p FROM Producto p WHERE p.procodigo = :procodigo")
    , @NamedQuery(name = "Producto.findByProdescripcion", query = "SELECT p FROM Producto p WHERE p.prodescripcion = :prodescripcion")
    , @NamedQuery(name = "Producto.findByProcantidad", query = "SELECT p FROM Producto p WHERE p.procantidad = :procantidad")
    , @NamedQuery(name = "Producto.findByProprecio", query = "SELECT p FROM Producto p WHERE p.proprecio = :proprecio")
    , @NamedQuery(name = "Producto.findByProestado", query = "SELECT p FROM Producto p WHERE p.proestado = :proestado")
    , @NamedQuery(name = "Producto.findByProfechaingreso", query = "SELECT p FROM Producto p WHERE p.profechaingreso = :profechaingreso")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "PROCODIGO")
    private String procodigo;
    @Size(max = 30)
    @Column(name = "PRODESCRIPCION")
    private String prodescripcion;
    @Column(name = "PROCANTIDAD")
    private Short procantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PROPRECIO")
    private BigDecimal proprecio;
    @Column(name = "PROESTADO")
    private Short proestado;
    @Column(name = "PROFECHAINGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date profechaingreso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procodigo")
    private List<Existencia> existenciaList;
    @JoinColumn(name = "TIECODIGO", referencedColumnName = "TIECODIGO")
    @ManyToOne(optional = false)
    private Tienda tiecodigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procodigo")
    private List<DetalleVenta> detalleVentaList;

    @Transient    
    private ProductoJpaController pmd;
    
    public Producto() {
    }

    public Producto(String procodigo) {
        this.procodigo = procodigo;
    }

    public String getProcodigo() {
        return procodigo;
    }

    public void setProcodigo(String procodigo) {
        this.procodigo = procodigo;
    }

    public String getProdescripcion() {
        return prodescripcion;
    }

    public void setProdescripcion(String prodescripcion) {
        this.prodescripcion = prodescripcion;
    }

    public Short getProcantidad() {
        return procantidad;
    }

    public void setProcantidad(Short procantidad) {
        this.procantidad = procantidad;
    }

    public BigDecimal getProprecio() {
        return proprecio;
    }

    public void setProprecio(BigDecimal proprecio) {
        this.proprecio = proprecio;
    }

    public Short getProestado() {
        return proestado;
    }

    public void setProestado(Short proestado) {
        this.proestado = proestado;
    }

    public Date getProfechaingreso() {
        return profechaingreso;
    }

    public void setProfechaingreso(Date profechaingreso) {
        this.profechaingreso = profechaingreso;
    }

    @XmlTransient
    public List<Existencia> getExistenciaList() {
        return existenciaList;
    }

    public void setExistenciaList(List<Existencia> existenciaList) {
        this.existenciaList = existenciaList;
    }

    public Tienda getTiecodigo() {
        return tiecodigo;
    }

    public void setTiecodigo(Tienda tiecodigo) {
        this.tiecodigo = tiecodigo;
    }

    @XmlTransient
    public List<DetalleVenta> getDetalleVentaList() {
        return detalleVentaList;
    }

    public void setDetalleVentaList(List<DetalleVenta> detalleVentaList) {
        this.detalleVentaList = detalleVentaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (procodigo != null ? procodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.procodigo == null && other.procodigo != null) || (this.procodigo != null && !this.procodigo.equals(other.procodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Producto{" + "procodigo=" + procodigo + ", prodescripcion=" + prodescripcion + ", procantidad=" + procantidad + ", proprecio=" + proprecio + ", proestado=" + proestado + ", profechaingreso=" + profechaingreso + ", tiecodigo=" + tiecodigo + '}';
    }

    public List<Producto> obtenerProductos() {
        pmd = new ProductoJpaController();
        return pmd.findProductoEntities();
    }
    
    public void incrementarCantidad(short cant){
        try {
            ProductoJpaController pmd = new ProductoJpaController();
            short cant_ini = this.procantidad;
            short cant_fin = (short) (cant_ini + cant);
            this.procantidad = cant_fin;
            pmd.edit(this);
        } catch (NonexistentEntityException ex) {
            System.out.println("error"+ex.toString());
        } catch (RollbackFailureException ex) {
            System.out.println("error"+ex.toString());
        } catch (Exception ex) {
            System.out.println("error"+ex.toString());
        }
    }
    
}
