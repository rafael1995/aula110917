package com.leonardo.conexaohttp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String opcaoRadio = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        consulta();
    }

    public void consulta() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo redeInfo = connMgr.getActiveNetworkInfo();
        if (redeInfo != null && redeInfo.isConnected()) {
            new ComunicacaoAssincrona().execute("https://goo.gl/g9hXoa");
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
            ((TextView)findViewById(R.id.txt1)).setText(retorno);
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

    public void radioClick(View view) {
        // usa o método isChecked() para saber se o
        // radioClick está selecionado
        RadioButton controle = (RadioButton) view;
        boolean checado = controle.isChecked();
        // testa qual radioClick foi clicado
        switch(controle.getId()) {
            case R.id.radioButtonA:
                if (checado)
                    opcaoRadio = "A";
                    Toast.makeText(this,"Opção A selecionada",Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButtonB:
                if (checado)
                    opcaoRadio = "B";
                    Toast.makeText(this,"Opção B selecionada",Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButtonC:
                if (checado)
                    opcaoRadio = "C";
                    Toast.makeText(this,"Opção C selecionada",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void enviar(View v) {
        if (opcaoRadio != "") {
            Intent i = new Intent(this, SegundaActivity.class);
            i.putExtra("opcao",opcaoRadio);
            startActivity(i);
        } else {
            Toast.makeText(this,"Selecione alguma das opções",Toast.LENGTH_SHORT).show();
        }
    }

}

