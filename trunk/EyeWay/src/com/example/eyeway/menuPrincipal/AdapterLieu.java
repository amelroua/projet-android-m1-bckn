package com.example.eyeway.menuPrincipal;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eyeway.R;
import com.example.eyeway.fouilleDedonne.Lieu;

public class AdapterLieu extends ArrayAdapter<Lieu> {
	//utiliser ArrayAdapter
    private Activity activity;
    
    private static LayoutInflater inflater=null;
    Context context; 
    int layoutResourceId;    
    ArrayList<Lieu> data= null;
    
    public AdapterLieu(Context context, int layoutResourceId, ArrayList<Lieu> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
 	public void add(Lieu f){
 		if(!data.contains(f)){
 			data.add(f);
 			notifyDataSetChanged();
 		}
 	}
 	public void remove(int index){
 		data.remove(index);
 		notifyDataSetChanged();
 	}
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LieuHolder holder=null;
        if(row==null){
        	LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        	row=inflater.inflate(layoutResourceId, null);
        	holder=new LieuHolder();
        	holder.nom=(TextView)row.findViewById(R.id.text_view_nom);
        	holder.adresse=(TextView)row.findViewById(R.id.text_view_adresse);
        	row.setTag(holder);
        }
        else {	
        	holder=(LieuHolder)row.getTag();
        }
        
        Lieu l=data.get(position);
        holder.nom.setText(l.getNom());
        holder.adresse.setText(l.getFormatted_address());
        return row;
    }
    static class LieuHolder
    {
       TextView nom;
       TextView adresse;
    }
}