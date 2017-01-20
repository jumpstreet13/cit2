package com.cit.abakar.application.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "Dismantling")
public class Dismantling extends Model {

    @Column(name = "akt_number")
    public String akt_number;

    @Column(name = "user")
    public String user;

    @Column(name = "visit_id")
    public String visit_id;

    @Column(name = "equipment_id")
    public String equipment_id;

    public Dismantling(){
        super();

    }

    public Dismantling(String akt_number, String user, String visit_id, String equipment_id) {
        super();
        this.akt_number = akt_number;
        this.user = user;
        this.visit_id = visit_id;
        this.equipment_id = equipment_id;
    }
}
