/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MD;

import DP.Existencia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DP.Producto;
import DP.Proveedor;
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
public class ExistenciaJpaController implements Serializable {

    public ExistenciaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public ExistenciaJpaController() {
    }

    public EntityManager getEntityManager() {
        if(emf==null)
            emf = Persistence.createEntityManagerFactory("SGTRP_WEBPU");
        return emf.createEntityManager();
    }

    public void create(Existencia existencia) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        Context context = new InitialContext();
        utx = (UserTransaction) context.lookup("java:comp/env/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Producto procodigo = existencia.getProcodigo();
            if (procodigo != null) {
                procodigo = em.getReference(procodigo.getClass(), procodigo.getProcodigo());
                existencia.setProcodigo(procodigo);
            }
            Proveedor pvdruc = existencia.getPvdruc();
            if (pvdruc != null) {
                pvdruc = em.getReference(pvdruc.getClass(), pvdruc.getPvdruc());
                existencia.setPvdruc(pvdruc);
            }
            em.persist(existencia);
            if (procodigo != null) {
                procodigo.getExistenciaList().add(existencia);
                procodigo = em.merge(procodigo);
            }
            if (pvdruc != null) {
                pvdruc.getExistenciaList().add(existencia);
                pvdruc = em.merge(pvdruc);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findExistencia(existencia.getExicodigo()) != null) {
                throw new PreexistingEntityException("Existencia " + existencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Existencia existencia) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Existencia persistentExistencia = em.find(Existencia.class, existencia.getExicodigo());
            Producto procodigoOld = persistentExistencia.getProcodigo();
            Producto procodigoNew = existencia.getProcodigo();
            Proveedor pvdrucOld = persistentExistencia.getPvdruc();
            Proveedor pvdrucNew = existencia.getPvdruc();
            if (procodigoNew != null) {
                procodigoNew = em.getReference(procodigoNew.getClass(), procodigoNew.getProcodigo());
                existencia.setProcodigo(procodigoNew);
            }
            if (pvdrucNew != null) {
                pvdrucNew = em.getReference(pvdrucNew.getClass(), pvdrucNew.getPvdruc());
                existencia.setPvdruc(pvdrucNew);
            }
            existencia = em.merge(existencia);
            if (procodigoOld != null && !procodigoOld.equals(procodigoNew)) {
                procodigoOld.getExistenciaList().remove(existencia);
                procodigoOld = em.merge(procodigoOld);
            }
            if (procodigoNew != null && !procodigoNew.equals(procodigoOld)) {
                procodigoNew.getExistenciaList().add(existencia);
                procodigoNew = em.merge(procodigoNew);
            }
            if (pvdrucOld != null && !pvdrucOld.equals(pvdrucNew)) {
                pvdrucOld.getExistenciaList().remove(existencia);
                pvdrucOld = em.merge(pvdrucOld);
            }
            if (pvdrucNew != null && !pvdrucNew.equals(pvdrucOld)) {
                pvdrucNew.getExistenciaList().add(existencia);
                pvdrucNew = em.merge(pvdrucNew);
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
                Integer id = existencia.getExicodigo();
                if (findExistencia(id) == null) {
                    throw new NonexistentEntityException("The existencia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Existencia existencia;
            try {
                existencia = em.getReference(Existencia.class, id);
                existencia.getExicodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The existencia with id " + id + " no longer exists.", enfe);
            }
            Producto procodigo = existencia.getProcodigo();
            if (procodigo != null) {
                procodigo.getExistenciaList().remove(existencia);
                procodigo = em.merge(procodigo);
            }
            Proveedor pvdruc = existencia.getPvdruc();
            if (pvdruc != null) {
                pvdruc.getExistenciaList().remove(existencia);
                pvdruc = em.merge(pvdruc);
            }
            em.remove(existencia);
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

    public List<Existencia> findExistenciaEntities() {
        return findExistenciaEntities(true, -1, -1);
    }

    public List<Existencia> findExistenciaEntities(int maxResults, int firstResult) {
        return findExistenciaEntities(false, maxResults, firstResult);
    }

    private List<Existencia> findExistenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Existencia.class));
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

    public Existencia findExistencia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Existencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getExistenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Existencia> rt = cq.from(Existencia.class);
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
            String qs = "SELECT e.pvdruc, COUNT(e), sum(e.exicantidad*e.exicosto), EXTRACT(MONTH FROM e.exifechaingreso) as m, EXTRACT (YEAR FROM e.exifechaingreso) AS y "
                    + "FROM Existencia e "
                    + "GROUP BY e.pvdruc, EXTRACT (MONTH FROM e.exifechaingreso), EXTRACT (YEAR FROM e.exifechaingreso) "
                    + "ORDER BY y";
            Query q = em.createQuery(qs);
            List<Object[]> rsl = q.getResultList();
            
            return rsl;
        } finally {
            em.close();
        }
    }
    
    public List<Object[]> obtenerReporteAño(){
        EntityManager em = getEntityManager();
        try {
            String qs = "SELECT DISTINCT EXTRACT (YEAR FROM e.exifechaingreso) AS y, SUM(e.exicantidad*e.exicosto) "
                    + "FROM Existencia e "
                    + "GROUP BY EXTRACT (YEAR FROM e.exifechaingreso) "
                    + "ORDER BY y";
            Query q = em.createQuery(qs);
            List<Object[]> rsl = q.getResultList();
            
            return rsl;
        } finally {
            em.close();
        }
    }
    
    public List<Object[]> obtenerReporteProveedor(){
        EntityManager em = getEntityManager();
        try {
            String qs = "SELECT DISTINCT e.pvdruc, COUNT(e) "
                    + "FROM Existencia e "
                    + "GROUP BY e.pvdruc ";
            Query q = em.createQuery(qs);
            List<Object[]> rsl = q.getResultList();
            
            return rsl;
        } finally {
            em.close();
        }
    }
    
    public Object obtenerMaxReporteProveedor(){
        EntityManager em = getEntityManager();
        try {
            String qs = "SELECT MAX(SUM(e.exicantidad*e.exicosto)) "
                    + "FROM Existencia e "
                    + "GROUP BY EXTRACT (YEAR FROM e.exifechaingreso) ";
            Query q = em.createQuery(qs);
            Object rsl = q.getSingleResult();
            
            return rsl;
        } finally {
            em.close();
        }
    }
}
