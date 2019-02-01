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
import DP.Tienda;
import DP.Existencia;
import java.util.ArrayList;
import java.util.List;
import DP.DetalleVenta;
import DP.Producto;
import MD.exceptions.IllegalOrphanException;
import MD.exceptions.NonexistentEntityException;
import MD.exceptions.PreexistingEntityException;
import MD.exceptions.RollbackFailureException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Francisco
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public ProductoJpaController() {
    }

    public EntityManager getEntityManager() {
        if(emf==null)
            emf = Persistence.createEntityManagerFactory("SGTRP_WEBPU");
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (producto.getExistenciaList() == null) {
            producto.setExistenciaList(new ArrayList<Existencia>());
        }
        if (producto.getDetalleVentaList() == null) {
            producto.setDetalleVentaList(new ArrayList<DetalleVenta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tienda tiecodigo = producto.getTiecodigo();
            if (tiecodigo != null) {
                tiecodigo = em.getReference(tiecodigo.getClass(), tiecodigo.getTiecodigo());
                producto.setTiecodigo(tiecodigo);
            }
            List<Existencia> attachedExistenciaList = new ArrayList<Existencia>();
            for (Existencia existenciaListExistenciaToAttach : producto.getExistenciaList()) {
                existenciaListExistenciaToAttach = em.getReference(existenciaListExistenciaToAttach.getClass(), existenciaListExistenciaToAttach.getExicodigo());
                attachedExistenciaList.add(existenciaListExistenciaToAttach);
            }
            producto.setExistenciaList(attachedExistenciaList);
            List<DetalleVenta> attachedDetalleVentaList = new ArrayList<DetalleVenta>();
            for (DetalleVenta detalleVentaListDetalleVentaToAttach : producto.getDetalleVentaList()) {
                detalleVentaListDetalleVentaToAttach = em.getReference(detalleVentaListDetalleVentaToAttach.getClass(), detalleVentaListDetalleVentaToAttach.getDetcodigo());
                attachedDetalleVentaList.add(detalleVentaListDetalleVentaToAttach);
            }
            producto.setDetalleVentaList(attachedDetalleVentaList);
            em.persist(producto);
            if (tiecodigo != null) {
                tiecodigo.getProductoList().add(producto);
                tiecodigo = em.merge(tiecodigo);
            }
            for (Existencia existenciaListExistencia : producto.getExistenciaList()) {
                Producto oldProcodigoOfExistenciaListExistencia = existenciaListExistencia.getProcodigo();
                existenciaListExistencia.setProcodigo(producto);
                existenciaListExistencia = em.merge(existenciaListExistencia);
                if (oldProcodigoOfExistenciaListExistencia != null) {
                    oldProcodigoOfExistenciaListExistencia.getExistenciaList().remove(existenciaListExistencia);
                    oldProcodigoOfExistenciaListExistencia = em.merge(oldProcodigoOfExistenciaListExistencia);
                }
            }
            for (DetalleVenta detalleVentaListDetalleVenta : producto.getDetalleVentaList()) {
                Producto oldProcodigoOfDetalleVentaListDetalleVenta = detalleVentaListDetalleVenta.getProcodigo();
                detalleVentaListDetalleVenta.setProcodigo(producto);
                detalleVentaListDetalleVenta = em.merge(detalleVentaListDetalleVenta);
                if (oldProcodigoOfDetalleVentaListDetalleVenta != null) {
                    oldProcodigoOfDetalleVentaListDetalleVenta.getDetalleVentaList().remove(detalleVentaListDetalleVenta);
                    oldProcodigoOfDetalleVentaListDetalleVenta = em.merge(oldProcodigoOfDetalleVentaListDetalleVenta);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProducto(producto.getProcodigo()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        Context context = new InitialContext();
        utx = (UserTransaction) context.lookup("java:comp/env/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Producto persistentProducto = em.find(Producto.class, producto.getProcodigo());
            Tienda tiecodigoOld = persistentProducto.getTiecodigo();
            Tienda tiecodigoNew = producto.getTiecodigo();
            List<Existencia> existenciaListOld = persistentProducto.getExistenciaList();
            List<Existencia> existenciaListNew = producto.getExistenciaList();
            List<DetalleVenta> detalleVentaListOld = persistentProducto.getDetalleVentaList();
            List<DetalleVenta> detalleVentaListNew = producto.getDetalleVentaList();
            List<String> illegalOrphanMessages = null;
            for (Existencia existenciaListOldExistencia : existenciaListOld) {
                if (!existenciaListNew.contains(existenciaListOldExistencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Existencia " + existenciaListOldExistencia + " since its procodigo field is not nullable.");
                }
            }
            for (DetalleVenta detalleVentaListOldDetalleVenta : detalleVentaListOld) {
                if (!detalleVentaListNew.contains(detalleVentaListOldDetalleVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleVenta " + detalleVentaListOldDetalleVenta + " since its procodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tiecodigoNew != null) {
                tiecodigoNew = em.getReference(tiecodigoNew.getClass(), tiecodigoNew.getTiecodigo());
                producto.setTiecodigo(tiecodigoNew);
            }
            List<Existencia> attachedExistenciaListNew = new ArrayList<Existencia>();
            for (Existencia existenciaListNewExistenciaToAttach : existenciaListNew) {
                existenciaListNewExistenciaToAttach = em.getReference(existenciaListNewExistenciaToAttach.getClass(), existenciaListNewExistenciaToAttach.getExicodigo());
                attachedExistenciaListNew.add(existenciaListNewExistenciaToAttach);
            }
            existenciaListNew = attachedExistenciaListNew;
            producto.setExistenciaList(existenciaListNew);
            List<DetalleVenta> attachedDetalleVentaListNew = new ArrayList<DetalleVenta>();
            for (DetalleVenta detalleVentaListNewDetalleVentaToAttach : detalleVentaListNew) {
                detalleVentaListNewDetalleVentaToAttach = em.getReference(detalleVentaListNewDetalleVentaToAttach.getClass(), detalleVentaListNewDetalleVentaToAttach.getDetcodigo());
                attachedDetalleVentaListNew.add(detalleVentaListNewDetalleVentaToAttach);
            }
            detalleVentaListNew = attachedDetalleVentaListNew;
            producto.setDetalleVentaList(detalleVentaListNew);
            producto = em.merge(producto);
            if (tiecodigoOld != null && !tiecodigoOld.equals(tiecodigoNew)) {
                tiecodigoOld.getProductoList().remove(producto);
                tiecodigoOld = em.merge(tiecodigoOld);
            }
            if (tiecodigoNew != null && !tiecodigoNew.equals(tiecodigoOld)) {
                tiecodigoNew.getProductoList().add(producto);
                tiecodigoNew = em.merge(tiecodigoNew);
            }
            for (Existencia existenciaListNewExistencia : existenciaListNew) {
                if (!existenciaListOld.contains(existenciaListNewExistencia)) {
                    Producto oldProcodigoOfExistenciaListNewExistencia = existenciaListNewExistencia.getProcodigo();
                    existenciaListNewExistencia.setProcodigo(producto);
                    existenciaListNewExistencia = em.merge(existenciaListNewExistencia);
                    if (oldProcodigoOfExistenciaListNewExistencia != null && !oldProcodigoOfExistenciaListNewExistencia.equals(producto)) {
                        oldProcodigoOfExistenciaListNewExistencia.getExistenciaList().remove(existenciaListNewExistencia);
                        oldProcodigoOfExistenciaListNewExistencia = em.merge(oldProcodigoOfExistenciaListNewExistencia);
                    }
                }
            }
            for (DetalleVenta detalleVentaListNewDetalleVenta : detalleVentaListNew) {
                if (!detalleVentaListOld.contains(detalleVentaListNewDetalleVenta)) {
                    Producto oldProcodigoOfDetalleVentaListNewDetalleVenta = detalleVentaListNewDetalleVenta.getProcodigo();
                    detalleVentaListNewDetalleVenta.setProcodigo(producto);
                    detalleVentaListNewDetalleVenta = em.merge(detalleVentaListNewDetalleVenta);
                    if (oldProcodigoOfDetalleVentaListNewDetalleVenta != null && !oldProcodigoOfDetalleVentaListNewDetalleVenta.equals(producto)) {
                        oldProcodigoOfDetalleVentaListNewDetalleVenta.getDetalleVentaList().remove(detalleVentaListNewDetalleVenta);
                        oldProcodigoOfDetalleVentaListNewDetalleVenta = em.merge(oldProcodigoOfDetalleVentaListNewDetalleVenta);
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
                String id = producto.getProcodigo();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getProcodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Existencia> existenciaListOrphanCheck = producto.getExistenciaList();
            for (Existencia existenciaListOrphanCheckExistencia : existenciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Existencia " + existenciaListOrphanCheckExistencia + " in its existenciaList field has a non-nullable procodigo field.");
            }
            List<DetalleVenta> detalleVentaListOrphanCheck = producto.getDetalleVentaList();
            for (DetalleVenta detalleVentaListOrphanCheckDetalleVenta : detalleVentaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the DetalleVenta " + detalleVentaListOrphanCheckDetalleVenta + " in its detalleVentaList field has a non-nullable procodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tienda tiecodigo = producto.getTiecodigo();
            if (tiecodigo != null) {
                tiecodigo.getProductoList().remove(producto);
                tiecodigo = em.merge(tiecodigo);
            }
            em.remove(producto);
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

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
