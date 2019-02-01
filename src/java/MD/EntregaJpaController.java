/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MD;

import DP.Entrega;
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
public class EntregaJpaController implements Serializable {

    public EntregaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntregaJpaController() {
    }

    public EntityManager getEntityManager() {
        if(emf==null)
            emf = Persistence.createEntityManagerFactory("SGTRP_WEBPU");
        return emf.createEntityManager();
    }

    public void create(Entrega entrega) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        Context context = new InitialContext();
        utx = (UserTransaction) context.lookup("java:comp/env/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Venta vencodigo = entrega.getVencodigo();
            if (vencodigo != null) {
                vencodigo = em.getReference(vencodigo.getClass(), vencodigo.getVencodigo());
                entrega.setVencodigo(vencodigo);
            }
            em.persist(entrega);
            if (vencodigo != null) {
                vencodigo.getEntregaList().add(entrega);
                vencodigo = em.merge(vencodigo);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEntrega(entrega.getEntcodigo()) != null) {
                throw new PreexistingEntityException("Entrega " + entrega + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entrega entrega) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        Context context = new InitialContext();
        utx = (UserTransaction) context.lookup("java:comp/env/UserTransaction");
        try {
            utx.begin();
            em = getEntityManager();
            Entrega persistentEntrega = em.find(Entrega.class, entrega.getEntcodigo());
            Venta vencodigoOld = persistentEntrega.getVencodigo();
            Venta vencodigoNew = entrega.getVencodigo();
            if (vencodigoNew != null) {
                vencodigoNew = em.getReference(vencodigoNew.getClass(), vencodigoNew.getVencodigo());
                entrega.setVencodigo(vencodigoNew);
            }
            entrega = em.merge(entrega);
            if (vencodigoOld != null && !vencodigoOld.equals(vencodigoNew)) {
                vencodigoOld.getEntregaList().remove(entrega);
                vencodigoOld = em.merge(vencodigoOld);
            }
            if (vencodigoNew != null && !vencodigoNew.equals(vencodigoOld)) {
                vencodigoNew.getEntregaList().add(entrega);
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
                String id = entrega.getEntcodigo();
                if (findEntrega(id) == null) {
                    throw new NonexistentEntityException("The entrega with id " + id + " no longer exists.");
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
            Entrega entrega;
            try {
                entrega = em.getReference(Entrega.class, id);
                entrega.getEntcodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entrega with id " + id + " no longer exists.", enfe);
            }
            Venta vencodigo = entrega.getVencodigo();
            if (vencodigo != null) {
                vencodigo.getEntregaList().remove(entrega);
                vencodigo = em.merge(vencodigo);
            }
            em.remove(entrega);
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

    public List<Entrega> findEntregaEntities() {
        return findEntregaEntities(true, -1, -1);
    }

    public List<Entrega> findEntregaEntities(int maxResults, int firstResult) {
        return findEntregaEntities(false, maxResults, firstResult);
    }

    private List<Entrega> findEntregaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entrega.class));
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

    public Entrega findEntrega(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entrega.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntregaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entrega> rt = cq.from(Entrega.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
