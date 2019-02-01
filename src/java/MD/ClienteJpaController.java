/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MD;

import DP.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DP.Tienda;
import DP.Venta;
import MD.exceptions.IllegalOrphanException;
import MD.exceptions.NonexistentEntityException;
import MD.exceptions.PreexistingEntityException;
import MD.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Francisco
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (cliente.getVentaList() == null) {
            cliente.setVentaList(new ArrayList<Venta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tienda tiecodigo = cliente.getTiecodigo();
            if (tiecodigo != null) {
                tiecodigo = em.getReference(tiecodigo.getClass(), tiecodigo.getTiecodigo());
                cliente.setTiecodigo(tiecodigo);
            }
            List<Venta> attachedVentaList = new ArrayList<Venta>();
            for (Venta ventaListVentaToAttach : cliente.getVentaList()) {
                ventaListVentaToAttach = em.getReference(ventaListVentaToAttach.getClass(), ventaListVentaToAttach.getVencodigo());
                attachedVentaList.add(ventaListVentaToAttach);
            }
            cliente.setVentaList(attachedVentaList);
            em.persist(cliente);
            if (tiecodigo != null) {
                tiecodigo.getClienteList().add(cliente);
                tiecodigo = em.merge(tiecodigo);
            }
            for (Venta ventaListVenta : cliente.getVentaList()) {
                Cliente oldClicedulaOfVentaListVenta = ventaListVenta.getClicedula();
                ventaListVenta.setClicedula(cliente);
                ventaListVenta = em.merge(ventaListVenta);
                if (oldClicedulaOfVentaListVenta != null) {
                    oldClicedulaOfVentaListVenta.getVentaList().remove(ventaListVenta);
                    oldClicedulaOfVentaListVenta = em.merge(oldClicedulaOfVentaListVenta);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCliente(cliente.getClicedula()) != null) {
                throw new PreexistingEntityException("Cliente " + cliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getClicedula());
            Tienda tiecodigoOld = persistentCliente.getTiecodigo();
            Tienda tiecodigoNew = cliente.getTiecodigo();
            List<Venta> ventaListOld = persistentCliente.getVentaList();
            List<Venta> ventaListNew = cliente.getVentaList();
            List<String> illegalOrphanMessages = null;
            for (Venta ventaListOldVenta : ventaListOld) {
                if (!ventaListNew.contains(ventaListOldVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venta " + ventaListOldVenta + " since its clicedula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tiecodigoNew != null) {
                tiecodigoNew = em.getReference(tiecodigoNew.getClass(), tiecodigoNew.getTiecodigo());
                cliente.setTiecodigo(tiecodigoNew);
            }
            List<Venta> attachedVentaListNew = new ArrayList<Venta>();
            for (Venta ventaListNewVentaToAttach : ventaListNew) {
                ventaListNewVentaToAttach = em.getReference(ventaListNewVentaToAttach.getClass(), ventaListNewVentaToAttach.getVencodigo());
                attachedVentaListNew.add(ventaListNewVentaToAttach);
            }
            ventaListNew = attachedVentaListNew;
            cliente.setVentaList(ventaListNew);
            cliente = em.merge(cliente);
            if (tiecodigoOld != null && !tiecodigoOld.equals(tiecodigoNew)) {
                tiecodigoOld.getClienteList().remove(cliente);
                tiecodigoOld = em.merge(tiecodigoOld);
            }
            if (tiecodigoNew != null && !tiecodigoNew.equals(tiecodigoOld)) {
                tiecodigoNew.getClienteList().add(cliente);
                tiecodigoNew = em.merge(tiecodigoNew);
            }
            for (Venta ventaListNewVenta : ventaListNew) {
                if (!ventaListOld.contains(ventaListNewVenta)) {
                    Cliente oldClicedulaOfVentaListNewVenta = ventaListNewVenta.getClicedula();
                    ventaListNewVenta.setClicedula(cliente);
                    ventaListNewVenta = em.merge(ventaListNewVenta);
                    if (oldClicedulaOfVentaListNewVenta != null && !oldClicedulaOfVentaListNewVenta.equals(cliente)) {
                        oldClicedulaOfVentaListNewVenta.getVentaList().remove(ventaListNewVenta);
                        oldClicedulaOfVentaListNewVenta = em.merge(oldClicedulaOfVentaListNewVenta);
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
                String id = cliente.getClicedula();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getClicedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Venta> ventaListOrphanCheck = cliente.getVentaList();
            for (Venta ventaListOrphanCheckVenta : ventaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Venta " + ventaListOrphanCheckVenta + " in its ventaList field has a non-nullable clicedula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tienda tiecodigo = cliente.getTiecodigo();
            if (tiecodigo != null) {
                tiecodigo.getClienteList().remove(cliente);
                tiecodigo = em.merge(tiecodigo);
            }
            em.remove(cliente);
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

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
