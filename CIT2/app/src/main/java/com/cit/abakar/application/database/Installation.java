package com.cit.abakar.application.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Installation")
public class Installation extends Model {

    @Column(name = "akt_number")
    public String akt_number;

    @Column(name = "equipment_id")
    public String equipment_id;

    @Column(name = "visit_id")
    public String visit_id;

    public Installation(){
        super();

    }

    public Installation(String akt_number, String equipment_id, String visit_id) {
        super();
        this.akt_number = akt_number;
        this.equipment_id = equipment_id;
        this.visit_id = visit_id;
    }
}
