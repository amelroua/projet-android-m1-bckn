package com.example.eyeway;

import java.util.ArrayList;
import java.util.HashMap;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<Fonctionnalite> {
	//utiliser ArrayAdapter
    private Activity activity;
    
    private static LayoutInflater inflater=null;
    Context context; 
    int layoutResourceId;    
    Fonctionnalite data[] = null;
    
    public ListAdapter(Context context, int layoutResourceId, Fonctionnalite[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
 	
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        FonctionnaliteHolder holder=null;
        if(row==null){
        	LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        	row=inflater.inflate(layoutResourceId, null);
        	holder=new FonctionnaliteHolder();
        	holder.imgIcon=(ImageView)row.findViewById(R.id.icon_action);
        	holder.txtTitle=(TextView)row.findViewById(R.id.texte_ligne);
        	row.setTag(holder);
        }
        else {
        	
        	holder=(FonctionnaliteHolder)row.getTag();
        }
        
        Fonctionnalite f=data[position];
        holder.txtTitle.setText(f.title);
        holder.imgIcon.setImageResource(f.icon);   
        return row;
    }
    static class FonctionnaliteHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}