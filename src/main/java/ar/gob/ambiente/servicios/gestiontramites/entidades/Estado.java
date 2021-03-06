/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestiontramites.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author carmendariz
 */
@Entity
public class Estado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column (nullable=false, length=50, unique=true)
    @NotNull(message = "El campo nombre no puede quedar nulo")
    @Size(message = "El campo nombre debe tener entre 1 y 50 caracteres", min = 1, max = 50)
    private String nombre;
    
    private int app;
    
    @OneToMany(mappedBy="estadoInicial")
    private List<Instancia> instanciasEstInicial;
     
    @OneToMany(mappedBy="estadoFinal")
    private List<Instancia> instanciasEstFinal;   
    
    
    public Estado(){
        instanciasEstInicial = new ArrayList<>();
        instanciasEstFinal = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getApp() {
        return app;
    }

    public void setApp(int app) {
        this.app = app;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Instancia> getInstanciasEstInicial() {
        return instanciasEstInicial;
    }

    public void setInstanciasEstInicial(List<Instancia> instanciasEstInicial) {
        this.instanciasEstInicial = instanciasEstInicial;
    }

    public List<Instancia> getInstanciasEstFinal() {
        return instanciasEstFinal;
    }

    public void setInstanciasEstFinal(List<Instancia> instanciasEstFinal) {
        this.instanciasEstFinal = instanciasEstFinal;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estado)) {
            return false;
        }
        Estado other = (Estado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.gob.ambiente.servicios.gestiontramites.entidades.Estado[ id=" + id + " ]";
    }
    
}

