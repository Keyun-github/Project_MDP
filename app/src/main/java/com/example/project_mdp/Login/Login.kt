package com.example.project_mdp.Login

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_mdp.ApiClient
import com.example.project_mdp.Donatur.HomeActivity
import com.example.project_mdp.HomeAdmin.HomeAdminActivity
import com.example.project_mdp.R
import com.example.project_mdp.Register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtEmail = findViewById<EditText>(R.id.etEmail)
        val edtPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        // Menambahkan listener untuk membuka halaman register
        tvRegister.setOnClickListener {
            val intent = Intent(this@Login, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = LoginRequest(email, password)

            ApiClient.instance.loginUser(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!
                        val user = loginResponse.user
                        if (user != null) {
                            Toast.makeText(this@Login, "Login Berhasil. Selamat datang ${user.namaLengkap}", Toast.LENGTH_SHORT).show()
                            if (user.role == "admin") {
                                startActivity(Intent(this@Login, HomeAdminActivity::class.java))
                            } else {
                                startActivity(Intent(this@Login, HomeActivity::class.java))
                            }
                            finish()
                        } else {
                            Toast.makeText(this@Login, "User data tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@Login, "Login gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@Login, "Kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
