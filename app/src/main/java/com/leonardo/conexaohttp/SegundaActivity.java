package com.leonardo.conexaohttp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SegundaActivity extends Activity {

    String opcao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);
        Intent i = getIntent();
        opcao = i.getStringExtra("opcao");
//        ((TextView)findViewById(R.id.txt2)).setText(opcao);
        consulta();
    }

    public void consulta() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo redeInfo = connMgr.getActiveNetworkInfo();
        if (redeInfo != null && redeInfo.isConnected()) {
            //http://professorangoti.com/aula10.php/?zxc=a
            new SegundaActivity.ComunicacaoAssincrona().execute("http://professorangoti.com/aula10.php/?zxc=" + opcao.toLowerCase());
        } else {
            Toast.makeText(this,"Erro de rede", Toast.LENGTH_SHORT).show();
        }
    }


    private class ComunicacaoAssincrona extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return realizaRequisicaoHTTP(urls[0]);
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String retorno) {
            ((TextView)findViewById(R.id.txt2)).setText(retorno);
        }
    }//fim da classe interna ComunicacaoAssincrona

    // Realiza a conexão HTTP e retorna a resposta em String
    private String realizaRequisicaoHTTP(String endereco) throws IOException {
        InputStream is = null;
        int len = 500;
        try {
            // A classe URL representa um "Uniform Resource Locator", um ponteiro para um recurso na Web
            URL url = new URL(endereco); // cria um novo objeto que representa um endereço de Web
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Retorna uma instância URLConnection que representa uma conexão com o objeto remoto referido pelo URL.
            conn.setRequestMethod("GET");
            conn.connect(); // Estabelece a conexão HTTP
            is = conn.getInputStream();
            return converteStreamEmString(is, len);  // Converte  a resposta em string
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // recebe um InputStream e retorna uma String
    public String converteStreamEmString(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "ISO-8859-1");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }




    public void retornar(View v) {
        Intent i = new Intent(this, MainActivity.class);
//        i.putExtra("nome","Angoti");
//        i.putExtra("idade",47);
        startActivity(i);
    }
}
