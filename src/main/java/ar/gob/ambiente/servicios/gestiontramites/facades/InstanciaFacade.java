/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestiontramites.facades;

import ar.gob.ambiente.servicios.gestiontramites.entidades.Instancia;
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
public class InstanciaFacade extends AbstractFacade<Instancia> {
    @PersistenceContext(unitName = "gestionTramitesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InstanciaFacade() {
        super(Instancia.class);
    }
    
   /**
     * Metodo que verifica si ya existe la entidad.
     * @param aBuscar: es la cadena que buscara para ver si ya existe en la BDD
     * @return: devuelve True o False
     */
    public boolean noExiste(String aBuscar){
        em = getEntityManager();
        String queryString = "SELECT ins.nombre FROM Instancia ins "
                + "WHERE ins.nombre = :stringParam "
                + "AND ins.adminentidad.habilitado = true";
        
        Query q = em.createQuery(queryString)
                .setParameter("stringParam", aBuscar);
        return q.getResultList().isEmpty();
    }  
      
    
     /**
     * Metodo para el autocompletado de la búsqueda por nombre
     * @return 
     */
    public List<String> getNombre(){
        em = getEntityManager();
        String queryString = "SELECT ins.nombre FROM Instancia ins "
                + "AND ins.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
 
    /**
     * Método que devuelve un LIST con las entidades HABILITADAS
     * @return: True o False
     */
    public List<Instancia> getHabilitadas(){
        em = getEntityManager();
        List<Instancia> result;
        String queryString = "SELECT ins FROM Instancia ins "
                + "WHERE ins.adminentidad.habilitado = true "
                + "ORDER BY ins.nombre";
        Query q = em.createQuery(queryString);
        result = q.getResultList();
        return result;
    }
        

 
    public List<Instancia> getInstanciasXIdProcedimiento(Long idProc) {
        em = getEntityManager(); 
        String queryString = "SELECT inst FROM Instancia inst "
                + "INNER JOIN inst.procedimiento_id pro "
                + "WHERE pro.id = :idProc "
                + "AND inst.adminentidad.habilitado = true"; 
        Query q = em.createQuery(queryString)
                .setParameter("idProc", idProc);
        return q.getResultList();
    }

   /**
     * Método que devuelve todos los géneros de una familia
     * @param unidadDeTiempoAlerta
     * @return 
     */
    public List<Instancia> getUnidadDeTiempoAlerta(Long id, Object unidadDeTiempoAlerta){
        em = getEntityManager();        
        String queryString = "SELECT ins FROM Instancia ins "
                + "WHERE ins.unidaddetiempoalerta_id = :unidadDeTiempoAlerta "
                + "AND ins.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("unidadDeTiempoAlerta", unidadDeTiempoAlerta);
        return q.getResultList();
    } 
    /**
     * Método que devuelve todos los géneros de una familia
     * @param unidadDeTiempoVto
     * @return 
     */
    public List<Instancia> getUnidadDeTiempoVto(Long id, Object unidadDeTiempoVto){
        em = getEntityManager();        
        String queryString = "SELECT ins FROM Instancia ins "
                + "WHERE ins.unidaddetiempovto_id = :unidadDeTiempoVto "
                + "AND ins.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("unidadDeTiempoVto", unidadDeTiempoVto);
        return q.getResultList();
    }     


    /**
     * Método que devuelve todos los géneros de una familia
     * @param estadoInicial
     * @return 
     */
    public List<Instancia> getEstadoInicial(Long id, Object estadoInicial){
        em = getEntityManager();        
        String queryString = "SELECT ins FROM Instancia ins "
                + "WHERE ins.estadoinicial_id = :estadoInicial "
                + "AND ins.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("estadoInicial", estadoInicial);
        return q.getResultList();
    } 
    /**
     * Método que devuelve todos los géneros de una familia
     * @param estadoFinal
     * @return 
     */
    public List<Instancia> getEstadoFinal(Long id, Object estadoFinal){
        em = getEntityManager();        
        String queryString = "SELECT ins FROM Instancia ins "
                + "WHERE ins.estadofinal_id = :estadoFinal "
                + "AND ins.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("estadoFinal", estadoFinal);
        return q.getResultList();
    }     
     /**
     * Método que devuelve todos las instancias de una app
     * @param app
     * @return 
     */
    public List<Instancia> getApp(Long id, Object app){
        em = getEntityManager();        
        String queryString = "SELECT ins FROM Instancia ins "
                + "WHERE ins.app = :app "
                + "AND ins.adminentidad.habilitado = true";
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
}