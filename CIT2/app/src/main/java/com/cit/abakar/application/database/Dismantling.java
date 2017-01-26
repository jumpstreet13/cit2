package com.cit.abakar.application.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "Dismantling")
public class Dismantling extends Model {

    @Column(name = "akt_number", index = true)
    public String akt_number;

    @Column(name = "user")
    public String user;

    @Column(name = "visit_id")
    public String visit_id;

    @Column(name = "equipment_id")
    public String equipment_id;

    @Column(name = "Visit")
    public Visit visit;

    @Column(name = "Equipment")
    public Equipment equipment;

    @Column(name = "visit_id_ot")
    public String visit_id_out;

    @Column(name = "equipment_id_out")
    public String equipment_id_out;

    @Column(name = "id_out")
    public String id_out;


    public Dismantling(){
        super();

    }

    public Dismantling(String akt_number, String user, String visit_id, String equipment_id,
                       Visit visit, Equipment equipment, String visit_id_out,
                       String equipment_id_out, String id_out) {
        this.akt_number = akt_number;
        this.user = user;
        this.visit_id = visit_id;
        this.equipment_id = equipment_id;
        this.visit = visit;
        this.equipment = equipment;
        this.visit_id_out = visit_id_out;
        this.equipment_id_out = equipment_id_out;
        this.id_out = id_out;
    }
}
