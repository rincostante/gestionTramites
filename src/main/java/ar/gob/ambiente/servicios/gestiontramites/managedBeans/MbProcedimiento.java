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
import ar.gob.ambiente.servicios.gestiontramites.util.JsfUtil;
import ar.gob.ambiente.servicios.gestiontramites.facades.EstadoFacade;
import ar.gob.ambiente.servicios.gestiontramites.facades.InstanciaFacade;
import ar.gob.ambiente.servicios.gestiontramites.facades.ProcedimientoFacade;
import ar.gob.ambiente.servicios.gestiontramites.facades.UnidadDeTiempoFacade;
import ar.gob.ambiente.servicios.gestiontramites.wsExt.AccesoAppWebService;
import ar.gob.ambiente.servicios.gestiontramites.wsExt.AccesoAppWebService_Service;
import ar.gob.ambiente.servicios.gestiontramites.wsExt.Aplicacion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;



/**
 *
 * @author carmendariz
 */
public class MbProcedimiento implements Serializable{
    
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/AccesoAppWebService/AccesoAppWebService.wsdl")
    private AccesoAppWebService_Service service;        

    private Procedimiento current;
    private Instancia instancia;
    private Aplicacion app;

    private List<Instancia> instancias;
    private List<Instancia> instanciasFilter;
    private List<Instancia> listInstancias;
    private List<Procedimiento> listProcedimiento;  


    @EJB
    private ProcedimientoFacade procedimientoFacade;
    @EJB
    private EstadoFacade estadoFacade;
    @EJB
    private UnidadDeTiempoFacade unidadDeTiempoFacade;
    @EJB
    private InstanciaFacade instFacade;
    
    private Procedimiento procedimientoSelected;
    private Instancia instSelected;
    private Usuario usLogeado;
    private MbLogin login;   

    private boolean iniciado;
    private int update; // 0=updateNormal | 1=deshabiliar | 2=habilitar
    private List<UnidadDeTiempo> listaUnidadDeTiempos;
  
    private List<Estado> listaEstados;
    private List<Aplicacion> listApp;
    private static final Logger logger = Logger.getLogger(Procedimiento.class.getName());  



    /**
     * Creates a new instance of MbProcedimiento
     */
    public MbProcedimiento() {
    }
  
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
                    if(!s.equals("mbProcedimiento") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }    

    /********************************
     ****** Getters y Setters *******
     ********************************
    /*
    * @return 
    */
    public Aplicacion getApp() {
        return app;
    }

    public void setApp(Aplicacion app) {
        this.app = app;
    }

    public List<Aplicacion> getListApp() {
        return listApp;
    }

    public void setListApp(List<Aplicacion> listApp) {
        this.listApp = listApp;
    }
    
    public Instancia getInstancia() {
        return instancia;
    }

    public void setInstancia(Instancia instancia) {
        this.instancia = instancia;
    }

    public List<Instancia> getInstanciasFilter() {
        return instanciasFilter;
    }

    public void setInstanciasFilter(List<Instancia> instanciasFilter) {
        this.instanciasFilter = instanciasFilter;
    }

    public List<Procedimiento> getListProcedimiento() {
        if(listProcedimiento == null){
            listProcedimiento = getFacade().findAll();
        }
        return listProcedimiento;
    }
    
    public void setListProcedimiento(List<Procedimiento> listProcedimiento) {
        this.listProcedimiento = listProcedimiento;
    }

    public List<Instancia> getInstancias() {
        return instancias;
    }

    public void setInstancias(List<Instancia> instancias) {
        this.instancias = instancias;
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

    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }

    public Procedimiento getCurrent() {
        return current;
    }

    public void setCurrent(Procedimiento current) {
        this.current = current;
    }
    public List<UnidadDeTiempo> getListaUnidadDeTiempos() {
        return listaUnidadDeTiempos;
    }

    public void setListaUnidadDeTiempos(List<UnidadDeTiempo> listaUnidadDeTiempos) {
        this.listaUnidadDeTiempos = listaUnidadDeTiempos;
    }

    public List<Estado> getListaEstados() {
        return listaEstados;
    }

    public void setListaEstados(List<Estado> listaEstados) {
        this.listaEstados = listaEstados;
    }


    public Instancia getInstSelected() {
        return instSelected;
    }

    public void setInstSelected(Instancia instSelected) {
        this.instSelected = instSelected;
    }

