package com.cit.abakar.application.ExampleClasses;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "Inspection", id="_id")
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

    @Column(name = "Visit")
    public Visit visit;

    @Column(name = "Equipment")
    public Equipment equipment;

    public List<Malfunctions> malfunctionses(){
        return  getMany(Malfunctions.class, "Inspection");
    }

    public Inspection(){
        super();
    }

    public Inspection(String equipment_id, String visit_id, String fg_availability, String fg_usings,
                      String note, Visit visit, Equipment equipment) {
        this.equipment_id = equipment_id;
        this.visit_id = visit_id;
        this.fg_availability = fg_availability;
        this.fg_usings = fg_usings;
        this.note = note;
        this.visit = visit;
        this.equipment = equipment;
    }
}
