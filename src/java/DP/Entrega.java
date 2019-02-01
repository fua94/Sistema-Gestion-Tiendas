/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DP;

import MD.EntregaJpaController;
import MD.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Francisco
 */
//@Named(value = "entrega")
//@RequestScoped
@ManagedBean(name = "entrega")
@ViewScoped
@Entity
@Table(name = "ENTREGAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entrega.findAll", query = "SELECT e FROM Entrega e")
    , @NamedQuery(name = "Entrega.findByEntcodigo", query = "SELECT e FROM Entrega e WHERE e.entcodigo = :entcodigo")
    , @NamedQuery(name = "Entrega.findByEntdestinatario", query = "SELECT e FROM Entrega e WHERE e.entdestinatario = :entdestinatario")
    , @NamedQuery(name = "Entrega.findByEntdireccion", query = "SELECT e FROM Entrega e WHERE e.entdireccion = :entdireccion")
    , @NamedQuery(name = "Entrega.findByEntfechaentrega", query = "SELECT e FROM Entrega e WHERE e.entfechaentrega = :entfechaentrega")
    , @NamedQuery(name = "Entrega.findByEntestado", query = "SELECT e FROM Entrega e WHERE e.entestado = :entestado")
    , @NamedQuery(name = "Entrega.findByEntfechadespacho", query = "SELECT e FROM Entrega e WHERE e.entfechadespacho = :entfechadespacho")})
public class Entrega implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "ENTCODIGO")
    private String entcodigo;
    @Size(max = 50)
    @Column(name = "ENTDESTINATARIO")
    private String entdestinatario;
    @Size(max = 100)
    @Column(name = "ENTDIRECCION")
    private String entdireccion;
    @Column(name = "ENTFECHAENTREGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date entfechaentrega;
    @Size(max = 1)
    @Column(name = "ENTESTADO")
    private String entestado;
    @Column(name = "ENTFECHADESPACHO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date entfechadespacho;
    @JoinColumn(name = "VENCODIGO", referencedColumnName = "VENCODIGO")
    @ManyToOne(optional = false)
    private Venta vencodigo;
    
    @Transient
    private EntregaJpaController emd;
    
    @Transient
    private List<Entrega> listaentregas;

    public Entrega() {
        this.entestado="P";
        this.listaentregas = new ArrayList<Entrega>();
        this.entfechadespacho = new Date();
        this.vencodigo = new Venta();
    }
    
    @PostConstruct
    public void init() {
        inicializar();
    }

    public Entrega(String entcodigo) {
        this.entcodigo = entcodigo;
    }

    public String getEntcodigo() {
        return entcodigo;
    }

    public void setEntcodigo(String entcodigo) {
        this.entcodigo = entcodigo;
    }

    public String getEntdestinatario() {
        return entdestinatario;
    }

    public void setEntdestinatario(String entdestinatario) {
        this.entdestinatario = entdestinatario;
    }

    public String getEntdireccion() {
        return entdireccion;
    }

    public void setEntdireccion(String entdireccion) {
        this.entdireccion = entdireccion;
    }

    public Date getEntfechaentrega() {
        return entfechaentrega;
    }

    public void setEntfechaentrega(Date entfechaentrega) {
        this.entfechaentrega = entfechaentrega;
    }

    public String getEntestado() {
        return entestado;
    }

    public void setEntestado(String entestado) {
        this.entestado = entestado;
    }

    public Date getEntfechadespacho() {
        return entfechadespacho;
    }

    public void setEntfechadespacho(Date entfechadespacho) {
        this.entfechadespacho = entfechadespacho;
    }

    public Venta getVencodigo() {
        return vencodigo;
    }

    public void setVencodigo(Venta vencodigo) {
        this.vencodigo = vencodigo;
    }

    public List<Entrega> getListaentregas() {
        return listaentregas;
    }

    public void setListaentregas(List<Entrega> listaentregas) {
        this.listaentregas = listaentregas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (entcodigo != null ? entcodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entrega)) {
            return false;
        }
        Entrega other = (Entrega) object;
        if ((this.entcodigo == null && other.entcodigo != null) || (this.entcodigo != null && !this.entcodigo.equals(other.entcodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DP.Entrega[ entcodigo=" + entcodigo + " ]";
    }
    
    public boolean buscarVenta(){
        boolean ret = false;

        List<Venta> ventas = this.vencodigo.obtenerVentas();
        
        for(Venta v : ventas){
            if(v.getVencodigo().equals(this.vencodigo.getVencodigo()))
                ret = true;
        }
        System.out.println("cod: "+this.vencodigo.getVencodigo());
        return ret;
    }
    
    public void verificarVenta(){
        String msg;
        
        if(!buscarVenta())
            msg = "No se encontro la factura";
        else
            msg = "Se encontro la factura";
         
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", msg));
    }
    
    public void ingresar(){
        this.buscarVenta();
        String cod = this.vencodigo.getVencodigo();
        this.entcodigo = "E-"+cod.substring(2);
        try {
            emd = new EntregaJpaController();
            emd.create(this);
        } catch (RollbackFailureException ex) {
            System.out.println("error"+ex.toString());
        } catch (Exception ex) {
            System.out.println("error"+ex.toString());
        }
        
        String msg = "Su ticked es: "+this.entcodigo;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", msg));
    }
    
    public void listarEntregas(){       
        this.emd = new EntregaJpaController();
        this.listaentregas = this.emd.findEntregaEntities();       
    }
    
    public boolean existeTicket(){
        boolean ret = false;
        
        this.emd = new EntregaJpaController();
        Entrega aux = this.emd.findEntrega(this.entcodigo);
        if(aux != null)
            ret = true;
        
        return ret;
    }
    
    public void verificarTicket(){
        String msg;
        
        if(!existeTicket())
            msg = "No se encontro ticket de entrega";
        else
            msg = "Se encontro ticket de entrega";
         
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", msg));
    }
    
    public void actualizar(){
        String msg;
        if(existeTicket()){
            try {
                emd = new EntregaJpaController();
                Entrega aux = emd.findEntrega(this.entcodigo);
                aux.setEntestado(this.entestado);
                this.entfechaentrega = new Date();
                aux.setEntfechaentrega(this.entfechaentrega);
                emd.edit(aux);
            } catch (RollbackFailureException ex) {
                System.out.println("error"+ex.toString());
            } catch (Exception ex) {
                System.out.println("error"+ex.toString());
            }
            msg = "Ticket actualizado!";
        }
        else
            msg = "No existe ticket";
                    
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", msg));
    }
    
    public int getEntregasCount(){
        this.emd = new EntregaJpaController();
        return this.emd.getEntregaCount();
    }
    
    public void inicializar(){
        if(this.getEntregasCount()>0)
            this.listarEntregas();
    }
    
}
