package com.bday;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Switch switchStartDay;
    Switch switchWeekend;
    Switch switchHoliday;
    EditText dateStart;
    EditText numberDays;
    TextView textResultDay;
    TextView textResultDate;
    TextView textResultTitle;
    String calculationThreadsNamePrefix = "App-UpdateResult_";
    Button dateSelectionButton;
    int year_x,month_x,day_x;
    int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        InitializeInputReferences();
        setDefaultUIValues();
        DefineInputActions();
    }

    public void DefineInputActions()
    {
        DefineTextActions();
        DefineSwitchActions();
        DefineDateDialogAction();
    }

    public void DefineDateDialogAction()
    {
        Calendar today = Calendar.getInstance();

        year_x = today.get(Calendar.YEAR);
        month_x = today.get(Calendar.MONTH);
        day_x = today.get(Calendar.DAY_OF_MONTH);

        System.out.println("fuck me! " + day_x + "/" + month_x + "/" + year_x);

        dateSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });


    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this,dpickerListener,year_x,month_x,day_x);
        return null;
    }
    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year_x = i;
            month_x = i1 + 1;
            day_x = i2;

            String prefixDay = day_x<=10?"0":"";
            String prefixMonth = month_x<10?"0":"";
            dateStart.setText( prefixDay + day_x + "/" + prefixMonth + month_x + "/" + year_x);
            handleInput();
            Toast.makeText(MainActivity.this, day_x + "/" + month_x + "/" + year_x, Toast.LENGTH_LONG).show();
        }
    };

    public void InitializeInputReferences()
    {
        //set references to interface Widgets

        switchStartDay = (Switch) findViewById(R.id.switchStartDay);
        switchWeekend = (Switch) findViewById(R.id.switchWeekend);
        switchHoliday = (Switch) findViewById(R.id.switchHoliday);

        dateStart = (EditText) findViewById(R.id.dateStart);
        numberDays = (EditText) findViewById(R.id.numberDays);

        textResultTitle = (TextView) findViewById(R.id.textResultTitle);
        textResultDay = (TextView) findViewById(R.id.textResultDay);
        textResultDate = (TextView) findViewById(R.id.textResultDate);

        dateSelectionButton = (Button) findViewById(R.id.dateSelectionButton);

    }

    public void setDefaultUIValues()
    {
        handleInput(); //calculate an initial result for the default values
    }
    public void DefineSwitchActions()
    {
        switchStartDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleInput();
            }
        });

        switchWeekend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleInput();
            }
        });

        switchHoliday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleInput();
            }
        });
    }

    public void DefineTextActions()
    {
        numberDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { handleInput(); }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    public void handleInput()
    {
        final InputFieldValues state = new InputFieldValues( DateLogic.formatDDMMYYYYToDate(dateStart.getText().toString()), numberOfDaysTyped() , switchStartDay.isChecked(), switchWeekend.isChecked(), switchHoliday.isChecked());

        int ID = (int)(Math.random()*100000 + (new Date().getTime())%1000);
        String newThreadName = calculationThreadsNamePrefix+ID;
        killAllOtherCalculationThreads(ID);

        restrictChoices(state);
        resetResultFields();

        class ResultCalculationThread extends Thread {
            public void run() {
                state.setEndDate(calculateResult(state));
                if(! isInterrupted()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() { updateResultFields(state); }
                    });
                }
                else {
                    System.out.println(this.getName() + ": thread interrupted. result not displayed.");
                }

            }
        }

        Thread t = new ResultCalculationThread();
        t.setName(newThreadName);
        t.start();


    }

    public void killAllOtherCalculationThreads(int threadID) {
        String latestThreadName = calculationThreadsNamePrefix+threadID;
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);

        for(int i=0; i< threadArray.length;i++) {
            String threadName = threadArray[i].getName();
            if(threadName.contains(calculationThreadsNamePrefix) && threadName.compareTo(latestThreadName) !=0) {
                System.out.println(threadArray[i].getName() + " is interrupted because of newer thread: " + latestThreadName);
                threadArray[i].interrupt();
            }
        }
    }

    public void restrictChoices(InputFieldValues state)
    {   //getDay() -> days begin from 0

        if(state.getStartDate().getDay() + 1 == Calendar.SATURDAY) {switchStartDay.setChecked(false);} // you can't start from a saturday as its never a business day

        if (state.getStartDate().getDay() + 1 == Calendar.FRIDAY && switchStartDay.isChecked()) {
            switchWeekend.setChecked(true);
            state.setFridayIsBusinessDay(true);
        }
/*
        Calendar cal = Calendar.getInstance(); //new calendar
        cal.setTime(state.getStartDate());
        CalendarManager cm = new CalendarManager(cal);
        if(cm.getJewishEvent().compareTo("None") != 0 &&  switchStartDay.isChecked()){
            switchHoliday.setChecked(true);
            state.setHolHaMoedIsBusinessDay(true);
        }
*/

        //holidays

    }

    public Date calculateResult(InputFieldValues state)
    {
        Date dEnd = DateLogic.findDay(state.getStartDate(),state.getHowManyDays(),state.isFromSameDay(),state.isFridayIsBusinessDay(),state.isHolHaMoedIsBusinessDay());
        return dEnd;
    }

    public void resetResultFields()
    {
        textResultTitle.setText("רק שנייה...");
        textResultDay.setText("");
        textResultDate.setText("");
    }
    public int numberOfDaysTyped()
    {
        int numDays;
        try { numDays = Integer.parseInt(numberDays.getText().toString()); }
        catch (Exception E) { numDays = 1; }
        return numDays;
    }
    public void updateResultFields(InputFieldValues state)
    {
        Date dEnd = state.getEndDate();
        textResultTitle.setText(DateLogic.formatTitle(state.getHowManyDays()));
        textResultDay.setText(DateLogic.translateDayNameToHebrew(dEnd.getDay()));
        textResultDate.setText(DateLogic.formatDateToDDMMYYYY(dEnd));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) { drawer.closeDrawer(GravityCompat.START); }
        else { super.onBackPressed(); }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
