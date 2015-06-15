/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestiontramites.managedBeans;

import ar.gob.ambiente.servicios.gestiontramites.entidades.AdminEntidad;
import ar.gob.ambiente.servicios.gestiontramites.entidades.Estado;
import ar.gob.ambiente.servicios.gestiontramites.entidades.Instancia;
import ar.gob.ambiente.servicios.gestiontramites.entidades.Procedimiento;
import ar.gob.ambiente.servicios.gestiontramites.entidades.UnidadDeTiempo;
import ar.gob.ambiente.servicios.gestiontramites.entidades.Usuario;
import ar.gob.ambiente.servicios.gestiontramites.entidades.util.JsfUtil;
import ar.gob.ambiente.servicios.gestiontramites.facades.EstadoFacade;
import ar.gob.ambiente.servicios.gestiontramites.facades.InstanciaFacade;
import ar.gob.ambiente.servicios.gestiontramites.facades.UnidadDeTiempoFacade;
import ar.gob.ambiente.servicios.gestiontramites.facades.ProcedimientoFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author carmendariz
 */
public class MbProcedimiento implements Serializable{
    
    private Procedimiento current;
    private DataModel items = null;
    private List<Procedimiento> listFilter;
    private List<Instancia> instVinc;
    private List<Instancia> instVincFilter;
    private List<Instancia> instDisp;
    private List<Instancia> instDispFilter;
    private List<Instancia> instancias;
    private List<Instancia> instanciasFilter;
    private List<Instancia> listInstancias;
    private boolean asignaInstancia; 
    private List<Procedimiento> listProcedimiento;

    
    @EJB
    private ProcedimientoFacade procedimientoFacade;
    private boolean iniciado;
    //private int update; // 0=updateNormal | 1=deshabiliar | 2=habilitar 
    private MbLogin login;
    private Usuario usLogeado;
    private Procedimiento procedimientoSelected;
    
    @EJB
    private InstanciaFacade instFacade;
    /*
     * Creates a new instance of MbProcedimiento
     */
    public MbProcedimiento() {
         
    }   

    /********************************
     ** Getters y Setters ***********
     ********************************/     
    
