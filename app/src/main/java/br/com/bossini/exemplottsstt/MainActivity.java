package br.com.bossini.exemplottsstt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText textoParaFalarEditText;
    private ListView textoFaladoListView;
    private List <String> textoFalado;
    private ArrayAdapter<String> adapter;
    private TextToSpeech textToSpeech;
    private SpeechRecognizer speechRecognizer;
    private static final int PERMISSION_RECORD_AUDIO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            inicializaTudo();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO);
        }
    }
    public void stt (View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent. EXTRA_LANGUAGE , "pt-BR");
        intent.putExtra(RecognizerIntent. EXTRA_LANGUAGE_PREFERENCE , "pt-BR");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        speechRecognizer.startListening(intent);
    }

    public void tts (View view){
        String textoParaFalar = textoParaFalarEditText.getEditableText().toString();
        textToSpeech.speak(textoParaFalar, TextToSpeech.QUEUE_FLUSH, null,hashCode() + "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_RECORD_AUDIO){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                inicializaTudo();
            }
            else{
                Toast.makeText(this, "Sem a permissão não vai rolar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void inicializaTudo (){
        textoParaFalarEditText = (EditText) findViewById(R.id.textoParaFalarEditText);
        textoFaladoListView = (ListView) findViewById(R.id.textoFaladoListView);
        textoFalado = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, textoFalado);
        textoFaladoListView.setAdapter(adapter);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    //idioma padrão do sistema
                    textToSpeech.setLanguage(Locale.getDefault());
                    //tom
                    textToSpeech.setPitch(1.3f);
                    //velocidade da fala
                    textToSpeech.setSpeechRate(1f);
                }
            }
        });
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {
                Toast.makeText(MainActivity.this, "onError:" + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                //só tem um resultado, então pega na posição 0 do ArrayList
               String falou =  results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
               textoFalado.add(falou);
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }
}
