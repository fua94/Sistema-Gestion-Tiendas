/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MD;

import DP.DetalleVenta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DP.Producto;
import DP.Venta;
import MD.exceptions.NonexistentEntityException;
import MD.exceptions.PreexistingEntityException;
import MD.exceptions.RollbackFailureException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Francisco
 */
public class DetalleVentaJpaController implements Serializable {

    public DetalleVentaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleVenta detalleVenta) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto procodigo = detalleVenta.getProcodigo();
            if (procodigo != null) {
                procodigo = em.getReference(procodigo.getClass(), procodigo.getProcodigo());
                detalleVenta.setProcodigo(procodigo);
            }
            Venta vencodigo = detalleVenta.getVencodigo();
            if (vencodigo != null) {
                vencodigo = em.getReference(vencodigo.getClass(), vencodigo.getVencodigo());
                detalleVenta.setVencodigo(vencodigo);
            }
            em.persist(detalleVenta);
            if (procodigo != null) {
                procodigo.getDetalleVentaList().add(detalleVenta);
                procodigo = em.merge(procodigo);
            }
            if (vencodigo != null) {
                vencodigo.getDetalleVentaList().add(detalleVenta);
                vencodigo = em.merge(vencodigo);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetalleVenta(detalleVenta.getDetcodigo()) != null) {
                throw new PreexistingEntityException("DetalleVenta " + detalleVenta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleVenta detalleVenta) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetalleVenta persistentDetalleVenta = em.find(DetalleVenta.class, detalleVenta.getDetcodigo());
            Producto procodigoOld = persistentDetalleVenta.getProcodigo();
            Producto procodigoNew = detalleVenta.getProcodigo();
            Venta vencodigoOld = persistentDetalleVenta.getVencodigo();
            Venta vencodigoNew = detalleVenta.getVencodigo();
            if (procodigoNew != null) {
                procodigoNew = em.getReference(procodigoNew.getClass(), procodigoNew.getProcodigo());
                detalleVenta.setProcodigo(procodigoNew);
            }
            if (vencodigoNew != null) {
                vencodigoNew = em.getReference(vencodigoNew.getClass(), vencodigoNew.getVencodigo());
                detalleVenta.setVencodigo(vencodigoNew);
            }
            detalleVenta = em.merge(detalleVenta);
            if (procodigoOld != null && !procodigoOld.equals(procodigoNew)) {
                procodigoOld.getDetalleVentaList().remove(detalleVenta);
                procodigoOld = em.merge(procodigoOld);
            }
            if (procodigoNew != null && !procodigoNew.equals(procodigoOld)) {
                procodigoNew.getDetalleVentaList().add(detalleVenta);
                procodigoNew = em.merge(procodigoNew);
            }
            if (vencodigoOld != null && !vencodigoOld.equals(vencodigoNew)) {
                vencodigoOld.getDetalleVentaList().remove(detalleVenta);
                vencodigoOld = em.merge(vencodigoOld);
            }
            if (vencodigoNew != null && !vencodigoNew.equals(vencodigoOld)) {
                vencodigoNew.getDetalleVentaList().add(detalleVenta);
                vencodigoNew = em.merge(vencodigoNew);
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
                Short id = detalleVenta.getDetcodigo();
                if (findDetalleVenta(id) == null) {
                    throw new NonexistentEntityException("The detalleVenta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetalleVenta detalleVenta;
            try {
                detalleVenta = em.getReference(DetalleVenta.class, id);
                detalleVenta.getDetcodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleVenta with id " + id + " no longer exists.", enfe);
            }
            Producto procodigo = detalleVenta.getProcodigo();
            if (procodigo != null) {
                procodigo.getDetalleVentaList().remove(detalleVenta);
                procodigo = em.merge(procodigo);
            }
            Venta vencodigo = detalleVenta.getVencodigo();
            if (vencodigo != null) {
                vencodigo.getDetalleVentaList().remove(detalleVenta);
                vencodigo = em.merge(vencodigo);
            }
            em.remove(detalleVenta);
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

    public List<DetalleVenta> findDetalleVentaEntities() {
        return findDetalleVentaEntities(true, -1, -1);
    }

    public List<DetalleVenta> findDetalleVentaEntities(int maxResults, int firstResult) {
        return findDetalleVentaEntities(false, maxResults, firstResult);
    }

    private List<DetalleVenta> findDetalleVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleVenta.class));
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

    public DetalleVenta findDetalleVenta(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleVenta.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleVenta> rt = cq.from(DetalleVenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
