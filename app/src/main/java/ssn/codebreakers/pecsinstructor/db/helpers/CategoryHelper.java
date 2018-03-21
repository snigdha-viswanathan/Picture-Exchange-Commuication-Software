package ssn.codebreakers.pecsinstructor.db.helpers;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.LocalDatabase;
import ssn.codebreakers.pecsinstructor.db.models.Category;

public class CategoryHelper
{
    public static void addCategory(Context context, Category category)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        localDatabase.categoryDao().addCategory(category);
        localDatabase.close();
    }

    public static Category getCategory(Context context, String categoryId)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        Category category = localDatabase.categoryDao().get(categoryId);
        localDatabase.close();
        return category;
    }

    public static List<Category> getAllCategories(Context context)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        List<Category> categories = localDatabase.categoryDao().getAllCategories();
        localDatabase.close();
        return categories;
    }

    public static void deleteCategory(Context context, Category category)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        localDatabase.categoryDao().deleteCategory(category);
        localDatabase.close();
    }
}
