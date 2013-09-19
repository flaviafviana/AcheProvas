package com.acheprovas.activitys;

import ufop.smd.controle.Constantes;
import ufop.smd.gui.activity.Opcoes;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.acheprovas.R;

/**
 * Esta classe implementa a Activity da view "MainActivity", ou seja, a view de
 * introdu��o da aplica��o.
 * 
 * @author Maycon Brito
 * @since 14/09/2013
 */
public class MainActivity extends Activity {

	/**
	 * Vari�veis da classe que representam componentes gr�ficos da view
	 */
	private EditText etBuscar;
	private Button btBuscar;

	/**
	 * Implementa as a��es a serem executadas assim que a activity � criada
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_main);
		this.initComponents();
	}

	/**
	 * Inicializa e instanc�a os componentes gr�ficos da view
	 */
	protected void initComponents() {

		//Instanc�a os componentes gr�ficos
		this.setEtBuscar((EditText) findViewById(R.id.etBuscar));
		this.setBtBuscar((Button) findViewById(R.id.btBuscar));
		
		//Define os eventos de cada componente
		this.btBuscar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(this, ListaProvasActivity.class);
				startActivityForResult(it, 0);
				
			}
		});
		
		this.etBuscar.setText("Hellow!!!");
	}

	/**
	 * M�todos Getters e Setters da Classe
	 */
	public EditText getEtBuscar() {
		return etBuscar;
	}

	public void setEtBuscar(EditText etBuscar) {
		this.etBuscar = etBuscar;
	}

	public Button getBtBuscar() {
		return btBuscar;
	}

	public void setBtBuscar(Button btBuscar) {
		this.btBuscar = btBuscar;
	}

}