    public List<Procedimiento> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<Procedimiento> listFilter) {
        this.listFilter = listFilter;
    }

    public List<Procedimiento> getListProcedimiento() {
        if(listProcedimiento == null){
            listProcedimiento = getFacade().getHabilitadas();
        }
        return listProcedimiento;
    }

    public void setListProcedimiento(List<Procedimiento> listProcedimiento) {
        this.listProcedimiento = listProcedimiento;
    }     
    
    public Procedimiento getCurrent() {
        return current;
    }

    public void setCurrent(Procedimiento current) {
        this.current = current;
    }

    public List<Instancia> getInstVinc() {
        return instVinc;
    }

    public void setInstVinc(List<Instancia> instVinc) {
        this.instVinc = instVinc;
    }

    public List<Instancia> getInstVincFilter() {
        return instVincFilter;
    }

    public void setInstVincFilter(List<Instancia> instVincFilter) {
        this.instVincFilter = instVincFilter;
    }

    public List<Instancia> getInstDisp() {
        return instDisp;
    }

    public void setInstDisp(List<Instancia> instDisp) {
        this.instDisp = instDisp;
    }

    public List<Instancia> getInstDispFilter() {
        return instDispFilter;
    }

    public void setInstDispFilter(List<Instancia> instDispFilter) {
        this.instDispFilter = instDispFilter;
    }

    public List<Instancia> getInstancias() {
        return instancias;
    }

    public void setInstancias(List<Instancia> instancias) {
        this.instancias = instancias;
    }

    public List<Instancia> getInstanciasFilter() {
        return instanciasFilter;
    }

    public void setInstanciasFilter(List<Instancia> instanciasFilter) {
        this.instanciasFilter = instanciasFilter;
    }

    public List<Instancia> getListInstancias() {
        return listInstancias;
    }

    public void setListInstancias(List<Instancia> listInstancias) {
        this.listInstancias = listInstancias;
    }

    public Procedimiento getProcedimientoSelected() {
        return procedimientoSelected;
    }

    public void setProcedimientoSelected(Procedimiento procedimientoSelected) {
        this.procedimientoSelected = procedimientoSelected;
    }

    /**
     * @return La entidad gestionada
     */
    public Procedimiento getSelected() {
        if (current == null) {
            current = new Procedimiento();
        }
        return current;
    }   
    
    /**
     * @return el listado de entidades a mostrar en el list
     */
   
    public DataModel getItems() {
        if (items == null) {
            items = new ListDataModel(getFacade().findAll());
        }
        return items;
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
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
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
     * 
     * @return 
     */
    public String prepareHabilitar(){
        current = procedimientoSelected;
        try{
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdminentidad().setFechaModif(date);
            current.getAdminentidad().setUsModif(usLogeado);
            current.getAdminentidad().setHabilitado(true);
            current.getAdminentidad().setUsBaja(null);
            current.getAdminentidad().setFechaBaja(null);

            // Actualizo
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoHabilitado"));
            instVinc = current.getInstanciasXProcedimiento();
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ProcedimientoHabilitadoErrorOccured"));
            return null; 
        }
    }     


    /**
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        iniciado = true;
        asignaInstancia = false;
        recreateModel();
        if(instVinc != null){
            instVinc.clear();
        }
        if(instDisp != null){
            instDisp.clear();
        }
        return "list";
    } 
     /**
     * 
     * @return 
     */
    public String prepareListaDes() {
        recreateModel();
        asignaInstancia = false;
        if(instVinc != null){
            instVinc.clear();
        }
        if(instDisp != null){
            instDisp.clear();
        }
        return "listaDes";
    }  
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        asignaInstancia = false;
        current = procedimientoSelected;
        instVinc = current.getInstanciasXProcedimiento();
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        asignaInstancia = false;
        current = procedimientoSelected;
        instVinc = current.getInstanciasXProcedimiento();
        return "viewDes";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        // cargo la tabla de Instancias
        instancias = instFacade.getHabilitadas();

        current = new Procedimiento();
        return "new";
    }

    public String prepareEdit() {
        //cargo los list para los combos
        current = procedimientoSelected;
        asignaInstancia = true;
        instVinc = current.getInstanciasXProcedimiento();
        instDisp = cargarInstanciasDisponibles();
        return "edit";
    }
    
    public String prepareInicio(){
        recreateModel();
        return "/faces/index";
    }
    
    /**
     * Método para preparar la búsqueda
     * @return la ruta a la vista que muestra los resultados de la consulta en forma de listado
     */
    public String prepareSelect(){
        //items = null;
        return "list";
    }
    
    /**
     * Método que verifica que la Actividad Planificada que se quiere eliminar no esté siento utilizada por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        current = procedimientoSelected;
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoNonDeletable"));
        }
        instVinc = current.getInstanciasXProcedimiento();
        return "view";
    }  
   
    
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
    
    private void validarExistente(Object arg2) throws ValidatorException{
        if(!getFacade().existe((String)arg2)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("CreateProcedimientoExistente")));
        }
    }
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        listProcedimiento.clear();
        listProcedimiento = null;
        if(instVincFilter != null){
            instVincFilter = null;
        }
        if(instDispFilter != null){
            instDispFilter = null;
        }
        if(instanciasFilter != null){
            instanciasFilter = null;
        }
    }      
    /**
     * 
     */
    private List<Instancia> cargarInstanciasDisponibles(){
        List<Instancia> insts = instFacade.getHabilitadas();
        List<Instancia> instsSelect = new ArrayList();
        Iterator itInsts = insts.listIterator();
        while(itInsts.hasNext()){
            Instancia inst = (Instancia)itInsts.next();
            if(!instVinc.contains(inst)){
                instsSelect.add(inst);
            }
        }
        return instsSelect;
    }
    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * @return 
     */
    public String create() {     
        // Creación de la entidad de administración y asignación
        Date date = new Date(System.currentTimeMillis());
        AdminEntidad admEnt = new AdminEntidad();
        admEnt.setFechaAlta(date);
        admEnt.setHabilitado(true);
        admEnt.setUsAlta(usLogeado);
        current.setAdminentidad(admEnt); 
        try {
            if(getFacade().noExiste(current.getNombre(), current.getApp())){
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoCreated"));
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoExistente"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PProcedimientoCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * Método que actualiza una nueva Provincia en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {    
        boolean edito;
        Procedimiento sub;
        try {
            sub = getFacade().getExistente(current.getNombre(), current.getApp());
            if(sub == null){
                edito = true;  
            }else{
                edito = sub.getId().equals(current.getId());
            }
            if(edito){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdminentidad().setFechaModif(date);
                current.getAdminentidad().setUsModif(usLogeado);

                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoUpdated"));
                asignaInstancia = false;
                instDisp.clear();
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("CreateProcedimientoExistente"));
                return null; 
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ProcedimientoUpdatedErrorOccured"));
            return null;
        }
    }

    /**************************
    **    Métodos de selección     **
    **************************/

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Procedimiento getProcedimiento(java.lang.Long id){
        return getFacade().find(id);
    }
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbProcedimiento");

        return "inicio";
    } 
    
     /**
     * Método para mostrar las Actividades Implementadas vinculadas a esta Actividad Planificada
     */
    public void verInstancias(){
        instancias = current.getInstanciasXProcedimiento();
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("", options, null);
    }       

    public void asignarInstancia(Instancia inst){
        instVinc.add(inst);
        instDisp.remove(inst);
        if(instVincFilter != null){
            instVincFilter = null;
        }
        if(instDispFilter != null){
            instDispFilter = null;
        }
    }
    
    public void quitarInstancia(Instancia inst){
        instVinc.remove(inst);
        instDisp.add(inst);
        if(instVincFilter != null){
            instVincFilter = null;
        }
        if(instDispFilter != null){
            instDispFilter = null;
        }
    }
    

    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private ProcedimientoFacade getFacade() {
        return procedimientoFacade;
    }

    private void performDestroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Procedimiento.class)
    public static class ProcedimientoControllerConverter implements Converter {

        /**
         *
         * @param facesContext
         * @param component
         * @param value
         * @return
         */
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbProcedimiento controller = (MbProcedimiento) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbProcedimiento");
            return controller.getProcedimiento(getKey(value));
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

        /**
         *
         * @param facesContext
         * @param component
         * @param object
         * @return
         */
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Procedimiento) {
                Procedimiento o = (Procedimiento) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Procedimiento.class.getName());
            }
        }
    }        

}