package com.cit.abakar.application.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "Malfunctions")
public class Malfunctions extends Model {

    @Column(name = "inspection_id")
    public String inspection_id;

    @Column(name = "description")
    public String description;

    @Column(name = "Directory_Equipment_Condition")
    public  Directory_Equipment_Condition directory_equipment_condition;

    @Column(name = "Inspection")
    public Inspection inspection;

    public Malfunctions(){
        super();
    }

    public Malfunctions(String inspection_id, String description,
                        Directory_Equipment_Condition directory_equipment_condition,
                        Inspection inspection) {
        this.inspection_id = inspection_id;
        this.description = description;
        this.directory_equipment_condition = directory_equipment_condition;
        this.inspection = inspection;
    }
}
