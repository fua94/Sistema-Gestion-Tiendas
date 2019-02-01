/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DP;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name = "EMPLEADOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e")
    , @NamedQuery(name = "Empleado.findByEmpcodigo", query = "SELECT e FROM Empleado e WHERE e.empcodigo = :empcodigo")
    , @NamedQuery(name = "Empleado.findByEmpcedula", query = "SELECT e FROM Empleado e WHERE e.empcedula = :empcedula")
    , @NamedQuery(name = "Empleado.findByEmpnombre", query = "SELECT e FROM Empleado e WHERE e.empnombre = :empnombre")
    , @NamedQuery(name = "Empleado.findByEmpfechanacimiento", query = "SELECT e FROM Empleado e WHERE e.empfechanacimiento = :empfechanacimiento")
    , @NamedQuery(name = "Empleado.findByEmptelefono", query = "SELECT e FROM Empleado e WHERE e.emptelefono = :emptelefono")
    , @NamedQuery(name = "Empleado.findByEmpcargo", query = "SELECT e FROM Empleado e WHERE e.empcargo = :empcargo")
    , @NamedQuery(name = "Empleado.findByEmpestado", query = "SELECT e FROM Empleado e WHERE e.empestado = :empestado")
    , @NamedQuery(name = "Empleado.findByEmpfechaingreso", query = "SELECT e FROM Empleado e WHERE e.empfechaingreso = :empfechaingreso")})
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "EMPCODIGO")
    private String empcodigo;
    @Column(name = "EMPCEDULA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date empcedula;
    @Size(max = 50)
    @Column(name = "EMPNOMBRE")
    private String empnombre;
    @Size(max = 50)
    @Column(name = "EMPFECHANACIMIENTO")
    private String empfechanacimiento;
    @Size(max = 10)
    @Column(name = "EMPTELEFONO")
    private String emptelefono;
    @Size(max = 1)
    @Column(name = "EMPCARGO")
    private String empcargo;
    @Column(name = "EMPESTADO")
    private Short empestado;
    @Column(name = "EMPFECHAINGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date empfechaingreso;
    @JoinColumn(name = "TIECODIGO", referencedColumnName = "TIECODIGO")
    @ManyToOne(optional = false)
    private Tienda tiecodigo;

    public Empleado() {
    }

    public Empleado(String empcodigo) {
        this.empcodigo = empcodigo;
    }

    public String getEmpcodigo() {
        return empcodigo;
    }

    public void setEmpcodigo(String empcodigo) {
        this.empcodigo = empcodigo;
    }

    public Date getEmpcedula() {
        return empcedula;
    }

    public void setEmpcedula(Date empcedula) {
        this.empcedula = empcedula;
    }

    public String getEmpnombre() {
        return empnombre;
    }

    public void setEmpnombre(String empnombre) {
        this.empnombre = empnombre;
    }

    public String getEmpfechanacimiento() {
        return empfechanacimiento;
    }

    public void setEmpfechanacimiento(String empfechanacimiento) {
        this.empfechanacimiento = empfechanacimiento;
    }

    public String getEmptelefono() {
        return emptelefono;
    }

    public void setEmptelefono(String emptelefono) {
        this.emptelefono = emptelefono;
    }

    public String getEmpcargo() {
        return empcargo;
    }

    public void setEmpcargo(String empcargo) {
        this.empcargo = empcargo;
    }

    public Short getEmpestado() {
        return empestado;
    }

    public void setEmpestado(Short empestado) {
        this.empestado = empestado;
    }

    public Date getEmpfechaingreso() {
        return empfechaingreso;
    }

    public void setEmpfechaingreso(Date empfechaingreso) {
        this.empfechaingreso = empfechaingreso;
    }

    public Tienda getTiecodigo() {
        return tiecodigo;
    }

    public void setTiecodigo(Tienda tiecodigo) {
        this.tiecodigo = tiecodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empcodigo != null ? empcodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.empcodigo == null && other.empcodigo != null) || (this.empcodigo != null && !this.empcodigo.equals(other.empcodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DP.Empleado[ empcodigo=" + empcodigo + " ]";
    }
    
}
