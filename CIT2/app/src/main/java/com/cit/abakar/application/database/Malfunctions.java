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

    public Malfunctions(){
        super();
    }

    public Malfunctions(String inspection_id, String description) {
        super();
        this.inspection_id = inspection_id;
        this.description = description;
    }
}
