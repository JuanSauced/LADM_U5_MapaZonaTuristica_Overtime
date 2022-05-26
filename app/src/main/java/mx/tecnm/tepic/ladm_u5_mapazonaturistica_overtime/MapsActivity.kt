package mx.tecnm.tepic.ladm_u5_mapazonaturistica_overtime

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.tepic.ladm_u5_mapazonaturistica_overtime.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var locacion : LocationManager
    private var baseRemota = FirebaseFirestore.getInstance()
    var posicion = ArrayList<Posicion>()
    var actual = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }

        baseRemota.collection("feria")
            .addSnapshotListener { value, error ->
                if(error != null){
                    Toast.makeText(this, "Error! No se puede acceder a Firebase", Toast.LENGTH_LONG)
                        .show()
                    return@addSnapshotListener
                }
                posicion.clear()
                for(documento in value!!){
                    var data = Posicion()
                    data.nombre = documento.getString("nombre").toString()
                    data.posicion1 = documento.getGeoPoint("posicion1")!!
                    data.posicion2 = documento.getGeoPoint("posicion2")!!
                    data.carpeta = documento.getString("carpeta").toString()
                    data.descripcion = documento.getString("descripcion").toString()
                    posicion.add(data)
                }
                /*AlertDialog.Builder(this)
                    .setTitle("Posiciones")
                    .setMessage(resultado)
                    .show()*/
            }

        locacion = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var Oyente = Oyente(this)
        locacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 01f, Oyente)
    }

    fun invocarOtraVentana() {
        val i = Intent(this, MainActivity::class.java)
        i.putExtra("Nombre",posicion.get(actual).nombre)
        i.putExtra("Descripcion",posicion.get(actual).descripcion)
        i.putExtra("Carpeta",posicion.get(actual).carpeta)
        startActivity(i)
        finish()
    }

    /*
    * Equipo Overtime
    *   Gustavo Marin Lemus
    *   Juan Luis Saucedo García
    *   Benjamin Maximiliano Zepeda Ibarra
    * */


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val feria = LatLng(21.483098, -104.882286)
        mMap.addMarker(MarkerOptions().position(feria).title("La feria Tepic"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(feria))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
    }
}