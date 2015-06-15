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
     * Metodo que verifica si ya existe la entidad.
     * @param nombre
     * @param app
     * @return: devuelve True o False
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
     * Metodo que verifica si ya existe la entidad.
     * @param aBuscar: es la cadena que buscara para ver si ya existe en la BDD
     * @return: devuelve True o False
     */
    public boolean existe(String aBuscar){
        em = getEntityManager();       
        String queryString = "SELECT pro.nombre FROM Procedimiento pro "
                + "WHERE pro.nombre = :stringParam ";
        Query q = em.createQuery(queryString)
                .setParameter("stringParam", aBuscar);
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
        String queryString = "SELECT reg FROM Procedimiento reg "
                + "WHERE reg.nombre = :nombre "
                + "AND reg.app = :app";
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

    public List<String> getNombre(){
        em = getEntityManager();
        String queryString = "SELECT pro.nombre FROM Procedimiento pro";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }

    public List<Procedimiento> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT pro FROM Procedimiento pro "
                + "WHERE pro.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    public List<Procedimiento> getXInstancia(Long id){
        em = getEntityManager();
        String queryString = "SELECT pro FROM Procedimiento pro "
                + "WHERE pro.instancia.id = :id "
                + "AND pro.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList();
    }    

    public void edit(Instancia current) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Método que verifica si la entidad tiene dependencias
     * @param id: ID de la entidad
     * @return: True o False
     */
    public boolean noTieneDependencias(Long id){
        em = getEntityManager();       
        String queryString = "SELECT ins FROM Instancia ins " 
                + "WHERE ins.procedimiento.id = :id";      
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    }          
   /**
     * Método que verifica si el procedimiento puede ser eliminado
     * @param id: Id de la region que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT inst.id FROM Instancia inst "
                + "INNER JOIN inst.procedimientos pro "
                + "WHERE pro.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
}

