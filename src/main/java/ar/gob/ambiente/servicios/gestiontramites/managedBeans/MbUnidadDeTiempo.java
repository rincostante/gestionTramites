/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestiontramites.managedBeans;

import ar.gob.ambiente.servicios.gestiontramites.entidades.Instancia;
import ar.gob.ambiente.servicios.gestiontramites.entidades.UnidadDeTiempo;
import ar.gob.ambiente.servicios.gestiontramites.entidades.util.JsfUtil;
import ar.gob.ambiente.servicios.gestiontramites.facades.UnidadDeTiempoFacade;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rincostante
 */
public class MbUnidadDeTiempo implements Serializable{
    
    private UnidadDeTiempo current;
    private List<UnidadDeTiempo> listado = null;
    private List<UnidadDeTiempo> listaFilter;    
    private List<Instancia> listInsFilter;
    
    @EJB
    private UnidadDeTiempoFacade unidadDeTiempoFacade;

    private boolean iniciado;    

    /**
     * Creates a new instance of MbUnidadDeTiempo
     */
    public MbUnidadDeTiempo() {
    }

    public List<Instancia> getListInsFilter() {
        return listInsFilter;
    }

    public void setListInsFilter(List<Instancia> listInsFilter) {
        this.listInsFilter = listInsFilter;
    }

    public UnidadDeTiempo getCurrent() {
        return current;
    }

    public void setCurrent(UnidadDeTiempo current) {
        this.current = current;
    }

    public List<UnidadDeTiempo> getListado() {
        if (listado == null || listado.isEmpty()) {
            listado = getFacade().findAll();
        }
        return listado;
    }

    public void setListado(List<UnidadDeTiempo> listado) {
        this.listado = listado;
    }

    public List<UnidadDeTiempo> getListaFilter() {
        return listaFilter;
    }

    public void setListaFilter(List<UnidadDeTiempo> listaFilter) {
        this.listaFilter = listaFilter;
    }

    /****************************
     * Métodos de inicialización
     ****************************/
    /**
     * Método que se ejecuta luego de instanciada la clase e inicializa los datos del usuario
     */
    @PostConstruct
    public void init(){
        iniciado = false;
    }
    /**
     * Método que borra de la memoria los MB innecesarios al cargar el listado 
     */
     public void iniciar(){
        if(!iniciado){
            String s;
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
            .getExternalContext().getSession(true);
            Enumeration enume = session.getAttributeNames();
            while(enume.hasMoreElements()){
                s = (String)enume.nextElement();
                if(s.substring(0, 2).equals("mb")){
                    if(!s.equals("mbUsuario") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }   
    
    /**
     * @return acción para el listado de entidades a mostrar en el list
     */
    public String prepareList() {
        recreateModel();
        return "list";
    }
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        return "view";
    }
    
    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        current = new UnidadDeTiempo();
        return "new";
    }   
    
    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        return "edit";
    }    
    
    /**
     * Método que verifica que el Cargo que se quiere eliminar no esté siento utilizado por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        boolean libre = getFacade().noTieneDependencias(current.getId());

        if (libre){
            // Elimina
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("UnidadDeTiempoNonDeletable"));
        }
        return "view";
    }    
    
    
    /****************************
     * Métodos de validación
     ****************************/    
    
    /**
     * Método para validar que no exista ya una entidad con este nombre al momento de crearla
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarInsert(FacesContext arg0, UIComponent arg1, Object arg2){
        validarExistente(arg2);
    }
    
    /**
     * Método para validar que no exista una entidad con este nombre, siempre que dicho nombre no sea el que tenía originalmente
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     * @throws ValidatorException 
     */
    public void validarUpdate(FacesContext arg0, UIComponent arg1, Object arg2){
        if(!current.getNombre().equals((String)arg2)){
            validarExistente(arg2);
        }
    }    

    
    /**********************
     * Métodos de operación
     **********************/
    /**
    * @return 
    */   
    public String create() {     
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UnidadDeTiempoCreated"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("UnidadDeTiempoCreatedErrorOccured"));
            return null;
        }
    }
    

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UnidadDeTiempoUpdated"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("UnidadDeTiempoUpdatedErrorOccured"));
            return null;
        }
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        listado.clear();
    }    
    
    
    /*******************************
    ** Métodos para la navegación **
    ********************************/   
    /**
     * @return La entidad gestionada
     */
 
    public UnidadDeTiempo getSelected() {
        if (current == null) {
            current = new UnidadDeTiempo();
        }
        return current;
    } 
    
    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public UnidadDeTiempo getUnidadDeTiempo(java.lang.Long id) {
        return unidadDeTiempoFacade.find(id);
    }  
    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private UnidadDeTiempoFacade getFacade() {
        return unidadDeTiempoFacade;
    }    
    
    private void validarExistente(Object arg2) throws ValidatorException{
        if(!getFacade().noExiste((String)arg2)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("CreateUnidadDeTiempoExistente")));
        }
    }    
    
    /**
     * Opera el borrado de la entidad
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UnidadDeTiempoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("UnidadDeTiempoDeletedErrorOccured"));
        }
    }    
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = UnidadDeTiempo.class)
    public static class UnidadDeTiempoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbUnidadDeTiempo controller = (MbUnidadDeTiempo) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbUnidadDeTiempo");
            return controller.getUnidadDeTiempo(getKey(value));
        }

        
        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }
        
        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof UnidadDeTiempo) {
                UnidadDeTiempo o = (UnidadDeTiempo) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + UnidadDeTiempo.class.getName());
            }
        }
    }            
}