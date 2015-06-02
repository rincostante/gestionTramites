/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestiontramites.facades;

import ar.gob.ambiente.servicios.gestiontramites.entidades.Estado;
import ar.gob.ambiente.servicios.gestiontramites.entidades.Instancia;
import ar.gob.ambiente.servicios.gestiontramites.entidades.Procedimiento;
import ar.gob.ambiente.servicios.gestiontramites.entidades.UnidadDeTiempo;
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
     * @param nombre
     * @param procedimiento
     * @return: devuelve True o False
     */
    public boolean noExiste(String nombre, int procedimiento){
        em = getEntityManager();       
        String queryString = "SELECT ins.nombre FROM Instancia ins "
                + "WHERE ins.nombre = :nombre "
                + "AND ins.procedimiento = :procedimiento";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("procedimiento", procedimiento);
        return q.getResultList().isEmpty();
    }      


    /**
     * Metodo que verifica si ya existe la entidad.
     * @param nombre
     * @return: devuelve True o False
     */
    public boolean existe(String nombre){
        em = getEntityManager();       
        String queryString = "SELECT ins.nombre FROM Instancia ins "
                + "WHERE ins.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        return q.getResultList().isEmpty();
    }  
    
    public Instancia getExistente(String nombre, Procedimiento procedimiento){
        em = getEntityManager();       
        String queryString = "SELECT ins.nombre FROM Instancia ins "
                + "WHERE esp.nombre = :nombre "
                + "AND esp.procedimiento = :procedimiento";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("procedimiento", procedimiento);
        return (Instancia)q.getSingleResult();
    }

    public List<String> getNombre(){
        em = getEntityManager();
        String queryString = "SELECT ins.nombre FROM Instancia ins";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }

    public List<Instancia> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT ins FROM Instancia ins "
                + "WHERE ins.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }

    public List<Instancia> getUnidadDeTiempo(Long id){
        em = getEntityManager();        
        String queryString = "SELECT ins FROM Instancia ins "
                + "WHERE ins.unidaddetiempo.id = :id "
                + "AND ins.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList();
    }




    /**
     * Método que devuelve todos los géneros de una familia
     * @param estado
     * @return 
     */
    public List<Instancia> getInstanciasXEstado(Estado estado){
        em = getEntityManager();        
        String queryString = "SELECT ins FROM Instancia ins "
                + "WHERE ins.estado = :estado "
                + "AND ins.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("estado", estado);
        return q.getResultList();
    } 
        
    /**
     * Método que verifica si la entidad tiene dependencias
     * @param id: ID de la entidad
     * @return: True o False
     */
    public boolean noTieneDependencias(Long id){
        em = getEntityManager();       
        String queryString = "SELECT pro FROM Procedimiento pro " 
                + "WHERE pro.instancia.id = :id";      
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    }       

}