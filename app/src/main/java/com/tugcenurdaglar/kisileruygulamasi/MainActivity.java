package com.tugcenurdaglar.kisileruygulamasi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private RecyclerView rv;
    private FloatingActionButton fab;

    private ArrayList<Kisiler> kisilerArrayList;
    private KisilerAdapter adapter;

    private Veritabani vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);

        vt = new Veritabani(this);


        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        toolbar.setTitle("Kişiler Uygulaması");
        setSupportActionBar(toolbar);

        kisilerArrayList = new KisilerDao().tumKisiler(vt);

        adapter = new KisilerAdapter(this,kisilerArrayList, vt);
        rv.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertGoster(); //fab a tıklanıldığında alert gözükecek

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //menuyu bağlamak için
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);//menü bağlandı
        //arama iconunu aktif hale getirmek için;

        //hangi item a tıklanılacağını seçmek için MenuItem
        MenuItem menuItem = menu.findItem(R.id.action_ara);

        //arama özelliğini aktif etmek için, yani buna tıklanıldığında arama yapmak için;
        SearchView searchView = (SearchView) menuItem.getActionView();//down casting
        searchView.setOnQueryTextListener(this); //this Main Activity i temsil ediyor,
        // OnQueryTextListener implements ile bu özelliği bağlayıp
        // bu işlemi gerçekleştirme yetkisini verdim :)




        return super.onCreateOptionsMenu(menu);

        /*menüdeki arama iconunu aktifleştirmek için implements SearchView demeliyim,
        * androidx olmasına dikkat et!
        * implements SearchView.OnQueryTextListener dediğimde iki metod implements edilecek
        * */
    }

    @Override
    public boolean onQueryTextSubmit(String query) {//arama butonuna basınca arama işlemi yapar
        Log.e("icona basınca",query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {//harf girdikçe veya sildikçe arama işlemi yapar
        Log.e("harf girdikçe", newText);

        kisilerArrayList = new KisilerDao().kisiAra(vt,newText);

        adapter = new KisilerAdapter(this,kisilerArrayList,vt);
        rv.setAdapter(adapter);

        return false;
    }

    //alert tasarımını almak için
    public void alertGoster(){
        //dışarıdan tasarım almak için LayoutInflater kullanılır
        LayoutInflater layout = LayoutInflater.from(this);

        View tasarim = layout.inflate(R.layout.alert_tasarim,null); //tasarıma eriştim

        EditText editTextAd = tasarim.findViewById(R.id.editTextAd);
        EditText editTextTel = tasarim.findViewById(R.id.editTextTel);

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Kişi Ekle");
        ad.setView(tasarim); //oluşturulan alert tasarımı eklendi

        ad.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String kisi_ad = editTextAd.getText().toString().trim();
                String kisi_tel = editTextTel.getText().toString().trim();

                new KisilerDao().kisiEkle(vt,kisi_ad,kisi_tel);

                kisilerArrayList = new KisilerDao().tumKisiler(vt);

                adapter = new KisilerAdapter(MainActivity.this, kisilerArrayList, vt);

                rv.setAdapter(adapter);

                Toast.makeText(getApplicationContext(),kisi_ad+" - "+kisi_tel,Toast.LENGTH_SHORT).show();

            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        ad.create().show(); //artık alert tasarımını görebilirim


    }
}