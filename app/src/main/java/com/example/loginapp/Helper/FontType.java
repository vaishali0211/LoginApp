package com.example.loginapp.Helper;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class FontType {
	public Typeface typeFace_AmTypeWriter;
	
	public FontType(Context con, ViewGroup root)
	{
		typeFace_AmTypeWriter= Typeface.createFromAsset(con.getAssets(),"font/Gelion-Regular.ttf");
		setFont(root, typeFace_AmTypeWriter);
	}
	
	public FontType(Context con) {
		typeFace_AmTypeWriter= Typeface.createFromAsset(con.getAssets(),"font/Gelion-Regular.ttf");
		//typeFace_AmTypeWriter_Italic= Typeface.createFromAsset(con.getAssets(),"fonts/Arcon-Regular.otf");
		
	}

	public void setFont(ViewGroup group, Typeface font) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof Button) {
				((TextView) v).setTypeface(font);
			} 
			else if (v instanceof ViewGroup)
				setFont((ViewGroup) v, font);
		}
	}
}
