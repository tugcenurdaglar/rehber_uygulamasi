package com.tugcenurdaglar.kisileruygulamasi;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KisilerAdapter extends RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu> {
    private Context mContext;
    private List<Kisiler> kisilerListe;

    private KisilerDaoInterface kisilerDaoInterface;

    public KisilerAdapter(Context mContext, List<Kisiler> kisilerListe, KisilerDaoInterface kisilerDaoInterface) {
        this.mContext = mContext;
        this.kisilerListe = kisilerListe;
        this.kisilerDaoInterface = kisilerDaoInterface;
    }

    public KisilerAdapter(Context mContext, List<Kisiler> kisilerListe) {
        this.mContext = mContext;
        this.kisilerListe = kisilerListe;

    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //kisi_card tasarimini görsel nesnelere burada bağlarım

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kisi_card_tasarim, parent, false);

        return new CardTasarimTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardTasarimTutucu holder, int position) {
        //kaç veri varsa o kadar çalışır

        final Kisiler kisi = kisilerListe.get(position);

        holder.textViewKisiBilgi.setText(kisi.getKisiAd() + " - " + kisi.getKisiTel());

        holder.imageViewNokta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.imageViewNokta);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_sil:
                                Snackbar.make(holder.imageViewNokta, "Sil Tıklandı", Snackbar.LENGTH_SHORT)
                                        .setAction("Evet", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                kisilerDaoInterface.kisiSil(Integer.parseInt(kisi.getKisiId())).enqueue(new Callback<CRUDCevap>() {
                                                    @Override
                                                    public void onResponse(Call<CRUDCevap> call, Response<CRUDCevap> response) {
                                                        tumKisiler();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<CRUDCevap> call, Throwable t) {

                                                    }
                                                });


//                                                notifyDataSetChanged();//değişiklik olduğunda adaptere bilgi vermek için

                                            }
                                        })

                                        .show();
                                return true;

                            case R.id.action_guncelle:
                                alertGoster(kisi);
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return kisilerListe.size();
    }

    public class CardTasarimTutucu extends RecyclerView.ViewHolder {
        private TextView textViewKisiBilgi;
        private ImageView imageViewNokta;

        public CardTasarimTutucu(@NonNull View itemView) {
            super(itemView);

            textViewKisiBilgi = itemView.findViewById(R.id.textViewKisiBilgi);
            imageViewNokta = itemView.findViewById(R.id.imageViewNokta);
        }
    }

    public void alertGoster(Kisiler kisi) {
        //dışarıdan tasarım almak için LayoutInflater kullanılır
        LayoutInflater layout = LayoutInflater.from(mContext);

        View tasarim = layout.inflate(R.layout.alert_tasarim, null); //tasarıma eriştim

        final EditText editTextAd = tasarim.findViewById(R.id.editTextAd);
        final EditText editTextTel = tasarim.findViewById(R.id.editTextTel);

        editTextAd.setText(kisi.getKisiAd());
        editTextTel.setText(kisi.getKisiTel());

        AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
        ad.setTitle("Kişi Güncelle");
        ad.setView(tasarim); //oluşturulan alert tasarımı eklendi

        ad.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String kisi_ad = editTextAd.getText().toString().trim();
                String kisi_tel = editTextTel.getText().toString().trim();

                kisilerDaoInterface.kisiGuncelle(Integer.parseInt(kisi.getKisiId()),kisi_ad,kisi_tel).enqueue(new Callback<CRUDCevap>() {
                    @Override
                    public void onResponse(Call<CRUDCevap> call, Response<CRUDCevap> response) {
                        tumKisiler();
                    }

                    @Override
                    public void onFailure(Call<CRUDCevap> call, Throwable t) {

                    }
                });

//                notifyDataSetChanged();
            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        ad.create().show(); //artık alert tasarımını görebilirim


    }

    public void tumKisiler() {
        kisilerDaoInterface.tumKisiler().enqueue(new Callback<KisilerCevap>() {
            @Override
            public void onResponse(Call<KisilerCevap> call, Response<KisilerCevap> response) {
                kisilerListe = response.body().getKisiler();

                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<KisilerCevap> call, Throwable t) {

            }
        });

    }

}
