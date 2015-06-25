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
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author carmendariz
 */
public class MbInstancia implements Serializable{
    
    private Instancia current;
    private DataModel items =null;
    private List<Instancia> listFilter;
    private List<Instancia> listado = null;    

    private List<Estado> listaEstados;
    private List<UnidadDeTiempo> listaUnidadDeTiempos;
    private List<Procedimiento> listaProcedimientos;
    
    @EJB
    private ProcedimientoFacade procFacade;
    
    @EJB
    private UnidadDeTiempoFacade unidadDeTiempoFacade;
    
    @EJB
    private EstadoFacade estadoFacade;
    
    @EJB
    private InstanciaFacade instanciaFacade;
    
    private Estado selectedEstado;
    private UnidadDeTiempo selectedUnidadDeTiempo;
    private Procedimiento selectedProcedimiento;    
    private List<UnidadDeTiempo> listaUnidadDeTiemposAlerta;
    private List<UnidadDeTiempo> listaUnidadDeTiemposVto;
    private List<Estado> listaEstadosIniciales;
    private List<Estado> listaEstadosFinales;

    private List<Procedimiento> listProFilter;

   
    private Usuario usLogeado;
    private boolean iniciado;  
    private int update; // 0=updateNormal | 1=deshabiliar | 2=habilitar
    private MbLogin login;
    
    /**
     * Creates a new instance of MbInstancia
     */
    public MbInstancia() {
    }

    public Instancia getCurrent() {
        return current;
    }

    public void setCurrent(Instancia current) {
        this.current = current;
    }

    public Procedimiento getSelectedProcedimiento() {
        return selectedProcedimiento;
    }

    public void setSelectedProcedimiento(Procedimiento selectedProcedimiento) {
        this.selectedProcedimiento = selectedProcedimiento;
    }

    public Estado getSelectedEstado() {
        return selectedEstado;
    }

    public void setSelectedEstado(Estado selectedEstado) {
        this.selectedEstado = selectedEstado;
    }

    public UnidadDeTiempo getSelectedUnidadDeTiempo() {
        return selectedUnidadDeTiempo;
    }

    public void setSelectedUnidadDeTiempo(UnidadDeTiempo selectedUnidadDeTiempo) {
        this.selectedUnidadDeTiempo = selectedUnidadDeTiempo;
    }

