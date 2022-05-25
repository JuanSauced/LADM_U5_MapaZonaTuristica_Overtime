package mx.tecnm.tepic.ladm_u5_mapazonaturistica_overtime

import android.location.Location
import android.location.LocationListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.GeoPoint

class Oyente(puntero:MapsActivity) : LocationListener {

    var p = puntero

    override fun onLocationChanged(location: Location) {
        var geoPosicionesGPS = GeoPoint(location.latitude, location.longitude)

        for(item in p.posicion){
            if (item.estoyEn(geoPosicionesGPS)){
                /*Toast.makeText(p, "Estas en: ${item.nombre}", Toast.LENGTH_LONG)
                    .show()
                */
                AlertDialog.Builder(p)
                    .setTitle("¡Atencion!")
                    .setMessage("Estas en: ${item.nombre}\n" +
                                "¿Deseas ver más información?")
                    .setPositiveButton("¡Sí!"){p,q->}
                    .setNegativeButton("¡No!"){p,q->}
                    .show()
            }
        }
    }

}