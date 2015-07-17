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
import ar.gob.ambiente.servicios.gestiontramites.facades.ProcedimientoFacade;
import ar.gob.ambiente.servicios.gestiontramites.facades.UnidadDeTiempoFacade;
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
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author carmendariz
 */
public class MbProcedimiento implements Serializable{

    private Procedimiento current;
    private Instancia instancia;

    private List<Instancia> instancias;
    private List<Instancia> instanciasFilter;
    private List<Instancia> listInstancias;
    private List<Procedimiento> listProcedimiento;  
    private List<Instancia> instVinc;
   

    @EJB
    private ProcedimientoFacade procedimientoFacade;
    @EJB
    private EstadoFacade estadoFacade;
    @EJB
    private UnidadDeTiempoFacade unidadDeTiempoFacade;
    @EJB
    private InstanciaFacade instFacade;
    
    private Procedimiento procedimientoSelected;
    private Usuario usLogeado;
    private MbLogin login;   

    private boolean iniciado;
    //private int update; // 0=updateNormal | 1=deshabiliar | 2=habilitar
    private List<UnidadDeTiempo> listaUnidadDeTiempos;
    private List<UnidadDeTiempo> listaUnidadDeTiemposAlerta;
    private List<UnidadDeTiempo> listaUnidadDeTiemposVto;
    private List<Estado> listaEstadosIniciales;
    private List<Estado> listaEstadosFinales;


    



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
     ********************************/
    
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

    public List<Instancia> getInstVinc() {
        return instVinc;
    }

    public void setInstVinc(List<Instancia> instVinc) {
        this.instVinc = instVinc;
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
        instVinc = current.getInstancias();
        return "view";
    }


     /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        // instanciamos el current
        current = new Procedimiento();

        // inicializamos la creación de instancias
        listInstancias = new ArrayList();
        instancia = new Instancia();
        listaEstadosIniciales = estadoFacade.findAll();
        listaEstadosFinales = estadoFacade.findAll();
        listaUnidadDeTiemposAlerta = unidadDeTiempoFacade.findAll();
        listaUnidadDeTiemposVto = unidadDeTiempoFacade.findAll();  
        
        return "new";

    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        // inicializamos el objeto instancia para asignar nuevas al procedimiento que se está editando
        instancia = new Instancia();
        
        // pueblo los combos
        listaEstadosIniciales = estadoFacade.findAll();
        listaEstadosFinales = estadoFacade.findAll();
        listaUnidadDeTiemposAlerta = unidadDeTiempoFacade.findAll();
        listaUnidadDeTiemposVto = unidadDeTiempoFacade.findAll();  
        
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
     * Método que verifica que la Actividad Planificada que se quiere eliminar no esté siento utilizada por otra entidad
     * @return 
     */
    public String prepareDestroy(){
       // current = procedimientoSelected;
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            performDestroyInstancia();
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoNonDeletable"));
        }
        instVinc = current.getInstancias();
        return "view";
    }  
    
    /**
     * 
     * @return 
     */
    public String prepareHabilitar(){
       // current = procedimientoSelected;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoHabilitada"));
            instVinc = current.getInstancias();
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ProcedimientoHabilitadaErrorOccured"));
            return null; 
        }
    }  

    /*************************
    ** Métodos de operación **
    **************************/
    
    /**
     * Método para guardar las instancias creadas en el listInstancias que irán en el nuevo procedimiento
     */
    public void createInstancia(){
        if(!compararInstancia(instancia)){
            // se agregan los datos del AdminEntidad
            Date date = new Date(System.currentTimeMillis());
            AdminEntidad admEnt = new AdminEntidad();
            admEnt.setFechaAlta(date);
            admEnt.setHabilitado(true);
            admEnt.setUsAlta(usLogeado);
            current.setAdminentidad(admEnt);
            
            // Si estoy creanto un procedimiento nuevo, agrego la instancia al list
            // Si no se la agrego a la propiedad instancias del procidimiento
            if(current.getId() != null){
                current.getInstancias().add(instancia);
            }else{
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

        // asigno las instancias al procedimiento
 //       Instancia inst = new Instancia();
 //       inst.setNombre("pipo");
 //       inst.setCodigo("codPipo");
 //       inst.setRuta("rutaPipo");
 //       listInstancias.add(inst);
        current.setInstancias(listInstancias);
        
        if(current.getNombre().isEmpty()){
            JsfUtil.addSuccessMessage("El procedimiento que está guardando debe tener un nombre.");
            return null;
        }else{
            try {
                if(getFacade().noExiste(current.getNombre(), current.getApp())){

                    // Inserción
                    getFacade().create(current);

                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoCreated"));
                   // recreateModel();
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
        try {
            pro = getFacade().getExistente(current.getNombre(), current.getApp());
            if(pro == null){
                edito = true;  
            }else{
                edito = pro.getId().equals(current.getId());
            }
            if(edito){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdminentidad().setFechaModif(date);
                current.getAdminentidad().setUsModif(usLogeado);
               // current.getInstancias().set(Instancia, instancia);

                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoUpdated"));

                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoExistente"));
                return null; 
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ProcedimientoUpdatedErrorOccured"));
            return null;
        }
    }
    
    /**
     * @return mensaje que notifica el borrado
     */    
//    public String destroyInstancia(Instancia inst) {
//        current = instanciaSelected;
//        performDestroyInstancia();
//        recreateModel();
//        return "view";
//    }     
    public void destroyInstancia(Instancia inst){
        instVinc.remove(inst);
    }
    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        performDestroy();
        recreateModel();
        return "view";
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
        instancias = current.getInstancias();
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("", options, null);
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
      
    private void validarInstanciaExistente(Object arg2) throws ValidatorException{
        if(!getFacade().noExisteInstancia((String)arg2)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("CreateInstanciaExistente")));
        }
    } 
    
    private void validarExistente(Object arg2) throws ValidatorException{
        if(!getFacade().noExiste((String)arg2)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("CreateProcedimientoExistente")));
        }
    }     
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        listProcedimiento.clear();
        listProcedimiento = null;
        if(listInstancias != null){
            listInstancias.clear();
            listInstancias =null;
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
      /**
     * Opera el borrado de la instancia
     */
    private void performDestroyInstancia() {
        try {
            getFacade().remove(instancia);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("InstanciaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("InstanciaDeletedErrorOccured"));
        }
    }  
    
    /**
     * Método que deshabilita la entidad
     */
    private void performDestroy() {
        try {
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdminentidad().setFechaBaja(date);
            current.getAdminentidad().setUsBaja(usLogeado);
            current.getAdminentidad().setHabilitado(false);
            
            // Deshabilito la entidad
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProcedimientoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ProcedimientoDeletedErrorOccured"));
        }
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

