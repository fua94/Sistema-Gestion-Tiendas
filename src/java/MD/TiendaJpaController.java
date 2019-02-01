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
import java.util.ArrayList;
import java.util.List;
import DP.Empleado;
import DP.Producto;
import DP.Tienda;
import DP.Venta;
import MD.exceptions.IllegalOrphanException;
import MD.exceptions.NonexistentEntityException;
import MD.exceptions.PreexistingEntityException;
import MD.exceptions.RollbackFailureException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Francisco
 */
public class TiendaJpaController implements Serializable {

    public TiendaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tienda tienda) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tienda.getClienteList() == null) {
            tienda.setClienteList(new ArrayList<Cliente>());
        }
        if (tienda.getEmpleadoList() == null) {
            tienda.setEmpleadoList(new ArrayList<Empleado>());
        }
        if (tienda.getProductoList() == null) {
            tienda.setProductoList(new ArrayList<Producto>());
        }
        if (tienda.getVentaList() == null) {
            tienda.setVentaList(new ArrayList<Venta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : tienda.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getClicedula());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            tienda.setClienteList(attachedClienteList);
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : tienda.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getEmpcodigo());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            tienda.setEmpleadoList(attachedEmpleadoList);
            List<Producto> attachedProductoList = new ArrayList<Producto>();
            for (Producto productoListProductoToAttach : tienda.getProductoList()) {
                productoListProductoToAttach = em.getReference(productoListProductoToAttach.getClass(), productoListProductoToAttach.getProcodigo());
                attachedProductoList.add(productoListProductoToAttach);
            }
            tienda.setProductoList(attachedProductoList);
            List<Venta> attachedVentaList = new ArrayList<Venta>();
            for (Venta ventaListVentaToAttach : tienda.getVentaList()) {
                ventaListVentaToAttach = em.getReference(ventaListVentaToAttach.getClass(), ventaListVentaToAttach.getVencodigo());
                attachedVentaList.add(ventaListVentaToAttach);
            }
            tienda.setVentaList(attachedVentaList);
            em.persist(tienda);
            for (Cliente clienteListCliente : tienda.getClienteList()) {
                Tienda oldTiecodigoOfClienteListCliente = clienteListCliente.getTiecodigo();
                clienteListCliente.setTiecodigo(tienda);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldTiecodigoOfClienteListCliente != null) {
                    oldTiecodigoOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldTiecodigoOfClienteListCliente = em.merge(oldTiecodigoOfClienteListCliente);
                }
            }
            for (Empleado empleadoListEmpleado : tienda.getEmpleadoList()) {
                Tienda oldTiecodigoOfEmpleadoListEmpleado = empleadoListEmpleado.getTiecodigo();
                empleadoListEmpleado.setTiecodigo(tienda);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldTiecodigoOfEmpleadoListEmpleado != null) {
                    oldTiecodigoOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldTiecodigoOfEmpleadoListEmpleado = em.merge(oldTiecodigoOfEmpleadoListEmpleado);
                }
            }
            for (Producto productoListProducto : tienda.getProductoList()) {
                Tienda oldTiecodigoOfProductoListProducto = productoListProducto.getTiecodigo();
                productoListProducto.setTiecodigo(tienda);
                productoListProducto = em.merge(productoListProducto);
                if (oldTiecodigoOfProductoListProducto != null) {
                    oldTiecodigoOfProductoListProducto.getProductoList().remove(productoListProducto);
                    oldTiecodigoOfProductoListProducto = em.merge(oldTiecodigoOfProductoListProducto);
                }
            }
            for (Venta ventaListVenta : tienda.getVentaList()) {
                Tienda oldTiecodigoOfVentaListVenta = ventaListVenta.getTiecodigo();
                ventaListVenta.setTiecodigo(tienda);
                ventaListVenta = em.merge(ventaListVenta);
                if (oldTiecodigoOfVentaListVenta != null) {
                    oldTiecodigoOfVentaListVenta.getVentaList().remove(ventaListVenta);
                    oldTiecodigoOfVentaListVenta = em.merge(oldTiecodigoOfVentaListVenta);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTienda(tienda.getTiecodigo()) != null) {
                throw new PreexistingEntityException("Tienda " + tienda + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tienda tienda) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tienda persistentTienda = em.find(Tienda.class, tienda.getTiecodigo());
            List<Cliente> clienteListOld = persistentTienda.getClienteList();
            List<Cliente> clienteListNew = tienda.getClienteList();
            List<Empleado> empleadoListOld = persistentTienda.getEmpleadoList();
            List<Empleado> empleadoListNew = tienda.getEmpleadoList();
            List<Producto> productoListOld = persistentTienda.getProductoList();
            List<Producto> productoListNew = tienda.getProductoList();
            List<Venta> ventaListOld = persistentTienda.getVentaList();
            List<Venta> ventaListNew = tienda.getVentaList();
            List<String> illegalOrphanMessages = null;
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteListOldCliente + " since its tiecodigo field is not nullable.");
                }
            }
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado " + empleadoListOldEmpleado + " since its tiecodigo field is not nullable.");
                }
            }
            for (Producto productoListOldProducto : productoListOld) {
                if (!productoListNew.contains(productoListOldProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Producto " + productoListOldProducto + " since its tiecodigo field is not nullable.");
                }
            }
            for (Venta ventaListOldVenta : ventaListOld) {
                if (!ventaListNew.contains(ventaListOldVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venta " + ventaListOldVenta + " since its tiecodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cliente> attachedClienteListNew = new ArrayList<Cliente>();
            for (Cliente clienteListNewClienteToAttach : clienteListNew) {
                clienteListNewClienteToAttach = em.getReference(clienteListNewClienteToAttach.getClass(), clienteListNewClienteToAttach.getClicedula());
                attachedClienteListNew.add(clienteListNewClienteToAttach);
            }
            clienteListNew = attachedClienteListNew;
            tienda.setClienteList(clienteListNew);
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getEmpcodigo());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            tienda.setEmpleadoList(empleadoListNew);
            List<Producto> attachedProductoListNew = new ArrayList<Producto>();
            for (Producto productoListNewProductoToAttach : productoListNew) {
                productoListNewProductoToAttach = em.getReference(productoListNewProductoToAttach.getClass(), productoListNewProductoToAttach.getProcodigo());
                attachedProductoListNew.add(productoListNewProductoToAttach);
            }
            productoListNew = attachedProductoListNew;
            tienda.setProductoList(productoListNew);
            List<Venta> attachedVentaListNew = new ArrayList<Venta>();
            for (Venta ventaListNewVentaToAttach : ventaListNew) {
                ventaListNewVentaToAttach = em.getReference(ventaListNewVentaToAttach.getClass(), ventaListNewVentaToAttach.getVencodigo());
                attachedVentaListNew.add(ventaListNewVentaToAttach);
            }
            ventaListNew = attachedVentaListNew;
            tienda.setVentaList(ventaListNew);
            tienda = em.merge(tienda);
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Tienda oldTiecodigoOfClienteListNewCliente = clienteListNewCliente.getTiecodigo();
                    clienteListNewCliente.setTiecodigo(tienda);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldTiecodigoOfClienteListNewCliente != null && !oldTiecodigoOfClienteListNewCliente.equals(tienda)) {
                        oldTiecodigoOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldTiecodigoOfClienteListNewCliente = em.merge(oldTiecodigoOfClienteListNewCliente);
                    }
                }
            }
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    Tienda oldTiecodigoOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getTiecodigo();
                    empleadoListNewEmpleado.setTiecodigo(tienda);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldTiecodigoOfEmpleadoListNewEmpleado != null && !oldTiecodigoOfEmpleadoListNewEmpleado.equals(tienda)) {
                        oldTiecodigoOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldTiecodigoOfEmpleadoListNewEmpleado = em.merge(oldTiecodigoOfEmpleadoListNewEmpleado);
                    }
                }
            }
            for (Producto productoListNewProducto : productoListNew) {
                if (!productoListOld.contains(productoListNewProducto)) {
                    Tienda oldTiecodigoOfProductoListNewProducto = productoListNewProducto.getTiecodigo();
                    productoListNewProducto.setTiecodigo(tienda);
                    productoListNewProducto = em.merge(productoListNewProducto);
                    if (oldTiecodigoOfProductoListNewProducto != null && !oldTiecodigoOfProductoListNewProducto.equals(tienda)) {
                        oldTiecodigoOfProductoListNewProducto.getProductoList().remove(productoListNewProducto);
                        oldTiecodigoOfProductoListNewProducto = em.merge(oldTiecodigoOfProductoListNewProducto);
                    }
                }
            }
            for (Venta ventaListNewVenta : ventaListNew) {
                if (!ventaListOld.contains(ventaListNewVenta)) {
                    Tienda oldTiecodigoOfVentaListNewVenta = ventaListNewVenta.getTiecodigo();
                    ventaListNewVenta.setTiecodigo(tienda);
                    ventaListNewVenta = em.merge(ventaListNewVenta);
                    if (oldTiecodigoOfVentaListNewVenta != null && !oldTiecodigoOfVentaListNewVenta.equals(tienda)) {
                        oldTiecodigoOfVentaListNewVenta.getVentaList().remove(ventaListNewVenta);
                        oldTiecodigoOfVentaListNewVenta = em.merge(oldTiecodigoOfVentaListNewVenta);
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
                String id = tienda.getTiecodigo();
                if (findTienda(id) == null) {
                    throw new NonexistentEntityException("The tienda with id " + id + " no longer exists.");
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
            Tienda tienda;
            try {
                tienda = em.getReference(Tienda.class, id);
                tienda.getTiecodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tienda with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cliente> clienteListOrphanCheck = tienda.getClienteList();
            for (Cliente clienteListOrphanCheckCliente : clienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tienda (" + tienda + ") cannot be destroyed since the Cliente " + clienteListOrphanCheckCliente + " in its clienteList field has a non-nullable tiecodigo field.");
            }
            List<Empleado> empleadoListOrphanCheck = tienda.getEmpleadoList();
            for (Empleado empleadoListOrphanCheckEmpleado : empleadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tienda (" + tienda + ") cannot be destroyed since the Empleado " + empleadoListOrphanCheckEmpleado + " in its empleadoList field has a non-nullable tiecodigo field.");
            }
            List<Producto> productoListOrphanCheck = tienda.getProductoList();
            for (Producto productoListOrphanCheckProducto : productoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tienda (" + tienda + ") cannot be destroyed since the Producto " + productoListOrphanCheckProducto + " in its productoList field has a non-nullable tiecodigo field.");
            }
            List<Venta> ventaListOrphanCheck = tienda.getVentaList();
            for (Venta ventaListOrphanCheckVenta : ventaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tienda (" + tienda + ") cannot be destroyed since the Venta " + ventaListOrphanCheckVenta + " in its ventaList field has a non-nullable tiecodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tienda);
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

    public List<Tienda> findTiendaEntities() {
        return findTiendaEntities(true, -1, -1);
    }

    public List<Tienda> findTiendaEntities(int maxResults, int firstResult) {
        return findTiendaEntities(false, maxResults, firstResult);
    }

    private List<Tienda> findTiendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tienda.class));
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

    public Tienda findTienda(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tienda.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiendaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tienda> rt = cq.from(Tienda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
