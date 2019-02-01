/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DP;

import MD.ProveedorJpaController;
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
@Table(name = "PROVEEDORES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proveedor.findAll", query = "SELECT p FROM Proveedor p")
    , @NamedQuery(name = "Proveedor.findByPvdruc", query = "SELECT p FROM Proveedor p WHERE p.pvdruc = :pvdruc")
    , @NamedQuery(name = "Proveedor.findByPvdnombre", query = "SELECT p FROM Proveedor p WHERE p.pvdnombre = :pvdnombre")
    , @NamedQuery(name = "Proveedor.findByPvddireccion", query = "SELECT p FROM Proveedor p WHERE p.pvddireccion = :pvddireccion")
    , @NamedQuery(name = "Proveedor.findByPvdtelefono", query = "SELECT p FROM Proveedor p WHERE p.pvdtelefono = :pvdtelefono")
    , @NamedQuery(name = "Proveedor.findByPvdemail", query = "SELECT p FROM Proveedor p WHERE p.pvdemail = :pvdemail")
    , @NamedQuery(name = "Proveedor.findByPvdestado", query = "SELECT p FROM Proveedor p WHERE p.pvdestado = :pvdestado")
    , @NamedQuery(name = "Proveedor.findByPvdfechaingreso", query = "SELECT p FROM Proveedor p WHERE p.pvdfechaingreso = :pvdfechaingreso")})
public class Proveedor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "PVDRUC")
    private String pvdruc;
    @Size(max = 30)
    @Column(name = "PVDNOMBRE")
    private String pvdnombre;
    @Size(max = 100)
    @Column(name = "PVDDIRECCION")
    private String pvddireccion;
    @Size(max = 10)
    @Column(name = "PVDTELEFONO")
    private String pvdtelefono;
    @Size(max = 50)
    @Column(name = "PVDEMAIL")
    private String pvdemail;
    @Column(name = "PVDESTADO")
    private Short pvdestado;
    @Column(name = "PVDFECHAINGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pvdfechaingreso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pvdruc")
    private List<Existencia> existenciaList;
    
    @Transient
    private ProveedorJpaController pmd;

    public Proveedor() {
    }

    public Proveedor(String pvdruc) {
        this.pvdruc = pvdruc;
    }

    public String getPvdruc() {
        return pvdruc;
    }

    public void setPvdruc(String pvdruc) {
        this.pvdruc = pvdruc;
    }

    public String getPvdnombre() {
        return pvdnombre;
    }

    public void setPvdnombre(String pvdnombre) {
        this.pvdnombre = pvdnombre;
    }

    public String getPvddireccion() {
        return pvddireccion;
    }

    public void setPvddireccion(String pvddireccion) {
        this.pvddireccion = pvddireccion;
    }

    public String getPvdtelefono() {
        return pvdtelefono;
    }

    public void setPvdtelefono(String pvdtelefono) {
        this.pvdtelefono = pvdtelefono;
    }

    public String getPvdemail() {
        return pvdemail;
    }

    public void setPvdemail(String pvdemail) {
        this.pvdemail = pvdemail;
    }

    public Short getPvdestado() {
        return pvdestado;
    }

    public void setPvdestado(Short pvdestado) {
        this.pvdestado = pvdestado;
    }

    public Date getPvdfechaingreso() {
        return pvdfechaingreso;
    }

    public void setPvdfechaingreso(Date pvdfechaingreso) {
        this.pvdfechaingreso = pvdfechaingreso;
    }

    @XmlTransient
    public List<Existencia> getExistenciaList() {
        return existenciaList;
    }

    public void setExistenciaList(List<Existencia> existenciaList) {
        this.existenciaList = existenciaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pvdruc != null ? pvdruc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        if ((this.pvdruc == null && other.pvdruc != null) || (this.pvdruc != null && !this.pvdruc.equals(other.pvdruc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DP.Proveedor[ pvdruc=" + pvdruc + " ]";
    }

    public List<Proveedor> obtenerProveedores() {
        pmd = new ProveedorJpaController();
        return pmd.findProveedorEntities();
    }
    
}
