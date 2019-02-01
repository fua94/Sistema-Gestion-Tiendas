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
import DP.Existencia;
import DP.Proveedor;
import MD.exceptions.IllegalOrphanException;
import MD.exceptions.NonexistentEntityException;
import MD.exceptions.PreexistingEntityException;
import MD.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Francisco
 */
public class ProveedorJpaController implements Serializable {

    public ProveedorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public ProveedorJpaController() {
    }

    public EntityManager getEntityManager() {
        if(emf==null)
            emf = Persistence.createEntityManagerFactory("SGTRP_WEBPU");
        return emf.createEntityManager();
    }

    public void create(Proveedor proveedor) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (proveedor.getExistenciaList() == null) {
            proveedor.setExistenciaList(new ArrayList<Existencia>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Existencia> attachedExistenciaList = new ArrayList<Existencia>();
            for (Existencia existenciaListExistenciaToAttach : proveedor.getExistenciaList()) {
                existenciaListExistenciaToAttach = em.getReference(existenciaListExistenciaToAttach.getClass(), existenciaListExistenciaToAttach.getExicodigo());
                attachedExistenciaList.add(existenciaListExistenciaToAttach);
            }
            proveedor.setExistenciaList(attachedExistenciaList);
            em.persist(proveedor);
            for (Existencia existenciaListExistencia : proveedor.getExistenciaList()) {
                Proveedor oldPvdrucOfExistenciaListExistencia = existenciaListExistencia.getPvdruc();
                existenciaListExistencia.setPvdruc(proveedor);
                existenciaListExistencia = em.merge(existenciaListExistencia);
                if (oldPvdrucOfExistenciaListExistencia != null) {
                    oldPvdrucOfExistenciaListExistencia.getExistenciaList().remove(existenciaListExistencia);
                    oldPvdrucOfExistenciaListExistencia = em.merge(oldPvdrucOfExistenciaListExistencia);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProveedor(proveedor.getPvdruc()) != null) {
                throw new PreexistingEntityException("Proveedor " + proveedor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedor proveedor) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getPvdruc());
            List<Existencia> existenciaListOld = persistentProveedor.getExistenciaList();
            List<Existencia> existenciaListNew = proveedor.getExistenciaList();
            List<String> illegalOrphanMessages = null;
            for (Existencia existenciaListOldExistencia : existenciaListOld) {
                if (!existenciaListNew.contains(existenciaListOldExistencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Existencia " + existenciaListOldExistencia + " since its pvdruc field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Existencia> attachedExistenciaListNew = new ArrayList<Existencia>();
            for (Existencia existenciaListNewExistenciaToAttach : existenciaListNew) {
                existenciaListNewExistenciaToAttach = em.getReference(existenciaListNewExistenciaToAttach.getClass(), existenciaListNewExistenciaToAttach.getExicodigo());
                attachedExistenciaListNew.add(existenciaListNewExistenciaToAttach);
            }
            existenciaListNew = attachedExistenciaListNew;
            proveedor.setExistenciaList(existenciaListNew);
            proveedor = em.merge(proveedor);
            for (Existencia existenciaListNewExistencia : existenciaListNew) {
                if (!existenciaListOld.contains(existenciaListNewExistencia)) {
                    Proveedor oldPvdrucOfExistenciaListNewExistencia = existenciaListNewExistencia.getPvdruc();
                    existenciaListNewExistencia.setPvdruc(proveedor);
                    existenciaListNewExistencia = em.merge(existenciaListNewExistencia);
                    if (oldPvdrucOfExistenciaListNewExistencia != null && !oldPvdrucOfExistenciaListNewExistencia.equals(proveedor)) {
                        oldPvdrucOfExistenciaListNewExistencia.getExistenciaList().remove(existenciaListNewExistencia);
                        oldPvdrucOfExistenciaListNewExistencia = em.merge(oldPvdrucOfExistenciaListNewExistencia);
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
                String id = proveedor.getPvdruc();
                if (findProveedor(id) == null) {
                    throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.");
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
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getPvdruc();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Existencia> existenciaListOrphanCheck = proveedor.getExistenciaList();
            for (Existencia existenciaListOrphanCheckExistencia : existenciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the Existencia " + existenciaListOrphanCheckExistencia + " in its existenciaList field has a non-nullable pvdruc field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(proveedor);
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

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(int maxResults, int firstResult) {
        return findProveedorEntities(false, maxResults, firstResult);
    }

    private List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedor.class));
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

    public Proveedor findProveedor(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedor> rt = cq.from(Proveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
