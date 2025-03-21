package com.example.reread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btnCadastro, btnEntrar;
    private EditText editTextEmail, editTextSenha;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnCadastro = findViewById(R.id.btnCadastro);
        btnEntrar = findViewById(R.id.btnEntrar);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);

    }

    @Override
    protected void onResume() {
        super.onResume();

        btnEntrar.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();
            String senha = editTextSenha.getText().toString().trim();

            if(email.isEmpty() || senha.isEmpty()){
                Toast.makeText(getApplicationContext(), "Por favor, preencha os campos corretamente.", Toast.LENGTH_LONG).show();
            }else{
                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener((Activity) LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Login realizado com sucesso", Toast.LENGTH_LONG).show();
                        goToApp();
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(getApplicationContext(), "E-mail ou senha inválidos", Toast.LENGTH_LONG).show();
                        } catch (FirebaseNetworkException e) {
                            Toast.makeText(getApplicationContext(), "Sem conexão com a internet", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("Erro no login", e.getMessage());
                        }
                    }
                });
            }
        });

        btnCadastro.setOnClickListener(view -> {
            Intent cadastro = new Intent(getApplicationContext(), CadastroActivity.class);
            startActivity(cadastro);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = auth.getInstance().getCurrentUser();

        if(usuarioAtual != null){
            goToApp();
        }

    }

    private void goToApp(){
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);
        finish();
    }

}
