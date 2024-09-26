package org.switf.telegramexample.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "places")
public class PlaceModel {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Integer id;
    private  String nombre;
    private String descripcion;
    private String direccion;
}
