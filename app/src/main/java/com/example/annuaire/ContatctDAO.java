package com.example.annuaire;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContatctDAO {
    @Query("SELECT * FROM Contact")
    List<Contact> getAll();

    @Insert
    void insert(Contact contact);

    @Query("SELECT * FROM Contact WHERE name LIKE :name")
    List<Contact> findByName(String name);

    @Query("SELECT * FROM Contact WHERE ID=:ID")
    Contact findByID(int ID);

    @Delete
    void delete(Contact contact);

    @Update
    void update(Contact contact);
}
