/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DP;
import MD.ReclamoJpaController;
import MD.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
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
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DonutChartModel;

/**
 *
 * @author Francisco
 */
@Named(value = "reclamo")
@RequestScoped
@Entity
@Table(name = "RECLAMOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reclamo.findAll", query = "SELECT r FROM Reclamo r")
    , @NamedQuery(name = "Reclamo.findByReccodigo", query = "SELECT r FROM Reclamo r WHERE r.reccodigo = :reccodigo")
    , @NamedQuery(name = "Reclamo.findByRecdescripcion", query = "SELECT r FROM Reclamo r WHERE r.recdescripcion = :recdescripcion")
    , @NamedQuery(name = "Reclamo.findByRecfechafin", query = "SELECT r FROM Reclamo r WHERE r.recfechafin = :recfechafin")
    , @NamedQuery(name = "Reclamo.findByRecestado", query = "SELECT r FROM Reclamo r WHERE r.recestado = :recestado")
    , @NamedQuery(name = "Reclamo.findByRecfechaingreso", query = "SELECT r FROM Reclamo r WHERE r.recfechaingreso = :recfechaingreso")})
public class Reclamo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "RECCODIGO")
    private String reccodigo;
    @Size(max = 50)
    @Column(name = "RECDESCRIPCION")
    private String recdescripcion;
    @Column(name = "RECFECHAFIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recfechafin;
    @Size(max = 1)
    @Column(name = "RECESTADO")
    private String recestado;
    @Column(name = "RECFECHAINGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recfechaingreso;
    @JoinColumn(name = "VENCODIGO", referencedColumnName = "VENCODIGO")
    @ManyToOne(optional = false)
    private Venta vencodigo;    
    @Transient
    private String cedula;    
    @Transient
    private ReclamoJpaController rmd;    
    @Transient
    private List<Reclamo> listareclamos;   
    
    //PaginaReporteReclamo
    @Transient
    private List<Reclamo> reporte;
    @Transient
    private List<String> meses;
    @Transient
    private String año;
    @Transient
    private String mes;
    @Transient
    private int cantidad;    
    @Transient
    private BarChartModel graficoAño;
    @Transient
    private DonutChartModel graficoEst;
    
    public Reclamo() {
        this.recfechaingreso = new Date();
        this.recestado="P";
        reporte = new ArrayList<Reclamo>();
        this.meses = new ArrayList<String>();
        this.listareclamos = new ArrayList<Reclamo>();
        this.vencodigo = new Venta();
    }   
    
    @PostConstruct
    public void init() {
        inicializar();
    }
    
    public Reclamo(String reccodigo) {
        this.reccodigo = reccodigo;
    }
    public String getReccodigo() {
        return reccodigo;
    }
    public void setReccodigo(String reccodigo) {
        this.reccodigo = reccodigo;
    }
    public String getRecdescripcion() {
        return recdescripcion;
    }
    public void setRecdescripcion(String recdescripcion) {
        this.recdescripcion = recdescripcion;
    }
    public Date getRecfechafin() {
        return recfechafin;
    }
    public void setRecfechafin(Date recfechafin) {
        this.recfechafin = recfechafin;
    }
    public String getRecestado() {
        return recestado;
    }
    public void setRecestado(String recestado) {
        this.recestado = recestado;
    }
    public Date getRecfechaingreso() {
        return recfechaingreso;
    }
    public void setRecfechaingreso(Date recfechaingreso) {
        this.recfechaingreso = recfechaingreso;
    }
    public Venta getVencodigo() {
        return vencodigo;
    }
    public void setVencodigo(Venta vencodigo) {
        this.vencodigo = vencodigo;
    }
    public String getCedula() {
        return cedula;
    }
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    public List<Reclamo> getListareclamos() {
        return listareclamos;
    }
    public void setListareclamos(List<Reclamo> listareclamos) {
        this.listareclamos = listareclamos;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reccodigo != null ? reccodigo.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reclamo)) {
            return false;
        }
        Reclamo other = (Reclamo) object;
        if ((this.reccodigo == null && other.reccodigo != null) || (this.reccodigo != null && !this.reccodigo.equals(other.reccodigo))) {
            return false;
        }
        return true;
    }
    public List<Reclamo> getReporte() {
        return reporte;
    }
    public void setReporte(List<Reclamo> reporte) {
        this.reporte = reporte;
    }
    public List<String> getMeses() {
        return meses;
    }
    public void setMeses(List<String> meses) {
        this.meses = meses;
    }
    public String getAño() {
        return año;
    }
    public void setAño(String año) {
        this.año = año;
    }
    public String getMes() {
        return mes;
    }
    public void setMes(String mes) {
        this.mes = mes;
    }
    public BarChartModel getGraficoAño() {
        return graficoAño;
    }
    public DonutChartModel getGraficoEst() {
        return graficoEst;
    }

    @Override
    public String toString() {
        return "Reclamo{" + "reccodigo=" + reccodigo + ", recdescripcion=" + recdescripcion + ", recfechafin=" + recfechafin + ", recestado=" + recestado + ", recfechaingreso=" + recfechaingreso + ", vencodigo=" + vencodigo + ", cedula=" + cedula + '}';
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
        cod = "R-"+cod.substring(2);
        this.reccodigo = cod;
        System.out.println("ts: "+this.toString());
        try {
            rmd = new ReclamoJpaController();
            rmd.create(this);
        } catch (RollbackFailureException ex) {
            System.out.println("error"+ex.toString());
        } catch (Exception ex) {
            System.out.println("error"+ex.toString());
        }
    }
    
    public void actualizar(){
        String msg;
        if(existeTicket()){
            try {
                this.rmd = new ReclamoJpaController();
                Reclamo aux = this.rmd.findReclamo(this.reccodigo);
                aux.setRecestado(this.recestado);
                this.recfechafin = new Date();
                aux.setRecfechafin(this.recfechafin);
                System.out.println(""+aux.toString());
                this.rmd.edit(aux);
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
    
    public void listarReclamos(){       
        rmd = new ReclamoJpaController();
        listareclamos = rmd.findReclamoEntities();
    }
   
    public void listarMeses(){
        this.meses.add("Enero");
        this.meses.add("Febrero");
        this.meses.add("Marzo");
        this.meses.add("Abril");
        this.meses.add("Mayo");
        this.meses.add("Junio");
        this.meses.add("Julio");
        this.meses.add("Agosto");
        this.meses.add("Septiembre");
        this.meses.add("Octubre");
        this.meses.add("Noviembre");
        this.meses.add("Diciembre");
    }
   
    public void obtenerReporte(){
        this.rmd = new ReclamoJpaController();
        for(Object[] o : this.rmd.obtenerReporte()){
            Reclamo r = new Reclamo();
            r.setAño(o[0].toString());
            int i = Integer.valueOf(o[1].toString());
            r.setMes(this.meses.get(i-1));
            String cant = o[2].toString();
            r.setCantidad(Short.valueOf(cant));
            this.reporte.add(r);
        }  
    }
   
    private void crearGraficos() {
        this.graficoAño = initBarModel();
        this.graficoAño.setTitle("Reclamos x Año");
        this.graficoAño.setAnimate(true);
        this.graficoAño.setLegendPosition("ne");
        Axis yAxis = this.graficoAño.getAxis(AxisType.Y);
        yAxis.setLabel("Reclamos");
        yAxis.setMin(0);
        this.rmd = new ReclamoJpaController();
        double max = Double.valueOf(this.rmd.obtenerMaxReporteAnual().toString());
        yAxis.setMax(max);
        Axis xAxis = this.graficoAño.getAxis(AxisType.X);
        xAxis.setLabel("Año");
         
        this.graficoEst= initDonutModel();
        this.graficoEst.setTitle("Reclamos x Estado");
        this.graficoEst.setLegendPosition("e");
        this.graficoEst.setShowDataLabels(true);
        this.graficoEst.setDataFormat("value");
        this.graficoEst.setShadow(false);
    }
     
    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
        this.rmd = new ReclamoJpaController();
        
        ChartSeries años = new ChartSeries();
        años.setLabel("Reclamos");
        for(Object[] o : this.rmd.obtenerReporteAnual())
            años.set("'"+o[0].toString().substring(2), Double.valueOf(o[1].toString()));

        model.addSeries(años);
         
        return model;
    }
    
    private DonutChartModel initDonutModel() {
        DonutChartModel model = new DonutChartModel();
        this.rmd = new ReclamoJpaController();
        Map<String, Number> mapa = new LinkedHashMap<String, Number>();
        for(Object[] o : this.rmd.obtenerReporteEstado()){
            String e = o[0].toString();
            String estado="";
            switch(e){
                case "P": estado="Pendiente"; break;
                case "S": estado="Solucionado"; break;
                case "R": estado="Rechazado"; break;
            }
            String cants = o[1].toString();
            int cant = Short.valueOf(cants);
            mapa.put(estado, cant);
        }
        model.addCircle(mapa);
        return model;
    }
   
    public int getReclamosCount(){
        this.rmd = new ReclamoJpaController();
        return this.rmd.getReclamoCount();
    }
    
    public boolean existeTicket(){
        boolean ret = false;
        
        this.rmd = new ReclamoJpaController();
        Reclamo aux = this.rmd.findReclamo(this.reccodigo);
        if(aux != null)
            ret = true;
        
        return ret;
    }
    
    public void verificarTicket(){
        String msg;
        
        if(!existeTicket())
            msg = "No se encontro ticket de reclamo";
        else
            msg = "Se encontro ticket de reclamo";
         
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", msg));
    }
    
    public void inicializar(){
        if(this.getReclamosCount()>0){
            this.listarMeses();
            this.obtenerReporte();
            this.crearGraficos();
            this.listarReclamos();
        }
    }
    
}
