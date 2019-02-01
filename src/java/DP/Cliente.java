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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "CLIENTES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c")
    , @NamedQuery(name = "Cliente.findByClicedula", query = "SELECT c FROM Cliente c WHERE c.clicedula = :clicedula")
    , @NamedQuery(name = "Cliente.findByClinombre", query = "SELECT c FROM Cliente c WHERE c.clinombre = :clinombre")
    , @NamedQuery(name = "Cliente.findByClidireccion", query = "SELECT c FROM Cliente c WHERE c.clidireccion = :clidireccion")
    , @NamedQuery(name = "Cliente.findByClitelefono", query = "SELECT c FROM Cliente c WHERE c.clitelefono = :clitelefono")
    , @NamedQuery(name = "Cliente.findByCliemail", query = "SELECT c FROM Cliente c WHERE c.cliemail = :cliemail")
    , @NamedQuery(name = "Cliente.findByCliestado", query = "SELECT c FROM Cliente c WHERE c.cliestado = :cliestado")
    , @NamedQuery(name = "Cliente.findByClifechaingreso", query = "SELECT c FROM Cliente c WHERE c.clifechaingreso = :clifechaingreso")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "CLICEDULA")
    private String clicedula;
    @Size(max = 50)
    @Column(name = "CLINOMBRE")
    private String clinombre;
    @Size(max = 100)
    @Column(name = "CLIDIRECCION")
    private String clidireccion;
    @Size(max = 10)
    @Column(name = "CLITELEFONO")
    private String clitelefono;
    @Size(max = 30)
    @Column(name = "CLIEMAIL")
    private String cliemail;
    @Column(name = "CLIESTADO")
    private Short cliestado;
    @Column(name = "CLIFECHAINGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date clifechaingreso;
    @JoinColumn(name = "TIECODIGO", referencedColumnName = "TIECODIGO")
    @ManyToOne(optional = false)
    private Tienda tiecodigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clicedula")
    private List<Venta> ventaList;

    public Cliente() {
    }

    public Cliente(String clicedula) {
        this.clicedula = clicedula;
    }

    public String getClicedula() {
        return clicedula;
    }

    public void setClicedula(String clicedula) {
        this.clicedula = clicedula;
    }

    public String getClinombre() {
        return clinombre;
    }

    public void setClinombre(String clinombre) {
        this.clinombre = clinombre;
    }

    public String getClidireccion() {
        return clidireccion;
    }

    public void setClidireccion(String clidireccion) {
        this.clidireccion = clidireccion;
    }

    public String getClitelefono() {
        return clitelefono;
    }

    public void setClitelefono(String clitelefono) {
        this.clitelefono = clitelefono;
    }

    public String getCliemail() {
        return cliemail;
    }

    public void setCliemail(String cliemail) {
        this.cliemail = cliemail;
    }

    public Short getCliestado() {
        return cliestado;
    }

    public void setCliestado(Short cliestado) {
        this.cliestado = cliestado;
    }

    public Date getClifechaingreso() {
        return clifechaingreso;
    }

    public void setClifechaingreso(Date clifechaingreso) {
        this.clifechaingreso = clifechaingreso;
    }

    public Tienda getTiecodigo() {
        return tiecodigo;
    }

    public void setTiecodigo(Tienda tiecodigo) {
        this.tiecodigo = tiecodigo;
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
        hash += (clicedula != null ? clicedula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.clicedula == null && other.clicedula != null) || (this.clicedula != null && !this.clicedula.equals(other.clicedula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DP.Cliente[ clicedula=" + clicedula + " ]";
    }
    
}
