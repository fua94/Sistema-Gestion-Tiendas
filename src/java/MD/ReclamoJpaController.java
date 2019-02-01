/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MD;

import DP.Reclamo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DP.Venta;
import MD.exceptions.NonexistentEntityException;
import MD.exceptions.PreexistingEntityException;
import MD.exceptions.RollbackFailureException;
import java.util.List;
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
public class ReclamoJpaController implements Serializable {

    public ReclamoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public ReclamoJpaController() {
    }

    public EntityManager getEntityManager() {
        if(emf==null)
            emf = Persistence.createEntityManagerFactory("SGTRP_WEBPU");
        return emf.createEntityManager();
    }

    public void create(Reclamo reclamo) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        Context context = new InitialContext();
        utx = (UserTransaction) context.lookup("java:comp/env/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Venta vencodigo = reclamo.getVencodigo();
            if (vencodigo != null) {
                vencodigo = em.getReference(vencodigo.getClass(), vencodigo.getVencodigo());
                reclamo.setVencodigo(vencodigo);
            }
            em.persist(reclamo);
            if (vencodigo != null) {
                vencodigo.getReclamoList().add(reclamo);
                vencodigo = em.merge(vencodigo);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findReclamo(reclamo.getReccodigo()) != null) {
                throw new PreexistingEntityException("Reclamo " + reclamo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reclamo reclamo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        Context context = new InitialContext();
        utx = (UserTransaction) context.lookup("java:comp/env/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Reclamo persistentReclamo = em.find(Reclamo.class, reclamo.getReccodigo());
            Venta vencodigoOld = persistentReclamo.getVencodigo();
            Venta vencodigoNew = reclamo.getVencodigo();
            if (vencodigoNew != null) {
                vencodigoNew = em.getReference(vencodigoNew.getClass(), vencodigoNew.getVencodigo());
                reclamo.setVencodigo(vencodigoNew);
            }
            reclamo = em.merge(reclamo);
            if (vencodigoOld != null && !vencodigoOld.equals(vencodigoNew)) {
                vencodigoOld.getReclamoList().remove(reclamo);
                vencodigoOld = em.merge(vencodigoOld);
            }
            if (vencodigoNew != null && !vencodigoNew.equals(vencodigoOld)) {
                vencodigoNew.getReclamoList().add(reclamo);
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
                String id = reclamo.getReccodigo();
                if (findReclamo(id) == null) {
                    throw new NonexistentEntityException("The reclamo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Reclamo reclamo;
            try {
                reclamo = em.getReference(Reclamo.class, id);
                reclamo.getReccodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reclamo with id " + id + " no longer exists.", enfe);
            }
            Venta vencodigo = reclamo.getVencodigo();
            if (vencodigo != null) {
                vencodigo.getReclamoList().remove(reclamo);
                vencodigo = em.merge(vencodigo);
            }
            em.remove(reclamo);
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

    public List<Reclamo> findReclamoEntities() {
        return findReclamoEntities(true, -1, -1);
    }

    public List<Reclamo> findReclamoEntities(int maxResults, int firstResult) {
        return findReclamoEntities(false, maxResults, firstResult);
    }

    private List<Reclamo> findReclamoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reclamo.class));
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

    public Reclamo findReclamo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reclamo.class, id);
        } finally {
            em.close();
        }
    }

    public int getReclamoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reclamo> rt = cq.from(Reclamo.class);
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
            String qs = "SELECT EXTRACT(YEAR FROM e.recfechaingreso) AS y, EXTRACT(MONTH FROM e.recfechaingreso) as m, COUNT(e) "
                    + "FROM Reclamo e "
                    + "GROUP BY EXTRACT(YEAR FROM e.recfechaingreso), EXTRACT(MONTH FROM e.recfechaingreso) "
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
            String qs = "SELECT EXTRACT(YEAR FROM e.recfechaingreso) AS y, COUNT(e) "
                    + "FROM Reclamo e "
                    + "GROUP BY EXTRACT(YEAR FROM e.recfechaingreso) "
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
            String qs = "SELECT MAX(COUNT(e)) "
                    + "FROM Reclamo e "
                    + "GROUP BY EXTRACT(YEAR FROM e.recfechaingreso) ";
            Query q = em.createQuery(qs);
            Object rsl = q.getSingleResult();
            
            return rsl;
        } finally {
            em.close();
        }
    }
    
    public List<Object[]> obtenerReporteEstado(){
        EntityManager em = getEntityManager();
        try {
            String qs = "SELECT e.recestado, COUNT(e) "
                    + "FROM Reclamo e "
                    + "GROUP BY e.recestado ";
            Query q = em.createQuery(qs);
            List<Object[]> rsl = q.getResultList();
            
            return rsl;
        } finally {
            em.close();
        }
    }
    
}