    public List<Instancia> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<Instancia> listFilter) {
        this.listFilter = listFilter;
    }


    public List<UnidadDeTiempo> getListaUnidadDeTiemposAlerta() {
        return listaUnidadDeTiemposAlerta;
    }

    public void setListaUnidadDeTiemposAlerta(List<UnidadDeTiempo> listaUnidadDeTiemposAlerta) {
        this.listaUnidadDeTiemposAlerta = listaUnidadDeTiemposAlerta;
    }

    public List<UnidadDeTiempo> getListaUnidadDeTiemposVto() {
        return listaUnidadDeTiemposVto;
    }

    public void setListaUnidadDeTiemposVto(List<UnidadDeTiempo> listaUnidadDeTiemposVto) {
        this.listaUnidadDeTiemposVto = listaUnidadDeTiemposVto;
    }

    public List<UnidadDeTiempo> getListaUnidadDeTiempos() {
        return listaUnidadDeTiempos;
    }

    public void setListaUnidadDeTiempos(List<UnidadDeTiempo> listaUnidadDeTiempos) {
        this.listaUnidadDeTiempos = listaUnidadDeTiempos;
    }

    public List<Estado> getListaEstadosIniciales() {
        return listaEstadosIniciales;
    }

    public void setListaEstadosIniciales(List<Estado> listaEstadosIniciales) {
        this.listaEstadosIniciales = listaEstadosIniciales;
    }

    public List<Estado> getListaEstadosFinales() {
        return listaEstadosFinales;
    }

    public void setListaEstadosFinales(List<Estado> listaEstadosFinales) {
        this.listaEstadosFinales = listaEstadosFinales;
    }


    public List<Instancia> getListado() {
        return listado;
    }

    public void setListado(List<Instancia> listado) {
        this.listado = listado;
    }

    public List<Procedimiento> getListProFilter() {
        return listProFilter;
    }

    public void setListInsFilter(List<Procedimiento> listProFilter) {
        this.listProFilter = listProFilter;
    }

    public List<Procedimiento> getListaProcedimientos() {
        return listaProcedimientos;
    }

    public void setListaProcedimientos(List<Procedimiento> listaProcedimientos) {
        this.listaProcedimientos = listaProcedimientos;
    }

    public List<Estado> getListaEstados() {
        return listaEstados;
    }

    public void setListaEstados(List<Estado> listaEstados) {
        this.listaEstados = listaEstados;
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
        listaEstadosIniciales = estadoFacade.findAll();
        listaEstadosFinales = estadoFacade.findAll();
        listaUnidadDeTiemposAlerta = unidadDeTiempoFacade.findAll();
        listaUnidadDeTiemposVto = unidadDeTiempoFacade.findAll();   

        
        current = new Instancia();
        return "new";
    }
    
   /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        listaEstadosIniciales = estadoFacade.findAll();
        listaEstadosFinales = estadoFacade.findAll();
        listaUnidadDeTiemposAlerta = unidadDeTiempoFacade.findAll();
        listaUnidadDeTiemposVto = unidadDeTiempoFacade.findAll();    
        listaProcedimientos = procFacade.findAll();
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
        return "list";
    }
    
    /**
     * Método que verifica que el Cargo que se quiere eliminar no esté siento utilizado por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        boolean libre = getFacade().tieneDependencias(current.getId());

        if (libre){
            // Elimina
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("InstanciaNonDeletable"));
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
 
 

   
    
    /*******************************
    ** Métodos para la navegación **
    ********************************/
    /**
     * @return La entidad gestionada
     */
 
    public Instancia getSelected() {
        if (current == null) {
            current = new Instancia();
            //selectedItemIndex = -1;
        }
        return current;
    } 

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Instancia getInstancia(java.lang.Long id) {
       return instanciaFacade.find(id);
    }      
    
    public DataModel getItems() {
        if (items == null) {
            items = new ListDataModel(getFacade().findAll());
        }
        return items;
    }    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private InstanciaFacade getFacade() {
        return instanciaFacade;
    }

    
    private void validarExistente(Object arg2) throws ValidatorException{
        if(!getFacade().noExiste((String)arg2)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("CreateInstanciaExistente")));
        }
    }       
    
    /**
     * Opera el borrado de la entidad
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ProcedimientoDeletedErrorOccured"));
        }
    }      

     /**
     * @return mensaje que notifica la actualizacion de estado
     */     
   public void habilitar() {
        update = 2;
        update();        
        recreateModel();
    } 

    /**
     * @return mensaje que notifica la actualizacion de estado
     */    
    public void deshabilitar() {
       if (getFacade().tieneDependencias(current.getId())){
          update = 1;
          update();        
          recreateModel();
       } 
        else{
            //No Deshabilita 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("InstanciaNonDeletable"));            
        }
    } 

    private void recreateModel() {
        items = null;
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
        try{    
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("InstanciaCreated"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("InstanciaCreatedErrorOccured"));
            return null;
        }
    }
    
     /**
     * Método que actualiza una nueva Provincia en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {
        Date date = new Date(System.currentTimeMillis());
        //Date dateBaja = new Date();
        
        // actualizamos según el valor de update
        if(update == 1){
            current.getAdminentidad().setFechaBaja(date);
            current.getAdminentidad().setUsBaja(usLogeado);
            current.getAdminentidad().setHabilitado(false);
        }
        if(update == 2){
            current.getAdminentidad().setFechaModif(date);
            current.getAdminentidad().setUsModif(usLogeado);
            current.getAdminentidad().setHabilitado(true);
            current.getAdminentidad().setFechaBaja(null);
            current.getAdminentidad().setUsBaja(usLogeado);
        }
        if(update == 0){
            current.getAdminentidad().setFechaModif(date);
            current.getAdminentidad().setUsModif(usLogeado);
        }

        // acualizo
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("InstanciaUpdated"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("InstanciaUpdatedErrorOccured"));
            return null;
        }
    } 
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Instancia.class)
    public static class InstanciaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbInstancia controller = (MbInstancia) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbInstancia");
            return controller.getInstancia(getKey(value));
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
            if (object instanceof Instancia) {
                Instancia o = (Instancia) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Instancia.class.getName());
            }
        }
    }        
}