    public InstanciaFacade getInstFacade() {
        return instFacade;
    }
  
 
    /********************************
     ** Métodos para el datamodel **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public Procedimiento getSelected() {
        if (current == null) {
            current = new Procedimiento();
        }
        return current;
    }    
 
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
     * Método para inicializar el listado de los Actividades Planificadass habilitadas
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        iniciado = true;
        recreateModel();
        return "list";
    } 
     
   
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        listInstancias = current.getInstancias();
        return "view";
    }

     /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        // instanciamos el current
        current = new Procedimiento();
        
        // instancio la variable aplicación
        if(app == null){
            app = new Aplicacion();
        }
        
        // inicializamos la creación de instancias
        listInstancias = new ArrayList();
        instancia = new Instancia();
        listaUnidadDeTiempos = unidadDeTiempoFacade.findAll();
        listaEstados = estadoFacade.getEstadosXapp(app.getId());
        listApp = verAplicaciones();
        
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        // inicializamos el objeto instancia para asignar nuevas al procedimiento que se está editando
        instancia = new Instancia();
        
        // instancio la variable aplicación
        if(app == null){
            app = new Aplicacion();
        }        
        
        // pueblo los combos
        listaEstados = estadoFacade.getEstadosXapp(app.getId());
        listaUnidadDeTiempos = unidadDeTiempoFacade.findAll();
        listApp = verAplicaciones();
        
        return "edit";
    }
    
    /**
     *
     * @return
     */
    public String prepareInicio(){
        recreateModel();
        return "/faces/index";
    }
    
    /**
     * Método que permite habilitar y deshabilitar el Procedimiento
     */

    public void habilitar(){
        update = 2;
        update(); 
        recreateModel();
    }

    public void deshabilitar(){
        update = 1;
        update();   
        recreateModel();
    }  
    
  
    /*************************
    ** Métodos de operación **
    **************************/
    
    public void agregarInstancias(){
        if(app.getId() > 0){
            listaEstados = estadoFacade.getEstadosXapp(app.getId());
            Map<String,Object> options = new HashMap<>();
            options.put("contentWidth", 1200);
            RequestContext.getCurrentInstance().openDialog("dlgAddInstancias", options, null); 
            
            
        }else{
            Map<String,Object> options = new HashMap<>();
            options.put("contentWidth", 400);
            options.put("contentHeight", 100);
            RequestContext.getCurrentInstance().openDialog("dlgFaltaApp", options, null);     
        }

    }
        
