package edu.msoe.hermanb.lab2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.HORIZONTAL;

public class MainActivity extends AppCompatActivity {
    private EditText gpaEditText;
    private EditText honors;

    private LinearLayout parentLayout;

    private static final double HIGH_HONORS = 3.7;
    private static final double DEANS_LIST = 3.2;
    private static final double PROBATION = 2.0;

    private ArrayAdapter<CharSequence> gradeSpinnerAdapter;
    private ArrayAdapter<CharSequence> classNameAdapter;

    private static ArrayList<Class> classList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parentLayout = findViewById(R.id.container);
        gpaEditText = findViewById(R.id.gpa);
        honors = findViewById(R.id.honorsResults);
        classList = new ArrayList<>();

        gradeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.letter_grades, android.R.layout.simple_spinner_item);
        gradeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        classNameAdapter = ArrayAdapter.createFromResource(this,
                R.array.known_courses, android.R.layout.simple_dropdown_item_1line);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClassLayout();
            }
        });

        // Create one class
        addClassLayout();
    }

    private void calculateGPA() {
        double cummulativeGPA = 0.0;
        int totalCredits = 0;
        for(Class c : classList) {
            if(!c.name.equals("")) {
                cummulativeGPA += c.credits * c.gpa.getValue();
                totalCredits += c.credits;
            }
        }

        if(totalCredits != 0) {
            cummulativeGPA = cummulativeGPA / totalCredits;
        } else {
            cummulativeGPA = 0.0;
        }

        DecimalFormat df = new DecimalFormat("0.00");
        gpaEditText.setText(df.format(cummulativeGPA));

        if (cummulativeGPA >= DEANS_LIST) {
            if (cummulativeGPA >= HIGH_HONORS){
                honors.setText(R.string.HighHonors);
            } else {
                honors.setText(R.string.DeansList);
            }
        } else if (cummulativeGPA < PROBATION) {
            honors.setText(R.string.Probation);
        } else {
            honors.setText(R.string.NoHonors);
        }
    }

    private void addClassLayout(){
        LinearLayout container = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FILL_PARENT, WRAP_CONTENT);
        container.setLayoutParams(params);
        container.setOrientation(HORIZONTAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,WRAP_CONTENT, (float)0.30); // Width , height

        final Class temp = new Class(container);

        AutoCompleteTextView className = new AutoCompleteTextView(this);
        className.setHint(R.string.exampleClass);
        className.setLayoutParams(layoutParams);
        className.setGravity(Gravity.CENTER);
        className.setAdapter(classNameAdapter);
        className.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                temp.name = s.toString();
                calculateGPA();
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
        container.addView(className);

        EditText credits = new EditText(this);
        credits.setInputType(InputType.TYPE_CLASS_NUMBER);
        credits.setLayoutParams(layoutParams);
        credits.setGravity(Gravity.CENTER);
        credits.setText("0");
        credits.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                try{
                    temp.credits = Integer.parseInt(s.toString());
                } catch (NumberFormatException ex) {
                    temp.credits = 0;
                }
                calculateGPA();
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
        container.addView(credits);

        // Create spinner
        Spinner spinner = new Spinner(this);
        spinner.setAdapter(gradeSpinnerAdapter);
        spinner.setLayoutParams(layoutParams);
        spinner.setGravity(Gravity.CENTER);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                temp.gpa = Class.GPA.getGPA(parentView.getSelectedItem().toString());
                calculateGPA();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        container.addView(spinner);

        LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(0,WRAP_CONTENT, (float)0.10); // Width , height
        Button deleteButton = new Button(this);
        deleteButton.setText("-");
        deleteButton.setLayoutParams(buttonLayout);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentLayout.removeView(temp.container);
                classList.remove(temp);
                calculateGPA();
            }
        });
        container.addView(deleteButton);

        classList.add(temp);
        parentLayout.addView(container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
