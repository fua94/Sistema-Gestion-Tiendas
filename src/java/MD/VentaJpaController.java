/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MD;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DP.Cliente;
import DP.Tienda;
import DP.DetalleVenta;
import java.util.ArrayList;
import java.util.List;
import DP.Entrega;
import DP.Reclamo;
import DP.Venta;
import MD.exceptions.IllegalOrphanException;
import MD.exceptions.NonexistentEntityException;
import MD.exceptions.PreexistingEntityException;
import MD.exceptions.RollbackFailureException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Francisco
 */
public class VentaJpaController implements Serializable {

    public VentaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public VentaJpaController() {
    }

    public EntityManager getEntityManager() {
        if(emf==null)
            emf = Persistence.createEntityManagerFactory("SGTRP_WEBPU");
        return emf.createEntityManager();
    }

    public void create(Venta venta) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (venta.getDetalleVentaList() == null) {
            venta.setDetalleVentaList(new ArrayList<DetalleVenta>());
        }
        if (venta.getEntregaList() == null) {
            venta.setEntregaList(new ArrayList<Entrega>());
        }
        if (venta.getReclamoList() == null) {
            venta.setReclamoList(new ArrayList<Reclamo>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente clicedula = venta.getClicedula();
            if (clicedula != null) {
                clicedula = em.getReference(clicedula.getClass(), clicedula.getClicedula());
                venta.setClicedula(clicedula);
            }
            Tienda tiecodigo = venta.getTiecodigo();
            if (tiecodigo != null) {
                tiecodigo = em.getReference(tiecodigo.getClass(), tiecodigo.getTiecodigo());
                venta.setTiecodigo(tiecodigo);
            }
            List<DetalleVenta> attachedDetalleVentaList = new ArrayList<DetalleVenta>();
            for (DetalleVenta detalleVentaListDetalleVentaToAttach : venta.getDetalleVentaList()) {
                detalleVentaListDetalleVentaToAttach = em.getReference(detalleVentaListDetalleVentaToAttach.getClass(), detalleVentaListDetalleVentaToAttach.getDetcodigo());
                attachedDetalleVentaList.add(detalleVentaListDetalleVentaToAttach);
            }
            venta.setDetalleVentaList(attachedDetalleVentaList);
            List<Entrega> attachedEntregaList = new ArrayList<Entrega>();
            for (Entrega entregaListEntregaToAttach : venta.getEntregaList()) {
                entregaListEntregaToAttach = em.getReference(entregaListEntregaToAttach.getClass(), entregaListEntregaToAttach.getEntcodigo());
                attachedEntregaList.add(entregaListEntregaToAttach);
            }
            venta.setEntregaList(attachedEntregaList);
            List<Reclamo> attachedReclamoList = new ArrayList<Reclamo>();
            for (Reclamo reclamoListReclamoToAttach : venta.getReclamoList()) {
                reclamoListReclamoToAttach = em.getReference(reclamoListReclamoToAttach.getClass(), reclamoListReclamoToAttach.getReccodigo());
                attachedReclamoList.add(reclamoListReclamoToAttach);
            }
            venta.setReclamoList(attachedReclamoList);
            em.persist(venta);
            if (clicedula != null) {
                clicedula.getVentaList().add(venta);
                clicedula = em.merge(clicedula);
            }
            if (tiecodigo != null) {
                tiecodigo.getVentaList().add(venta);
                tiecodigo = em.merge(tiecodigo);
            }
            for (DetalleVenta detalleVentaListDetalleVenta : venta.getDetalleVentaList()) {
                Venta oldVencodigoOfDetalleVentaListDetalleVenta = detalleVentaListDetalleVenta.getVencodigo();
                detalleVentaListDetalleVenta.setVencodigo(venta);
                detalleVentaListDetalleVenta = em.merge(detalleVentaListDetalleVenta);
                if (oldVencodigoOfDetalleVentaListDetalleVenta != null) {
                    oldVencodigoOfDetalleVentaListDetalleVenta.getDetalleVentaList().remove(detalleVentaListDetalleVenta);
                    oldVencodigoOfDetalleVentaListDetalleVenta = em.merge(oldVencodigoOfDetalleVentaListDetalleVenta);
                }
            }
            for (Entrega entregaListEntrega : venta.getEntregaList()) {
                Venta oldVencodigoOfEntregaListEntrega = entregaListEntrega.getVencodigo();
                entregaListEntrega.setVencodigo(venta);
                entregaListEntrega = em.merge(entregaListEntrega);
                if (oldVencodigoOfEntregaListEntrega != null) {
                    oldVencodigoOfEntregaListEntrega.getEntregaList().remove(entregaListEntrega);
                    oldVencodigoOfEntregaListEntrega = em.merge(oldVencodigoOfEntregaListEntrega);
                }
            }
            for (Reclamo reclamoListReclamo : venta.getReclamoList()) {
                Venta oldVencodigoOfReclamoListReclamo = reclamoListReclamo.getVencodigo();
                reclamoListReclamo.setVencodigo(venta);
                reclamoListReclamo = em.merge(reclamoListReclamo);
                if (oldVencodigoOfReclamoListReclamo != null) {
                    oldVencodigoOfReclamoListReclamo.getReclamoList().remove(reclamoListReclamo);
                    oldVencodigoOfReclamoListReclamo = em.merge(oldVencodigoOfReclamoListReclamo);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVenta(venta.getVencodigo()) != null) {
                throw new PreexistingEntityException("Venta " + venta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Venta venta) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Venta persistentVenta = em.find(Venta.class, venta.getVencodigo());
            Cliente clicedulaOld = persistentVenta.getClicedula();
            Cliente clicedulaNew = venta.getClicedula();
            Tienda tiecodigoOld = persistentVenta.getTiecodigo();
            Tienda tiecodigoNew = venta.getTiecodigo();
            List<DetalleVenta> detalleVentaListOld = persistentVenta.getDetalleVentaList();
            List<DetalleVenta> detalleVentaListNew = venta.getDetalleVentaList();
            List<Entrega> entregaListOld = persistentVenta.getEntregaList();
            List<Entrega> entregaListNew = venta.getEntregaList();
            List<Reclamo> reclamoListOld = persistentVenta.getReclamoList();
            List<Reclamo> reclamoListNew = venta.getReclamoList();
            List<String> illegalOrphanMessages = null;
            for (DetalleVenta detalleVentaListOldDetalleVenta : detalleVentaListOld) {
                if (!detalleVentaListNew.contains(detalleVentaListOldDetalleVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleVenta " + detalleVentaListOldDetalleVenta + " since its vencodigo field is not nullable.");
                }
            }
            for (Entrega entregaListOldEntrega : entregaListOld) {
                if (!entregaListNew.contains(entregaListOldEntrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entrega " + entregaListOldEntrega + " since its vencodigo field is not nullable.");
                }
            }
            for (Reclamo reclamoListOldReclamo : reclamoListOld) {
                if (!reclamoListNew.contains(reclamoListOldReclamo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reclamo " + reclamoListOldReclamo + " since its vencodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (clicedulaNew != null) {
                clicedulaNew = em.getReference(clicedulaNew.getClass(), clicedulaNew.getClicedula());
                venta.setClicedula(clicedulaNew);
            }
            if (tiecodigoNew != null) {
                tiecodigoNew = em.getReference(tiecodigoNew.getClass(), tiecodigoNew.getTiecodigo());
                venta.setTiecodigo(tiecodigoNew);
            }
            List<DetalleVenta> attachedDetalleVentaListNew = new ArrayList<DetalleVenta>();
            for (DetalleVenta detalleVentaListNewDetalleVentaToAttach : detalleVentaListNew) {
                detalleVentaListNewDetalleVentaToAttach = em.getReference(detalleVentaListNewDetalleVentaToAttach.getClass(), detalleVentaListNewDetalleVentaToAttach.getDetcodigo());
                attachedDetalleVentaListNew.add(detalleVentaListNewDetalleVentaToAttach);
            }
            detalleVentaListNew = attachedDetalleVentaListNew;
            venta.setDetalleVentaList(detalleVentaListNew);
            List<Entrega> attachedEntregaListNew = new ArrayList<Entrega>();
            for (Entrega entregaListNewEntregaToAttach : entregaListNew) {
                entregaListNewEntregaToAttach = em.getReference(entregaListNewEntregaToAttach.getClass(), entregaListNewEntregaToAttach.getEntcodigo());
                attachedEntregaListNew.add(entregaListNewEntregaToAttach);
            }
            entregaListNew = attachedEntregaListNew;
            venta.setEntregaList(entregaListNew);
            List<Reclamo> attachedReclamoListNew = new ArrayList<Reclamo>();
            for (Reclamo reclamoListNewReclamoToAttach : reclamoListNew) {
                reclamoListNewReclamoToAttach = em.getReference(reclamoListNewReclamoToAttach.getClass(), reclamoListNewReclamoToAttach.getReccodigo());
                attachedReclamoListNew.add(reclamoListNewReclamoToAttach);
            }
            reclamoListNew = attachedReclamoListNew;
            venta.setReclamoList(reclamoListNew);
            venta = em.merge(venta);
            if (clicedulaOld != null && !clicedulaOld.equals(clicedulaNew)) {
                clicedulaOld.getVentaList().remove(venta);
                clicedulaOld = em.merge(clicedulaOld);
            }
            if (clicedulaNew != null && !clicedulaNew.equals(clicedulaOld)) {
                clicedulaNew.getVentaList().add(venta);
                clicedulaNew = em.merge(clicedulaNew);
            }
            if (tiecodigoOld != null && !tiecodigoOld.equals(tiecodigoNew)) {
                tiecodigoOld.getVentaList().remove(venta);
                tiecodigoOld = em.merge(tiecodigoOld);
            }
            if (tiecodigoNew != null && !tiecodigoNew.equals(tiecodigoOld)) {
                tiecodigoNew.getVentaList().add(venta);
                tiecodigoNew = em.merge(tiecodigoNew);
            }
            for (DetalleVenta detalleVentaListNewDetalleVenta : detalleVentaListNew) {
                if (!detalleVentaListOld.contains(detalleVentaListNewDetalleVenta)) {
                    Venta oldVencodigoOfDetalleVentaListNewDetalleVenta = detalleVentaListNewDetalleVenta.getVencodigo();
                    detalleVentaListNewDetalleVenta.setVencodigo(venta);
                    detalleVentaListNewDetalleVenta = em.merge(detalleVentaListNewDetalleVenta);
                    if (oldVencodigoOfDetalleVentaListNewDetalleVenta != null && !oldVencodigoOfDetalleVentaListNewDetalleVenta.equals(venta)) {
                        oldVencodigoOfDetalleVentaListNewDetalleVenta.getDetalleVentaList().remove(detalleVentaListNewDetalleVenta);
                        oldVencodigoOfDetalleVentaListNewDetalleVenta = em.merge(oldVencodigoOfDetalleVentaListNewDetalleVenta);
                    }
                }
            }
            for (Entrega entregaListNewEntrega : entregaListNew) {
                if (!entregaListOld.contains(entregaListNewEntrega)) {
                    Venta oldVencodigoOfEntregaListNewEntrega = entregaListNewEntrega.getVencodigo();
                    entregaListNewEntrega.setVencodigo(venta);
                    entregaListNewEntrega = em.merge(entregaListNewEntrega);
                    if (oldVencodigoOfEntregaListNewEntrega != null && !oldVencodigoOfEntregaListNewEntrega.equals(venta)) {
                        oldVencodigoOfEntregaListNewEntrega.getEntregaList().remove(entregaListNewEntrega);
                        oldVencodigoOfEntregaListNewEntrega = em.merge(oldVencodigoOfEntregaListNewEntrega);
                    }
                }
            }
            for (Reclamo reclamoListNewReclamo : reclamoListNew) {
                if (!reclamoListOld.contains(reclamoListNewReclamo)) {
                    Venta oldVencodigoOfReclamoListNewReclamo = reclamoListNewReclamo.getVencodigo();
                    reclamoListNewReclamo.setVencodigo(venta);
                    reclamoListNewReclamo = em.merge(reclamoListNewReclamo);
                    if (oldVencodigoOfReclamoListNewReclamo != null && !oldVencodigoOfReclamoListNewReclamo.equals(venta)) {
                        oldVencodigoOfReclamoListNewReclamo.getReclamoList().remove(reclamoListNewReclamo);
                        oldVencodigoOfReclamoListNewReclamo = em.merge(oldVencodigoOfReclamoListNewReclamo);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = venta.getVencodigo();
                if (findVenta(id) == null) {
                    throw new NonexistentEntityException("The venta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Venta venta;
            try {
                venta = em.getReference(Venta.class, id);
                venta.getVencodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The venta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleVenta> detalleVentaListOrphanCheck = venta.getDetalleVentaList();
            for (DetalleVenta detalleVentaListOrphanCheckDetalleVenta : detalleVentaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Venta (" + venta + ") cannot be destroyed since the DetalleVenta " + detalleVentaListOrphanCheckDetalleVenta + " in its detalleVentaList field has a non-nullable vencodigo field.");
            }
            List<Entrega> entregaListOrphanCheck = venta.getEntregaList();
            for (Entrega entregaListOrphanCheckEntrega : entregaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Venta (" + venta + ") cannot be destroyed since the Entrega " + entregaListOrphanCheckEntrega + " in its entregaList field has a non-nullable vencodigo field.");
            }
            List<Reclamo> reclamoListOrphanCheck = venta.getReclamoList();
            for (Reclamo reclamoListOrphanCheckReclamo : reclamoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Venta (" + venta + ") cannot be destroyed since the Reclamo " + reclamoListOrphanCheckReclamo + " in its reclamoList field has a non-nullable vencodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente clicedula = venta.getClicedula();
            if (clicedula != null) {
                clicedula.getVentaList().remove(venta);
                clicedula = em.merge(clicedula);
            }
            Tienda tiecodigo = venta.getTiecodigo();
            if (tiecodigo != null) {
                tiecodigo.getVentaList().remove(venta);
                tiecodigo = em.merge(tiecodigo);
            }
            em.remove(venta);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Venta> findVentaEntities() {
        return findVentaEntities(true, -1, -1);
    }

    public List<Venta> findVentaEntities(int maxResults, int firstResult) {
        return findVentaEntities(false, maxResults, firstResult);
    }

    private List<Venta> findVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Venta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Venta findVenta(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Venta> rt = cq.from(Venta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Object[]> obtenerReporte(){
        EntityManager em = getEntityManager();
        try {
            String qs = "SELECT EXTRACT(YEAR FROM e.venfechaingreso) AS y, EXTRACT(MONTH FROM e.venfechaingreso) as m, COUNT(e) "
                    + "FROM Venta e "
                    + "GROUP BY EXTRACT(YEAR FROM e.venfechaingreso), EXTRACT(MONTH FROM e.venfechaingreso) "
                    + "ORDER BY y";
            Query q = em.createQuery(qs);
            List<Object[]> rsl = q.getResultList();
            
            return rsl;
        } finally {
            em.close();
        }
    }

    public List<Object[]> obtenerReporteAnual(){
        EntityManager em = getEntityManager();
        try {
            String qs = "SELECT EXTRACT(YEAR FROM e.venfechaingreso) AS y, COUNT(e) "
                    + "FROM Venta e "
                    + "GROUP BY EXTRACT(YEAR FROM e.venfechaingreso) "
                    + "ORDER BY y";
            Query q = em.createQuery(qs);
            List<Object[]> rsl = q.getResultList();
            
            return rsl;
        } finally {
            em.close();
        }
    }

    public Object obtenerMaxReporteAnual(){
        EntityManager em = getEntityManager();
        try {
            String qs = "SELECT max(sum(d.procodigo.proprecio*d.detcantidad)) "
                    + "FROM Venta v "
                    + "INNER JOIN DetalleVenta d "
                    + "ON v.vencodigo = d.vencodigo.vencodigo "                    
                    +"GROUP BY Extract(Year from v.venfechaingreso)";
            Query q = em.createQuery(qs);
            Object rsl = q.getSingleResult();
            
            return rsl;
        } finally {
            em.close();
        }
    }
     
    public List<Object[]> obtenerReporteVendido(){
        EntityManager em = getEntityManager();
        try {
            //Extract(YEAR from v.venfechaingreso), sum(p.proprecio*d.detcantidad)
            String qs = "SELECT Extract(YEAR from v.venfechaingreso), sum(d.procodigo.proprecio*d.detcantidad) "
                    + "FROM Venta v "
                    + "INNER JOIN DetalleVenta d "
                    + "ON v.vencodigo = d.vencodigo.vencodigo "                    
                    +"GROUP BY Extract(Year from v.venfechaingreso)";
                    
            Query q = em.createQuery(qs);
            List<Object[]> rsl = q.getResultList();
            
            for(Object[] o : rsl){
                for(int i=0; i<o.length; i++)
                    System.out.print("rs: "+o[i].toString());
                System.out.println("");
            }
            
            return rsl;
        } finally {
            em.close();
        }
    }
    
}