    public void editarInstancias(){
            listaEstados = estadoFacade.getEstadosXapp(app.getId());
            Map<String,Object> options = new HashMap<>();
            options.put("contentWidth", 1200);
            RequestContext.getCurrentInstance().openDialog("dlgEditInstancias", options, null);
    }
    
    
    /**
     * Método para guardar las instancias creadas en el listInstancias que irán en el nuevo procedimiento
     */
    public void createInstancia(){
        if(!compararInstancia(instancia)){ 

            // Si estoy creanto un procedimiento nuevo, agrego la instancia al list
            // Si no se la agrego a la propiedad instancias del procedimiento
             
            if(current.getId() != null){

                current.getInstancias().add(instancia);
                // se agregan los datos del AdminEntidad
                Date date = new Date(System.currentTimeMillis());
                AdminEntidad admEnt = new AdminEntidad();
                admEnt.setFechaAlta(date);
                admEnt.setHabilitado(true);
                admEnt.setUsAlta(usLogeado);
                current.setAdminentidad(admEnt);
            }else{
                // se agregan los datos del AdminEntidad
                Date date = new Date(System.currentTimeMillis());
                AdminEntidad admEnt = new AdminEntidad();
                admEnt.setFechaAlta(date);
                admEnt.setHabilitado(true);
                admEnt.setUsAlta(usLogeado);
                instancia.setAdminentidad(admEnt);
                listInstancias.add(instancia);    
            }
            // reseteo la instancia
            instancia = null;
            instancia = new Instancia();
        } else{
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("InstanciaExistente"));
        }
    }
    
    /**
     * Método que inserta una nueva instancia en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna al procedimiento
     * @return mensaje que notifica la inserción
     */
    public String create() {
        // Creación de la entidad de administración y asignación
        Date date = new Date(System.currentTimeMillis());
        AdminEntidad admEnt = new AdminEntidad();
        admEnt.setFechaAlta(date);
        admEnt.setHabilitado(true);
        admEnt.setUsAlta(usLogeado);
        current.setAdminentidad(admEnt);
        
        // asgino la aplicación al procedimiento
        current.setApp(app.getId());

        // asigno las instancias al procedimiento
        current.setInstancias(listInstancias);
        listApp.clear();
        app = null;
        
        if(current.getNombre().isEmpty()){
            JsfUtil.addSuccessMessage("El procedimiento que está guardando debe tener un nombre.");
            return null;
        }else{
            try {
                if(getFacade().noExiste(current.getNombre(), current.getApp())){

                    // Inserción
                    getFacade().create(current);

                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoCreated"));
                    return "view";

                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("CreateProcedimientoExistente"));
                    return null;
                }
            } 
            catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ProcedimientoCreatedErrorOccured"));
                return null;
            }
        }
    }

    /**
     * Método que actualiza una nueva Instancia en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
   public String update() {    
        boolean edito;
        Procedimiento pro;
        Date date = new Date(System.currentTimeMillis());
        

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
        
        // acualizo según la operación seleccionada
        try {
            if(update == 0){
                pro = getFacade().getExistente(current.getNombre(), current.getApp());
                if(pro == null){
                    edito = true;  
                }else{
                    edito = pro.getId().equals(current.getId());
                }
                if(edito){
                    // Actualización de datos de administración de la entidad
                    current.getAdminentidad().setFechaModif(date);
                    current.getAdminentidad().setUsModif(usLogeado);
                   
                    // asgino la aplicación al procedimiento
                    current.setApp(app.getId());

                    // Actualizo
                    getFacade().edit(current);
                    listApp.clear();
                    app = null;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Procedimiento", "Ha sido actualizado"));
                    return "view";
                }else{
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Procedimiento", "Ya Existe"));
                    return null; 
                    }
                
            }else if(update == 1){
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoDeshabilitado"));
                return "view";
            }else{
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoHabilitado"));
                return "view";
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Procedimiento", "Existe un error en la edición"));
            return null;
        }
    }
    
   
    /*************************
    ** Métodos de selección **
    **************************/

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Procedimiento getProcedimiento(java.lang.Long id) {
        return getFacade().find(id);
    }  

    /**
     * Método para mostrar las Instanciass vinculadas
     */
    public void verInstancias(){
        instancias = current.getInstancias();
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("", options, null);
    }         
 
    /**
     * Método para editar desde la tabla con sólo pararse en el campo a editar
     * @param event
     */
    
    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Instancia editada", ((Instancia) event.getObject()).getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Instancia) event.getObject()).getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    /**
     * Método para eliminar promociones
     * @param event
     */
    public void instDelete(RowEditEvent event){
        try{
            current.getInstancias().remove((Instancia)event.getObject());
            //getInstFacade().remove((Instancia)event.getObject());
            JsfUtil.addSuccessMessage("Instancia eliminada.");
        }catch(Exception e){
            JsfUtil.addErrorMessage("Hubo un error eliminando la Instancia. " + e.getMessage());
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

    /**
     * Restea la entidad
     */
    private void recreateModel() {
        if(listApp != null){
            listApp.clear();
            listApp = null;
        }
        if(app != null){
            app = null;
        }
        listProcedimiento.clear();
        listProcedimiento = null;
        if(listInstancias != null){
            listInstancias.clear();
            listInstancias = null;
        }
    }      
    
    /**
     * Método para validar si una instacia ya existe en el list que las guarda en memoria
     */
    private boolean compararInstancia(Instancia inst){
        boolean retorno = false;
        Iterator instIt;
        
        // Si estoy creando un procedimiento nuevo, uso el iterator del listInstancias
        // Si no, lo uso del current.getInstancias
        if(current.getId() != null){
            instIt = current.getInstancias().iterator();
        }else{
            instIt = listInstancias.iterator(); 
        }
        
        while(instIt.hasNext()){
            Instancia instancia = (Instancia)instIt.next();
            if(instancia.getNombre().equals(inst.getNombre())
                    && instancia.getEstadoInicial().equals(inst.getEstadoInicial())
                    && instancia.getEstadoFinal().equals(inst.getEstadoFinal())){
                retorno = true;
            }
        }
        return retorno;
    }
    
    private List<Aplicacion> verAplicaciones() {
        List<Aplicacion> result;
        try{
            AccesoAppWebService port = service.getAccesoAppWebServicePort();
            return port.verAplicaciones();
        }catch(Exception ex){
            result = null;
            // muestro un mensaje al usuario
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioAppErrorWs"));
            // lo escribo en el log del server
            logger.log(Level.SEVERE, "{0} - {1}", new Object[]{ResourceBundle.getBundle("/Bundle").getString("UsuarioAppErrorWs"), ex.getMessage()});
        }
        return result;
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


