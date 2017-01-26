package com.cit.abakar.application.database;


import android.widget.ListView;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "Visit", id="_id")
public class Visit extends Model {

    @Column(name = "center_id", index = true)
    public String center_id;

    @Column(name = "id_out")
    public String id_out;

    @Column(name = "date_visit")
    public String date_visit;
    //kk

    @Column(name = "description")
    public String description;

    @Column(name = "Center")
    public Center center;

    @Column(name = "date_insert")
    public String date_insert;

    public List<Installation> installations(){
        return  getMany(Installation.class, "Visit");
    }

    public List<Dismantling> dismantlings(){
        return  getMany(Dismantling.class, "Visit");
    }

    public List<Inspection> inspections(){
        return getMany(Inspection.class, "Visit");
    }

    public Visit(){
        super();
    }


    public Visit(String center_id, String id_out, String date_visit, String description, Center center, String date_insert) {
        this.center_id = center_id;
        this.id_out = id_out;
        this.date_visit = date_visit;
        this.description = description;
        this.center = center;
        this.date_insert = date_insert;
    }
}
