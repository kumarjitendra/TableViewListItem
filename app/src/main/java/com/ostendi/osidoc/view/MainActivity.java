package com.ostendi.osidoc.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ostendi.osidoc.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            /*
            //getSupportFragmentManager =Return the FragmentManager for interacting with fragments associated with this activity.

            //beginTransaction=Start a series of edit operations on the Fragments associated with this FragmentManager.
             Note: A fragment transaction can only be created/committed prior to an activity saving its state.
             If you try to commit a transaction after FragmentActivity.onSaveInstanceState()
              (and prior to a following FragmentActivity.onStart or FragmentActivity.onResume(), you will get an error.
              This is because the framework takes care of saving your current fragments in the state, and if changes are made after the state is saved then they will be lost.

            //add =Add a fragment to the activity state. This fragment may optionally also have its view
             (if Fragment.onCreateView returns non-null) into a container view of the activity.

             */

            getSupportFragmentManager().beginTransaction().add(R.id.activity_container, new MainFragment(),
                    MainFragment.class.getSimpleName()).commit();
        }

    }
}
