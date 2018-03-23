package edu.msoe.hermanb.lab2;

import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * Created by hermanb on 3/18/2018.
 */

public class Class{
    /* Package */ String name = "";
    /* Package */ GPA gpa = GPA.A;
    /* Package */ int credits = 0;
    /* Package */ AutoCompleteTextView classNameText;
    /* Package */ EditText creditsText;
    /* Package */ Spinner gradeSpinner;
    /* Pacakge */ LinearLayout container;

    public enum GPA {
        A("A", 4.0),
        AB("AB", 3.50),
        B("B", 3.0),
        BC("BC", 2.5),
        C("C", 2.0),
        CD("CD", 1.5),
        D("D", 1.0),
        F("F", 0.0);

        private final String name;
        private final double value;

        GPA(String name, double value) {
            this.name = name;
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public static GPA getGPA(String name){
            for (GPA gpa : GPA.values()) {
                if (gpa.name.equalsIgnoreCase(name)) {
                    return gpa;
                }
            }

            return null;
        }
    }

    /* Package */ Class(LinearLayout container){
        this.container = container;
    }

    /* Package */ Class(AutoCompleteTextView classNameText, EditText creditsText, Spinner gradeSpinner){
        this.classNameText = classNameText;
        this.creditsText = creditsText;
        this.gradeSpinner = gradeSpinner;

        classNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used
            }
        });

        creditsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                credits = Integer.parseInt(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used
            }
        });

        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                gpa = GPA.getGPA(parentView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }
}
