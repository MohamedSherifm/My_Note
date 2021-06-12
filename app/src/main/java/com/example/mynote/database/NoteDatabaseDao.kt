package com.example.mynote.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface NoteDatabaseDao {
    @Insert
    fun insertUser(user:User)

    @Insert
    fun insertNote(note:Notes)

    @Update
    fun updateNote(note:Notes)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteThisNote(note: Notes)

    @Transaction
    @Query("DELETE FROM notes WHERE noteId= :nId AND noteUserId= :uId")
    fun deleteOneNote(nId: Int, uId: Int)

    @Query("DELETE FROM notes WHERE noteUserId= :uId")
    fun deleteUserNotes(uId: Int)

    @Query("SELECT * FROM notes WHERE noteId= :nId")
    fun selectNote(nId: Int): Notes

    @Transaction
    @Query("SELECT * FROM notes")
    fun getAllNotes():LiveData<List<Notes>>

    @Transaction
    @Query("SELECT * FROM notes WHERE noteUserId= :uId")
    fun getUserNotes(uId: Int): LiveData<List<Notes>>

    @Query("SELECT * FROM users_table WHERE user_name = :userName AND user_password= :userPassword")
    fun selectUSer(userName: String, userPassword:String):User

    @Query("UPDATE users_table SET keep_me_signed = 0")
    fun setKeepMeSignedInToFalse()

    @Query("UPDATE users_table SET keep_me_signed = 1 WHERE user_name = :userName")
    fun setKeepMeSignedInToTrue(userName:String)

    @Query("SELECT * FROM users_table WHERE keep_me_signed = 1")
    fun getSignedInUser():User
}