package example.realmchangelistenertest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;

import java.util.Collection;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

/**
 * Created on 29/02/2016.
 *
 * @author Matteo Zaccagnino
 */
public class DomainController {
    private static final String sLogTag = DomainController.class.getSimpleName();
    private static final String sNullContextExceptionMsg = "Cannot complete initialization with a null context";
    private static DomainController ourInstance;
    private final Realm realm;

    private DomainController(Context context) {
        if (context == null) throw new NullPointerException(sNullContextExceptionMsg);
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        if (BuildConfig.DEBUG) Log.i(sLogTag, "Initialization completed");
    }

    @UiThread
    public static DomainController getInstance(@NonNull Context context) {
        if (ourInstance == null) ourInstance = new DomainController(context);
        return ourInstance;
    }

    @UiThread
    public <T extends RealmObject> T save(@NonNull final T object) {
        realm.beginTransaction();
        T realmObject = realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
        return realmObject;
    }

    @UiThread
    public <T extends RealmObject> Collection<T> save(@NonNull final Collection<T> objects) {
        realm.beginTransaction();
        Collection<T> realmObjects = realm.copyToRealmOrUpdate(objects);
        realm.commitTransaction();
        return realmObjects;
    }

    @UiThread
    public <T extends RealmObject> void delete(@NonNull final Class<T> objectsClass) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(objectsClass);
            }
        });
    }

    @UiThread
    public <T extends RealmObject> void delete(@NonNull final T object) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                object.deleteFromRealm();
            }
        });
    }

    @UiThread
    public <T extends RealmObject> T getDetachedCopy(@NonNull final T object) {
        return realm.copyFromRealm(object);
    }
}
