package com.acheprovas.activitys;

import com.acheprovas.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

public class ProvasActivity extends Activity {

	protected ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Define a view da activity
		this.setContentView(R.layout.lista_provas);
		
		//Uicializa componentes
		this.initComponents();
		
	}
	
	/**
	 * Infla as a�oes da action bar desta view
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.provas_activity_actions, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Inicializa e instanc�a os componentes gr�ficos da view
	 */
	protected void initComponents() {
		
	}
	
}
