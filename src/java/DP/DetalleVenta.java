/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DP;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Francisco
 */
@Named(value = "detalleventa")
@RequestScoped
@Entity
@Table(name = "DETALLEVENTAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleVenta.findAll", query = "SELECT d FROM DetalleVenta d")
    , @NamedQuery(name = "DetalleVenta.findByDetcodigo", query = "SELECT d FROM DetalleVenta d WHERE d.detcodigo = :detcodigo")
    , @NamedQuery(name = "DetalleVenta.findByDetcantidad", query = "SELECT d FROM DetalleVenta d WHERE d.detcantidad = :detcantidad")})
public class DetalleVenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "DETCODIGO")
    private Short detcodigo;
    @Column(name = "DETCANTIDAD")
    private Short detcantidad;
    @JoinColumn(name = "PROCODIGO", referencedColumnName = "PROCODIGO")
    @ManyToOne(optional = false)
    private Producto procodigo;
    @JoinColumn(name = "VENCODIGO", referencedColumnName = "VENCODIGO")
    @ManyToOne(optional = false)
    private Venta vencodigo;

    public DetalleVenta() {
    }

    public DetalleVenta(Short detcodigo) {
        this.detcodigo = detcodigo;
    }

    public Short getDetcodigo() {
        return detcodigo;
    }

    public void setDetcodigo(Short detcodigo) {
        this.detcodigo = detcodigo;
    }

    public Short getDetcantidad() {
        return detcantidad;
    }

    public void setDetcantidad(Short detcantidad) {
        this.detcantidad = detcantidad;
    }

    public Producto getProcodigo() {
        return procodigo;
    }

    public void setProcodigo(Producto procodigo) {
        this.procodigo = procodigo;
    }

    public Venta getVencodigo() {
        return vencodigo;
    }

    public void setVencodigo(Venta vencodigo) {
        this.vencodigo = vencodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detcodigo != null ? detcodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleVenta)) {
            return false;
        }
        DetalleVenta other = (DetalleVenta) object;
        if ((this.detcodigo == null && other.detcodigo != null) || (this.detcodigo != null && !this.detcodigo.equals(other.detcodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DP.DetalleVenta[ detcodigo=" + detcodigo + " ]";
    }
    
}
