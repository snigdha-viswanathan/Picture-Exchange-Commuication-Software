package ssn.codebreakers.pecsinstructor.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ssn.codebreakers.pecsinstructor.db.dao.CardDao;
import ssn.codebreakers.pecsinstructor.db.dao.CategoryDao;
import ssn.codebreakers.pecsinstructor.db.dao.MessageDao;
import ssn.codebreakers.pecsinstructor.db.dao.SimpleMessageDao;
import ssn.codebreakers.pecsinstructor.db.dao.UserDao;
import ssn.codebreakers.pecsinstructor.db.dao.VideoMessageDao;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.db.models.Message;
import ssn.codebreakers.pecsinstructor.db.models.SimpleMessage;
import ssn.codebreakers.pecsinstructor.db.models.User;
import ssn.codebreakers.pecsinstructor.db.models.VideoMessage;

@Database(entities = {User.class, Card.class, Category.class, Message.class, SimpleMessage.class, VideoMessage.class}, version = 5)
public abstract class LocalDatabase extends RoomDatabase
{
    public abstract UserDao userDao();
    public abstract CardDao cardDao();
    public abstract CategoryDao categoryDao();
    public abstract MessageDao messageDao();
    public abstract SimpleMessageDao simpleMessageDao();
    public abstract VideoMessageDao videoMessageDao();
}
