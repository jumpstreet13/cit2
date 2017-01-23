package com.cit.abakar.application.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "Malfunctions", id="_id")
public class Malfunctions extends Model {

   @Column(name = "visit_id")
   public String visit_id;

    @Column(name = "equipment_id")
    public String equipment_id;

    @Column(name = "description")
    public String description;

    @Column(name = "Directory_Equipment_Condition")
    public  Directory_Equipment_Condition directory_equipment_condition;

    @Column(name = "Inspection")
    public Inspection inspection;

    public Malfunctions(){
        super();
    }

    public Malfunctions(String visit_id, String equipment_id, String description,
                        Directory_Equipment_Condition directory_equipment_condition,
                        Inspection inspection) {
        this.visit_id = visit_id;
        this.equipment_id = equipment_id;
        this.description = description;
        this.directory_equipment_condition = directory_equipment_condition;
        this.inspection = inspection;
    }
}
