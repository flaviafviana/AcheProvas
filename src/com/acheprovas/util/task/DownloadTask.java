package com.acheprovas.util.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.acheprovas.libs.Constants;
import com.acheprovas.model.Prova;
import com.acheprovas.util.Decompress;

/**
 * 
 * @author mayconfsbrito
 * 
 */

@SuppressLint({ "ShowToast", "Wakelock" })
public class DownloadTask extends AsyncTask<Prova, Integer, String> {

	private Context context;
	private String resultado;
	protected ProgressDialog mProgressDialog;

	public DownloadTask(Context context, ProgressDialog mProgressDialog) {
		this.context = context;
		this.mProgressDialog = mProgressDialog;
	}

	/**
	 * M�todo de execu��o da thread. Realiza de forma independete o download de
	 * uma determinada prova.
	 * 
	 * Artigo Refer�ncia:
	 * http://stackoverflow.com/questions/3028306/download-a-file
	 * -with-android-and-showing-the-progress-in-a-progressdialog
	 */
	@Override
	protected String doInBackground(Prova... prova) {

		// Previne que a CPU seja desligada caso o usu�rio pressione o bot�o de
		// desligar durante o download
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
		wl.acquire();

		// Inicializa vari�veis de conex�o
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		String resultado = "";

		try {

			// Inicializa a conex�o para download da prova
			String urlProva = prova[0].getLink().replaceAll(" ", "%20");
			Log.d(null, "Baixando a prova: " + urlProva);
			URL url = new URL(urlProva);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.connect();

			// Aguarda um response request HTTP 200 OK, pois em caso positivo
			// reporta o erro
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
				return "Server returned HTTP " + connection.getResponseCode()
						+ " " + connection.getResponseMessage();

			// Exibe a porcentagem do download
			// Ser� -1 caso o servidor n�o retorne um tamanho para o arquivo
			int fileLength = connection.getContentLength();

			// Realiza o download do arquivo
			input = connection.getInputStream();
			String enderecoDiretorio = Environment
					.getExternalStorageDirectory() + Constants.DIRETORIO_PROVAS;
			String enderecoArquivo = enderecoDiretorio + prova[0].getNome()
					+ ".zip";
			File diretorio = new File(enderecoDiretorio);
			File arquivo = new File(diretorio, prova[0].getNome()
					+ ".zip");
			diretorio.mkdirs(); // Cria o diret�rio para salvar o arquivo
			Log.d(null, diretorio.getAbsolutePath());
			output = new FileOutputStream(arquivo); // Endere�o aonde o arquivo
													// da prova ser� gravado

			// Grava o download em um arquivo byte a byte atrav�s de um buffer
			byte data[] = new byte[4096];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				// Permite cancelamento com o bot�o voltar
				if (isCancelled()) {
					return null;
				}
				total += count;
				// Publicando o progresso no ProgressBar
				if (fileLength > 0) { // Se o tamanho total do arquivo for
										// conhecido
					publishProgress((int) (total * 100 / fileLength));
				}
				output.write(data, 0, count);
			}

			// Descompactando o arquivo baixado
			publishProgress(-1); //Envia um token (-1) para indicar a mudan�a da mensagem no ProgressBar
			Decompress d = new Decompress(enderecoArquivo, enderecoDiretorio);
			d.unzip();
			Log.d(null, "Arquivos descompactados com sucesso!");

		} catch (IOException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				// Fecha os Buffers de escrita e envio de informa��es atrav�s da
				// conex�o
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (IOException ignored) {
			}

			// Fecha a conex�o do download
			if (connection != null)
				connection.disconnect();

			wl.release();
		}

		return null;

	}

	/**
	 * Exibe o ProgressBar ao iniciar a execu��o da thread
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog.show();
	}

	/**
	 * Atualiza o progresso do download atrav�s no ProgressBar
	 */
	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);

		//O inteiro recebido � um token (-1) para mudar a mensagem?
		if (progress[0] != -1) {
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgress(progress[0]);
		} else {
			mProgressDialog.setMessage("Descompactando arquivos!");
		}
		
	}

	/**
	 * Realiza a atualiza��o da publica��o ao encerrar a execu��o do download
	 */
	@Override
	protected void onPostExecute(String result) {
		mProgressDialog.dismiss();
		if (result != null)
			Toast.makeText(context, "Download error: " + result,
					Toast.LENGTH_LONG).show();
		else
			Toast.makeText(context, "Prova baixada com sucesso!",
					Toast.LENGTH_SHORT).show();
	}

}
