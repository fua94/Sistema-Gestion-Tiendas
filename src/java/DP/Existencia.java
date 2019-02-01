package DP;

import MD.ExistenciaJpaController;
import MD.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
import javax.xml.bind.annotation.XmlRootElement;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DonutChartModel;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
//import javax.enterprise.context.RequestScoped;
//import javax.inject.Named;

/**
 *
 * @author Francisco
 */
@ManagedBean(name = "existencia")
@ViewScoped
@Entity
@Table(name = "EXISTENCIAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Existencia.findAll", query = "SELECT e FROM Existencia e")
    , @NamedQuery(name = "Existencia.findByExicodigo", query = "SELECT e FROM Existencia e WHERE e.exicodigo = :exicodigo")
    , @NamedQuery(name = "Existencia.findByExicantidad", query = "SELECT e FROM Existencia e WHERE e.exicantidad = :exicantidad")
    , @NamedQuery(name = "Existencia.findByExicosto", query = "SELECT e FROM Existencia e WHERE e.exicosto = :exicosto")
    , @NamedQuery(name = "Existencia.findByExifechaingreso", query = "SELECT e FROM Existencia e WHERE e.exifechaingreso = :exifechaingreso")})
public class Existencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXICODIGO")
    private Integer exicodigo;
    @Column(name = "EXICANTIDAD")
    private Short exicantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "EXICOSTO")
    private BigDecimal exicosto;
    @Column(name = "EXIFECHAINGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date exifechaingreso;
    @JoinColumn(name = "PROCODIGO", referencedColumnName = "PROCODIGO")
    @ManyToOne(optional = false)
    private Producto procodigo;
    @JoinColumn(name = "PVDRUC", referencedColumnName = "PVDRUC")
    @ManyToOne(optional = false)
    private Proveedor pvdruc;
    
    @Transient
    ExistenciaJpaController emd;
    
    //PaginaConsultaExistencia
    @Transient
    private List<Existencia> listaexistencias;
    
    //PaginaReporteExistencia
    @Transient
    private List<Existencia> reporte;
    @Transient
    private List<String> meses;
    @Transient
    private String año;
    @Transient
    private String mes;
    @Transient
    private BarChartModel graficoAño;
    @Transient
    private DonutChartModel graficoProv;

    public Existencia() {
        this.listaexistencias = new ArrayList<Existencia>();
        this.exifechaingreso = new Date();
        this.pvdruc = new Proveedor();
        this.procodigo = new Producto();
        this.meses = new ArrayList<String>();
        this.reporte = new ArrayList<Existencia>();
    }
    
    @PostConstruct
    public void init() {
        inicializar();
    }

    public Existencia(Integer exicodigo) {
        this.exicodigo = exicodigo;
    }

    public Integer getExicodigo() {
        return exicodigo;
    }

    public void setExicodigo(Integer exicodigo) {
        this.exicodigo = exicodigo;
    }

    public Short getExicantidad() {
        return exicantidad;
    }

    public void setExicantidad(Short exicantidad) {
        this.exicantidad = exicantidad;
    }

    public BigDecimal getExicosto() {
        return exicosto;
    }

    public void setExicosto(BigDecimal exicosto) {
        this.exicosto = exicosto;
    }

    public Date getExifechaingreso() {
        return exifechaingreso;
    }

    public void setExifechaingreso(Date exifechaingreso) {
        this.exifechaingreso = exifechaingreso;
    }

    public Producto getProcodigo() {
        return procodigo;
    }

    public void setProcodigo(Producto procodigo) {
        this.procodigo = procodigo;
    }

    public Proveedor getPvdruc() {
        return pvdruc;
    }

    public void setPvdruc(Proveedor pvdruc) {
        this.pvdruc = pvdruc;
    }

    public List<Existencia> getListaexistencias() {
        return listaexistencias;
    }

    public void setListaexistencias(List<Existencia> listaexistencias) {
        this.listaexistencias = listaexistencias;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (exicodigo != null ? exicodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Existencia)) {
            return false;
        }
        Existencia other = (Existencia) object;
        if ((this.exicodigo == null && other.exicodigo != null) || (this.exicodigo != null && !this.exicodigo.equals(other.exicodigo))) {
            return false;
        }
        return true;
    }

    public List<Existencia> getReporte() {
        return reporte;
    }

    public void setReporte(List<Existencia> reporte) {
        this.reporte = reporte;
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

    public DonutChartModel getGraficoProv() {
        return graficoProv;
    }

    @Override
    public String toString() {
        return "Existencia{" + "exicodigo=" + exicodigo + ", exicantidad=" + exicantidad + ", exicosto=" + exicosto + ", exifechaingreso=" + exifechaingreso + ", procodigo=" + procodigo + ", pvdruc=" + pvdruc + '}';
    }  
    
    public boolean buscarProveedor(){
        boolean ret = false;

        List<Proveedor> proveedores = this.pvdruc.obtenerProveedores();
        
        for(Proveedor p : proveedores){
            if(p.getPvdruc().equals(this.pvdruc.getPvdruc()))
                ret = true;
        }
        System.out.println("cod: "+this.pvdruc.getPvdruc());
        return ret;
    }
    
    public void verificarProveedor(){
        String msg;
        
        if(!buscarProveedor())
            msg = "No se encontro el proveedor";
        else
            msg = "Se encontro el proveedor";
         
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", msg));
    }
    
    public boolean buscarProducto(){
        boolean ret = false;

        List<Producto> productos = this.procodigo.obtenerProductos();
        
        for(Producto p : productos){
            if(p.getProcodigo().equals(this.procodigo.getProcodigo()))
                ret = true;
        }
        System.out.println("cod: "+this.procodigo.getProcodigo());
        return ret;
    }
    
    public void verificarProducto(){
        String msg;
        
        if(!buscarProducto())
            msg = "No se encontro el producto";
        else
            msg = "Se encontro el producto";
         
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", msg));
    }
    
    public void ingresar(){
        this.buscarProveedor();
        this.buscarProducto();
        try {
            emd = new ExistenciaJpaController();
            int cont = emd.getExistenciaCount();
            this.exicodigo = cont;
            this.procodigo.incrementarCantidad(this.exicantidad);
            System.out.println("EXISTENCIA: "+this.toString());
            emd.create(this);
        } catch (RollbackFailureException ex) {
            System.out.println("error"+ex.toString());
        } catch (Exception ex) {
            System.out.println("error"+ex.toString());
        }
        
        String msg = "Insertado a inventario";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", msg));
    }
    
    public void listarExistencias(){       
        this.emd = new ExistenciaJpaController();
        this.listaexistencias = this.emd.findExistenciaEntities();       
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
        this.emd = new ExistenciaJpaController();
        for(Object[] o : this.emd.obtenerReporte()){
            Existencia e = new Existencia();
            e.setPvdruc((Proveedor)o[0]);
            String cant = o[1].toString();
            e.setExicantidad(Short.valueOf(cant));
            e.setExicosto((BigDecimal) o[2]);
            int i = Integer.valueOf(o[3].toString());
            e.setMes(this.meses.get(i-1));
            e.setAño(o[4].toString());
            this.reporte.add(e);
        }
    }
    
    private void crearGraficos() {
        this.graficoAño = initBarModel();
        this.graficoAño.setTitle("Coste x Año");
        this.graficoAño.setAnimate(true);
        this.graficoAño.setLegendPosition("ne");
        Axis yAxis = this.graficoAño.getAxis(AxisType.Y);
        yAxis.setLabel("Coste");
        yAxis.setMin(0);
        this.emd = new ExistenciaJpaController();
        double max = Double.valueOf(this.emd.obtenerMaxReporteProveedor().toString());
        yAxis.setMax(max);
        Axis xAxis = this.graficoAño.getAxis(AxisType.X);
        xAxis.setLabel("Año");
         
        this.graficoProv = initDonutModel();
        this.graficoProv.setTitle("Pedidos x Proveedor");
        this.graficoProv.setLegendPosition("e");
        this.graficoProv.setShowDataLabels(true);
        this.graficoProv.setDataFormat("value");
        this.graficoProv.setShadow(false);
    }
     
    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
        this.emd = new ExistenciaJpaController();
        
        ChartSeries años = new ChartSeries();
        años.setLabel("Costes");
        for(Object[] o : this.emd.obtenerReporteAño())
            años.set("'"+o[0].toString().substring(2), Double.valueOf(o[1].toString()));

        model.addSeries(años);
         
        return model;
    }
    
    private DonutChartModel initDonutModel() {
        DonutChartModel model = new DonutChartModel();
        this.emd = new ExistenciaJpaController(); 
        Map<String, Number> provs = new LinkedHashMap<String, Number>();
        for(Object[] o : this.emd.obtenerReporteProveedor()){
            Proveedor p = new Proveedor();
            p = (Proveedor) o[0];
            String cants = o[1].toString();
            int cant = Short.valueOf(cants);
            provs.put(p.getPvdnombre(), cant);
        }
        model.addCircle(provs);
        return model;
    }
    
    public int getExistenciasCount(){
        this.emd = new ExistenciaJpaController();
        return this.emd.getExistenciaCount();
    }
    
    public void inicializar(){
        if(this.getExistenciasCount()>0){
            this.listarExistencias();
            this.listarMeses();
            this.obtenerReporte();
            this.crearGraficos();
        }
    }
}
