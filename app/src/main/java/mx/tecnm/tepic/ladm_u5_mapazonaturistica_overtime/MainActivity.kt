package mx.tecnm.tepic.ladm_u5_mapazonaturistica_overtime

import android.R
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import mx.tecnm.tepic.ladm_u5_mapazonaturistica_overtime.databinding.ActivityMainBinding
import java.io.File


class MainActivity() : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var Nombre = ""
    var Descripcion = ""
    var Carpeta = ""
    var listaArchivos = ArrayList<String>()

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras : Bundle? = getIntent().getExtras()
        Nombre = extras?.getString("Nombre").toString()
        Descripcion = extras?.getString("Descripcion").toString()
        Carpeta = extras?.getString("Carpeta").toString()

        binding.titulo.text = Nombre
        binding.descripcion.text = Descripcion
        cargarLista()

        binding.volver.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

    }

    private fun cargarLista() {
        val storageRef = FirebaseStorage.getInstance()
            .reference.child("${Carpeta}")
        storageRef.listAll()
            .addOnSuccessListener {
                listaArchivos.clear()
                it.items.forEach {
                    listaArchivos.add(it.name)
                }
                binding.lista.adapter = ArrayAdapter<String>(this,
                    R.layout.simple_list_item_1, listaArchivos)

                binding.lista.setOnItemClickListener { adapterView, view, i, l ->
                    cargarImagen(listaArchivos.get(i))
                }
            }
            .addOnFailureListener {

            }
    }

    private fun cargarImagen(archivo: String) {
        val storageRef = FirebaseStorage.getInstance()
            .reference.child("${Carpeta}/${archivo}")

        val archivoTemporal = File.createTempFile("imagenTemp", "jpeg")

        storageRef.getFile(archivoTemporal)
            .addOnSuccessListener {
                val mapaBits = BitmapFactory.decodeFile(archivoTemporal.absolutePath)
                binding.imagen.setImageBitmap(mapaBits)
            }
    }
}