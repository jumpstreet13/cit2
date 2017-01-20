package com.cit.abakar.application.database;


import android.widget.ListView;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "Visit")
public class Visit extends Model {

    @Column(name = "center_id", index = true)
    public String center_id;

    @Column(name = "date_visit")
    public String date_visit;

    @Column(name = "description")
    public String description;

    @Column(name = "Center")
    public Center center;

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


    public Visit(String center_id, String date_visit, String description, Center center) {
        this.center_id = center_id;
        this.date_visit = date_visit;
        this.description = description;
        this.center = center;
    }
}
