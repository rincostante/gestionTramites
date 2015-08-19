/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestiontramites.facades;

import ar.gob.ambiente.servicios.gestiontramites.entidades.Instancia;
import ar.gob.ambiente.servicios.gestiontramites.entidades.Procedimiento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author carmendariz
 */
@Stateless
public class ProcedimientoFacade extends AbstractFacade<Procedimiento> {
    @PersistenceContext(unitName = "gestionTramitesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProcedimientoFacade() {
        super(Procedimiento.class);
    }
   /**
     * Método que verifica si la Procedimiento puede ser eliminada
     * @param id: Id de la procedimiento que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT inst.id FROM Instancia inst "
                + "INNER JOIN inst.procedimiento_id pro "
                + "WHERE pro.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    /**
     * Método para validad que no exista una Actividad Planificada con este nombre ya ingresado
     * @param nombre
     * @param app
     * @return 
     */
    public boolean noExiste(String nombre, int app){
        em = getEntityManager();       
        String queryString = "SELECT pro.nombre FROM Procedimiento pro "
                + "WHERE pro.nombre = :nombre "
                + "AND pro.app = :app";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("app", app);
        return q.getResultList().isEmpty();
    }  
 
    /**
     * Método para validad que no exista una Actividad Planificada con este nombre ya ingresado
     * @param nombre
     * @param procedimiento
     * @return 
     */
    public boolean noExisteInstancia(String nombre, Procedimiento procedimiento){
        em = getEntityManager();
        String queryString = "SELECT inst FROM Instancia inst "
                + "WHERE inst.nombre = :nombre "
                + "AND inst.procedimiento = :procedimiento";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("procedimiento", procedimiento);
         
        return q.getResultList().isEmpty();
    }
 
    
    
    /**
     * Método que obtiene una Procedimiento existente según los datos recibidos como parámetro
     * @param nombre
     * @param app
     * @return 
     */
    public Procedimiento getExistente(String nombre, int app){
        List<Procedimiento> lInst;
        String queryString = "SELECT pro FROM Procedimiento pro "
                + "WHERE pro.nombre = :nombre "
                + "AND pro.app = :app";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("app", app);
        lInst = q.getResultList();
        if(!lInst.isEmpty()){
            return lInst.get(0);
        }else{
            return null;
        }
    }     
    
    /**
     * Método que devuelve todos los procedimientos habilitados y vigentes
     * @return 
     */
    public List<Procedimiento> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT pro FROM Procedimiento pro "
                + "WHERE pro.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }

    /**
     * Método que devuelve todas los Actividades Planificadas deshabilitadas
     * @return 
     */
    public List<Procedimiento> getDeshabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT pro FROM Procedimiento pro "
                + "WHERE pro.adminentidad.habilitado = false";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }  
 
    
    public List<Procedimiento> getProcedimientosXapp(Long app){
        em = getEntityManager();
        String queryString = "SELECT pro FROM Procedimiento pro "
                + "WHERE pro.app = :app "
                + "AND pro.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("app", app);
        return q.getResultList();
    }
    /**
     * Método que verifica si la entidad tiene dependencias
     * @param id: ID de la entidad
     * @return: True o False
     */
    public boolean tieneDependencias(Long id){
        em = getEntityManager();       
        
        String queryString = "SELECT ins FROM Instancia ins " 
                + "WHERE ins.procedimiento.id = :idParam "; 
        
        Query q = em.createQuery(queryString)
                .setParameter("idParam", id);
        return q.getResultList().isEmpty();
 
    }  

    public boolean noExisteInstancia(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void remove(Instancia instancia) {
    }
}      
     
