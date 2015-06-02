/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestiontramites.entidades;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author carmendariz
 */

@Entity
public class Instancia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column (nullable=false, length=100, unique=true)
    @NotNull(message = "El campo nombre no puede quedar nulo")
    @Size(message = "El campo nombre debe tener entre 1 y 100 caracteres", min = 1, max = 100)
    private String nombre;
    
    @Column (nullable=false, length=100, unique=true)
    @NotNull(message = "El campo ruta no puede quedar nulo")
    @Size(message = "El campo nombre debe tener entre 1 y 100 caracteres", min = 1, max = 100)
    private String ruta;
    
    @Column (nullable=false, length=50, unique=true)
    @NotNull(message = "El campo código no puede quedar nulo")
    @Size(message = "El campo nombre debe tener entre 1 y 50 caracteres", min = 1, max = 50)
    private String codigo;
        
    @ManyToOne /*(fetch=FetchType.LAZY)*/
    @JoinColumn(name="procedimiento_id")
    private Procedimiento procedimiento;
    
    private int app;
    
    @ManyToOne /*(fetch=FetchType.LAZY)*/
    @JoinColumn(name="estado_id")
    private Estado estado;    
    
    @ManyToOne /*(fetch=FetchType.LAZY)*/
    @JoinColumn(name="unidadDeTiempo_id")
    private UnidadDeTiempo unidadDeTiempo;
    
    @Column (nullable=false, length=10, unique=true)
    @NotNull(message = "El campo vencimiento no puede quedar nulo")
    @Size(message = "El campo nombre debe tener entre 1 y 10 caracteres", min = 1, max = 10)
    private int cantidadVencimiento;

    @Column (nullable=false, length=10, unique=true)
    @NotNull(message = "El campo alerta no puede quedar nulo")
    @Size(message = "El campo nombre debe tener entre 1 y 10 caracteres", min = 1, max = 10)
    private int cantidadAlerta;
    
    private String observaciones;
    
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="adminentidad_id")
    private AdminEntidad adminentidad;    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Procedimiento getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(Procedimiento procedimiento) {
        this.procedimiento = procedimiento;
    }

    public int getApp() {
        return app;
    }

    public void setApp(int app) {
        this.app = app;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public AdminEntidad getAdminentidad() {
        return adminentidad;
    }

    public void setAdminentidad(AdminEntidad adminentidad) {
        this.adminentidad = adminentidad;
    }
    
    
    
/***************************** para ver con Rubén *******************************/
     public int getCantidadVencimiento() {
        return cantidadVencimiento;
    }

    public void setCantidadVencimiento(int cantidadVencimiento) {
        this.cantidadVencimiento = cantidadVencimiento;
    }

    public int getCantidadAlerta() {
        return cantidadAlerta;
    }

    public void setCantidadAlerta(int cantidadAlerta) {
        this.cantidadAlerta = cantidadAlerta;
    }

    public UnidadDeTiempo getUnidadDeTiempo() {
        return unidadDeTiempo;
    }

    public void setUnidadDeTiempo(UnidadDeTiempo unidadDeTiempo) {
        this.unidadDeTiempo = unidadDeTiempo;
    }



    
/***************************** para ver con Rubén *******************************/
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Instancia)) {
            return false;
        }
        Instancia other = (Instancia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.gob.ambiente.servicios.gestiontramites.entidades.Instancia[ id=" + id + " ]";
    }
    
}
