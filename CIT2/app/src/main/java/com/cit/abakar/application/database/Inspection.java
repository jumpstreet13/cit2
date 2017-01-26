package com.cit.abakar.application.database;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "Inspection")
public class Inspection extends Model {

    @Column(name = "equipment_id")
    public String equipment_id;

    @Column(name = "visit_id")
    public String visit_id;

    @Column(name = "id_out")
    public String id_out;

    @Column(name = "visit_id_out")
    public String visit_id_out;

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

    public List<Malfunctions> malfunctionses() {
        return getMany(Malfunctions.class, "Inspection");
    }

    public Inspection() {
        super();
    }

    public Inspection(String equipment_id, String visit_id, String id_out, String visit_id_out,
                      String fg_availability, String fg_usings, String note, Visit visit,
                      Equipment equipment) {
        this.equipment_id = equipment_id;
        this.visit_id = visit_id;
        this.id_out = id_out;
        this.visit_id_out = visit_id_out;
        this.fg_availability = fg_availability;
        this.fg_usings = fg_usings;
        this.note = note;
        this.visit = visit;
        this.equipment = equipment;
    }
}
