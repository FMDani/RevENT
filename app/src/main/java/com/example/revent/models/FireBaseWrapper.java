package com.example.revent.models;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FireBaseWrapper {

    public static class Callback {

        private static final String TAG = Callback.class.getCanonicalName();
        private final Method method;
        private final Object thiz;

        public Callback(Method method, Object thiz) {
            this.method = method;
            this.thiz = thiz;
        }

        public static Callback newInstance(Object thiz, String name, Class<?>... prms) {
            Class<?> clazz = thiz.getClass();

            try{
                return new Callback(clazz.getMethod(name, prms), thiz);
            } catch (NoSuchMethodException e) {
                Log.w(TAG, "Cannot find  method " + name + " in class " + clazz.getCanonicalName());


                throw new RuntimeException(e);
            }
        }
        /// Object con tre punti indica che questa metodo può prendere input da zero parametri a cento
        ///Lista di oggetti ma noi non sappiamo che oggetti siano

        public void invoke(Object... objs) {
            try {
                this.method.invoke(thiz, objs);
            } catch (IllegalAccessException | InvocationTargetException e) {

                Log.w(TAG,"Something went wrong. Message: " + e.getMessage());
                throw new RuntimeException(e);
            }

        }
    }

   public static class Auth {

        private static final String TAG = Auth.class.getCanonicalName();
        private final FirebaseAuth auth;


        public Auth() {

            this.auth = FirebaseAuth.getInstance();
        }

        public boolean isAuthenticated() {
            return this.auth.getCurrentUser() != null;
        }

       public FirebaseUser getUser() {
           return this.auth.getCurrentUser();
       }

        public void signIn(String email, String password, FireBaseWrapper.Callback callback) {
            System.out.println("email : " + email + "password: "+ password);
            this.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           callback.invoke(task.isSuccessful());
                        }
                    });
        }

        public void signUp(String email, String password, FireBaseWrapper.Callback callback) {
            this.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            callback.invoke(task.isSuccessful());

                        }
                    });
        }

        public void signOut(FireBaseWrapper.Callback callback) {
            this.auth.signOut();
            callback.invoke(true);

        }

       public String getUid() {
           return this.getUser().getUid();
       }

    }

    public static class RTDatabase {

        private final static String TAG = RTDatabase.class.getCanonicalName();

        // This is the name of the root of the DB (in the JSON format)

        private static final String CHILD = "events";

        private DatabaseReference getDb() {

            DatabaseReference ref = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference(CHILD);
            //FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").setValue("test");


            // Ritorna solo gli eventi del utente corrente

            String uid = new FireBaseWrapper.Auth().getUid();

            if(uid == null) {
                return null;
            }

            return ref.child(uid);
        }

        private DatabaseReference getUserDb() {

            DatabaseReference ref = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
            //FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").setValue("test");


            // Ritorna solo gli eventi del utente corrente

            String uid = new FireBaseWrapper.Auth().getUid();

            if(uid == null) {
                return null;
            }

            return ref.child(uid);
        }

        public void writeDbData(MyEvent myEvent) {
            DatabaseReference ref = getDb();
            if(ref == null) {
                return;
            }

            ref.child(String.valueOf(myEvent.getEventId())).setValue(myEvent);
        }


        public void readUserDbData(User user) {

            DatabaseReference ref_user = getDb();
        }
    }
}
