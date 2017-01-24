package com.cit.abakar.application.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;


@Table(name = "Equipment")
public class Equipment extends Model {

    @Column(name = "center_id")
    public String center_id;

    @Column(name = "serial_number", index = true)
    public String serial_number;

    @Column(name = "inventory_number", index = true)
    public String inventory_number;

    @Column(name = "name")
    public String name;

    @Column(name = "fg_dismantled")
    public String fg_dismantled;

    @Column(name = "fg_not_install")
    public String fg_not_install;

    @Column(name = "Center")
    public Center center;

    public List<Dismantling> dismantlings(){
        return  getMany(Dismantling.class, "Equipment");
    }

    public List<Installation> installations(){
        return  getMany(Installation.class, "Equipment");
    }

    public List<Inspection> inspections(){
        return  getMany(Inspection.class, "Equipment");
    }

    public static List<Equipment> getAll(){
        return new Select().from(Equipment.class).execute();
    }

    public static Equipment getById(String id){
        return  new Select().from(Equipment.class).where("id = ?", id).executeSingle();
    }

    public Equipment(){
        super();

    }

    public Equipment(String center_id, String serial_number, String inventory_number,
                     String name, String fg_dismantled, String fg_not_install,
                     Center center) {
        this.center_id = center_id;
        this.serial_number = serial_number;
        this.inventory_number = inventory_number;
        this.name = name;
        this.fg_dismantled = fg_dismantled;
        this.fg_not_install = fg_not_install;
        this.center = center;
    }
}
