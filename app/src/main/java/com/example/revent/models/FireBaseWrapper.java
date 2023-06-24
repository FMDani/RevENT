package com.example.revent.models;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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


                //TODO: Better handling of the error
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

        public boolean isAuthenticated() {return this.auth.getCurrentUser() != null;}

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

        public void signOut() {this.auth.signOut();}

    }
}
