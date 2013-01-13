package com.example.eyeway.map;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ListItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem>  arrayListOverlayItem = new ArrayList<OverlayItem>();

	private Context context;
	private Drawable dr;

	public ListItemizedOverlay(Drawable defaultMarker, Context pContext)
	{
		super(boundCenterBottom(defaultMarker));
		setDr(defaultMarker);
		this.context = pContext;
	}

	public ListItemizedOverlay(Drawable arg0) {
		super(boundCenterBottom(arg0));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OverlayItem createItem(int arg0) {
		// TODO Auto-generated method stub
		return arrayListOverlayItem.get(arg0);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return arrayListOverlayItem.size();
	}


	public void addOverlayItem(OverlayItem overlay)
	{
		arrayListOverlayItem.add(overlay);
		populate();
	}

	@Override
	protected boolean onTap(int index)
	{
		OverlayItem item = arrayListOverlayItem.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}

	public Drawable getDr() {
		return dr;
	}

	public void setDr(Drawable dr) {
		this.dr = dr;
	}

}
