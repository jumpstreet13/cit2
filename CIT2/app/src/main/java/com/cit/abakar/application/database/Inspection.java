package com.cit.abakar.application.database;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Inspection")
public class Inspection extends Model {

    @Column(name = "equipment_id")
    public String equipment_id;

    @Column(name = "visit_id")
    public String visit_id;

    @Column(name = "fg_availability")
    public String fg_availability;

    @Column(name = "fg_usings")
    public String fg_usings;

    @Column(name = "note")
    public String note;

    public Inspection(){
        super();

    }

    public Inspection(String equipment_id, String visit_id, String fg_availability, String fg_usings, String note) {
        super();
        this.equipment_id = equipment_id;
        this.visit_id = visit_id;
        this.fg_availability = fg_availability;
        this.fg_usings = fg_usings;
        this.note = note;
    }
}
