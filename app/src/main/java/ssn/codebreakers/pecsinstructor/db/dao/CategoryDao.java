package ssn.codebreakers.pecsinstructor.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.models.Category;

@Dao
public interface CategoryDao
{
    @Insert
    long addCategory(Category category);

    @Query("SELECT * FROM Category")
    List<Category> getAllCategories();

    @Query("SELECT * FROM Category where id = :id")
    Category get(String id);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);
}
