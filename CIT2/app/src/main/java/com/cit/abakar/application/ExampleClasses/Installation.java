package com.cit.abakar.application.ExampleClasses;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Installation")
public class Installation extends Model {

    @Column(name = "akt_number", index = true)
    public String akt_number;

    @Column(name = "equipment_id")
    public String equipment_id;

    @Column(name = "visit_id")
    public String visit_id;

    @Column(name = "Visit")
    public Visit visit;

    @Column(name = "Equipment")
    public Equipment equipment;

    public Installation(){
        super();

    }

    public Installation(String akt_number, String equipment_id, String visit_id,
                        Visit visit, Equipment equipment) {
        this.akt_number = akt_number;
        this.equipment_id = equipment_id;
        this.visit_id = visit_id;
        this.visit = visit;
        this.equipment = equipment;
    }
}
