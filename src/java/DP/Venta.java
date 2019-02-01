/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DP;

import MD.ReclamoJpaController;
import MD.VentaJpaController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
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
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DonutChartModel;

/**
 *
 * @author Francisco
 */
@Named(value = "venta")
@RequestScoped
@Entity
@Table(name = "VENTAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v")
    , @NamedQuery(name = "Venta.findByVencodigo", query = "SELECT v FROM Venta v WHERE v.vencodigo = :vencodigo")
    , @NamedQuery(name = "Venta.findByVenfechaingreso", query = "SELECT v FROM Venta v WHERE v.venfechaingreso = :venfechaingreso")})
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "VENCODIGO")
    private String vencodigo;
    @Column(name = "VENFECHAINGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date venfechaingreso;
    @JoinColumn(name = "CLICEDULA", referencedColumnName = "CLICEDULA")
    @ManyToOne(optional = false)
    private Cliente clicedula;
    @JoinColumn(name = "TIECODIGO", referencedColumnName = "TIECODIGO")
    @ManyToOne(optional = false)
    private Tienda tiecodigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vencodigo")
    private List<DetalleVenta> detalleVentaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vencodigo")
    private List<Entrega> entregaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vencodigo")
    private List<Reclamo> reclamoList;
    
    @Transient
    private VentaJpaController vmd;
    
    //PaginaReporteReclamo
    @Transient
    private List<Venta> reporte;
    @Transient
    private List<String> meses;
    @Transient
    private String año;
    @Transient
    private String mes;
    @Transient
    private BigDecimal vendido;
    
    @Transient
    private int cantidad;
    
    @Transient
    private BarChartModel graficoAño;
    @Transient
    private DonutChartModel graficoVendido;    
      

    public Venta() {
        this.venfechaingreso = new Date();
        reporte = new ArrayList<Venta>();
        this.meses = new ArrayList<String>();
    }
    
    @PostConstruct
    public void init() {
        this.obtenerVentas();
        this.listarMeses();
        this.obtenerReporte();
        this.crearGraficos();
    }


    public Venta(String vencodigo) {
        this.vencodigo = vencodigo;
    }

    public String getVencodigo() {
        return vencodigo;
    }

    public void setVencodigo(String vencodigo) {
        this.vencodigo = vencodigo;
    }

    public Date getVenfechaingreso() {
        return venfechaingreso;
    }

    public void setVenfechaingreso(Date venfechaingreso) {
        this.venfechaingreso = venfechaingreso;
    }

    public Cliente getClicedula() {
        return clicedula;
    }

    public void setClicedula(Cliente clicedula) {
        this.clicedula = clicedula;
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

    @XmlTransient
    public List<Entrega> getEntregaList() {
        return entregaList;
    }

    public void setEntregaList(List<Entrega> entregaList) {
        this.entregaList = entregaList;
    }

    @XmlTransient
    public List<Reclamo> getReclamoList() {
        return reclamoList;
    }

    public void setReclamoList(List<Reclamo> reclamoList) {
        this.reclamoList = reclamoList;
    }

    public BigDecimal getVendido() {
        return vendido;
    }

    public void setVendido(BigDecimal vendido) {
        this.vendido = vendido;
    }

    public List<Venta> getReporte() {
        return reporte;
    }

    public void setReporte(List<Venta> reporte) {
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

    public DonutChartModel getGraficoVendido() {
        return graficoVendido;
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
        hash += (vencodigo != null ? vencodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.vencodigo == null && other.vencodigo != null) || (this.vencodigo != null && !this.vencodigo.equals(other.vencodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DP.Venta[ vencodigo=" + vencodigo + " ]";
    }
    
    public List<Venta> obtenerVentas(){
        vmd = new VentaJpaController();
        return vmd.findVentaEntities();
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
        this.vmd = new VentaJpaController();
        for(Object[] o : this.vmd.obtenerReporte()){
            Venta v = new Venta();
            v.setAño(o[0].toString());
            int i = Integer.valueOf(o[1].toString());
            v.setMes(this.meses.get(i-1));
            String cant = o[2].toString();
            v.setCantidad(Short.valueOf(cant));
            this.reporte.add(v);
        }
    }
   
       private void crearGraficos() {
        this.graficoAño = initBarModel();
        this.graficoAño.setTitle("Ingresos x Año");
        this.graficoAño.setAnimate(true);
        this.graficoAño.setLegendPosition("ne");
        Axis yAxis = this.graficoAño.getAxis(AxisType.Y);
        yAxis.setLabel("Ingresos");
        yAxis.setMin(0);
        this.vmd = new VentaJpaController();
        double max = Double.valueOf(this.vmd.obtenerMaxReporteAnual().toString());
        yAxis.setMax(max);
        Axis xAxis = this.graficoAño.getAxis(AxisType.X);
        xAxis.setLabel("Año");
         
        this.graficoVendido= initDonutModel();
        this.graficoVendido.setTitle("Ventas x Año");
        this.graficoVendido.setLegendPosition("e");
        this.graficoVendido.setShowDataLabels(true);
        this.graficoVendido.setDataFormat("value");
        this.graficoVendido.setShadow(false);
    }
     
    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
        this.vmd = new VentaJpaController();
        
        ChartSeries años = new ChartSeries();
        años.setLabel("Ventas");
        for(Object[] o : this.vmd.obtenerReporteVendido())
            años.set("'"+o[0].toString().substring(2), Double.valueOf(o[1].toString()));

        model.addSeries(años);
         
        return model;
    }
    
    private DonutChartModel initDonutModel() {
        DonutChartModel model = new DonutChartModel();
        this.vmd = new VentaJpaController();
        Map<String, Number> mapa = new LinkedHashMap<String, Number>();
        for(Object[] o : this.vmd.obtenerReporteAnual()){
            String e = o[0].toString();
            String cants = o[1].toString();
            BigDecimal cant = BigDecimal.valueOf(Double.valueOf(cants));
            mapa.put(e, cant);
        }
        model.addCircle(mapa);
        return model;
    }
    
}
