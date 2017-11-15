package com.teomanyaman.radio;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teomanyaman.library.radio.RadioListener;
import com.teomanyaman.library.radio.RadioManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

public class Radios extends Activity implements RadioListener {

    private List<RadioDao> list;
    private List<RadioDao> notFilteredList;

    private TextView text_radios;
    private ImageView image_favouriteRadios;
    private EditText edit_searchRadios;
    private ListView listView_radios;
    private RadioAdapter radioAdapter;
    private String textRadio;
    private Database mDatabase;
    private RadioManager mRadioManager;
    private ProgressDialog progress;
    private int screenFavouriteStatus = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radios);

        findViews();
        databaseOperations();
        getValues();
        radioConnection();
        favouriteClick();
        editTextChange();
        listViewClick();
    }

    private void findViews() {
        text_radios = findViewById(R.id.text_radios);
        image_favouriteRadios = findViewById(R.id.image_favouriteRadios);
        edit_searchRadios = findViewById(R.id.edit_searchRadios);
        listView_radios = findViewById(R.id.listView_radios);

    }

    private void databaseOperations(){
        mDatabase = new Database(this);

        File database = getApplicationContext().getDatabasePath(Database.DBNAME);
        if (false == database.exists()) {
            mDatabase.getReadableDatabase();

            if (!copyDatabase(this)) {
                return;
            } else {

            }
        }
    }

    private void getValues(){
        list = mDatabase.getRadios();
        notFilteredList = mDatabase.getRadios();
        radioAdapter = new RadioAdapter(this, list, notFilteredList);
        listView_radios.setTextFilterEnabled(true);
        listView_radios.setAdapter(radioAdapter);
    }

    private void radioConnection(){
        mRadioManager = RadioManager.with(getApplicationContext());
        mRadioManager.registerListener(this);
        mRadioManager.setLogging(true);
        mRadioManager.connect();
    }

    private void favouriteClick(){
        image_favouriteRadios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (screenFavouriteStatus == 1) {
                    screenFavouriteStatus = 2;
                    image_favouriteRadios.setImageResource(R.drawable.favori_beyaz);
                    edit_searchRadios.setText("");
                    list = mDatabase.getRadios();
                    notFilteredList = mDatabase.getRadios();
                    radioAdapter = new RadioAdapter(Radios.this, list, notFilteredList);
                    listView_radios.setTextFilterEnabled(true);
                    listView_radios.setAdapter(radioAdapter);
                } else {
                    screenFavouriteStatus = 1;
                    image_favouriteRadios.setImageResource(R.drawable.favori_kirmizi);
                    edit_searchRadios.setText("");
                    list = mDatabase.getFavourites();
                    notFilteredList = mDatabase.getFavourites();
                    radioAdapter = new RadioAdapter(Radios.this, list, notFilteredList);
                    listView_radios.setTextFilterEnabled(true);
                    listView_radios.setAdapter(radioAdapter);
                }

            }
        });
    }

    private void editTextChange(){
        edit_searchRadios.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textRadio = edit_searchRadios.getText().toString().toLowerCase(Locale.getDefault());
                Radios.this.radioAdapter.getFilter().filter(textRadio);
            }

        });

    }

    private void listViewClick(){
        listView_radios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!mRadioManager.isPlaying()) {
                    mRadioManager.startRadio(list.get(position).getURL());
                } else {
                    mRadioManager.stopRadio();
                    mRadioManager.startRadio(list.get(position).getURL());
                }

                progress = ProgressDialog.show(Radios.this, "", getResources().getString(R.string.lutfenBekleyiniz), true);
                text_radios.setText(list.get(position).getName());

            }
        });
    }

    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(Database.DBNAME);
            String outFileName = Database.DBLOCATION + Database.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] buff = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public void onRadioLoading() {
    }

    @Override
    public void onRadioConnected() {
    }

    @Override
    public void onRadioStarted() {
        progress.dismiss();
    }

    @Override
    public void onRadioStopped() {
    }

    @Override
    public void onMetaDataReceived(String s, String s2) {

    }

    @Override
    public void onError() {
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}