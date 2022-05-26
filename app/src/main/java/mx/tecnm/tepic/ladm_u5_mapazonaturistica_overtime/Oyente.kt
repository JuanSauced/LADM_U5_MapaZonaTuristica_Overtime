package mx.tecnm.tepic.ladm_u5_mapazonaturistica_overtime

import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.firestore.GeoPoint

class Oyente(puntero:MapsActivity) : LocationListener {
    var p = puntero

    override fun onLocationChanged(location: Location){
        var geoPosicionesGPS = GeoPoint(location.latitude, location.longitude)
        for(item in p.posicion){
            if (item.estoyEn(geoPosicionesGPS)){
                p.actual=p.posicion.indexOf(item)
                p.invocarOtraVentana()
            }
        }
    }
}

/*
 AlertDialog.Builder(p)
     .setTitle("¡Atención!")
     .setMessage("Estas en: ${item.nombre}\n" +
                 "¿Deseas ver más información?")
     .setPositiveButton("¡Sí!"){p,q->}
     .setNegativeButton("¡No!"){p,q->}
     .show()
  */