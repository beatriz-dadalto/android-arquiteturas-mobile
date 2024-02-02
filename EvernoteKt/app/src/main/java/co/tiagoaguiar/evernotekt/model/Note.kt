package co.tiagoaguiar.evernotekt.model

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class Note(
    var id: Int = 0,
    var title: String? = null,
    var desc: String? = null,
    var date: String? = null,
    var body: String? = null
) {

    val createdDate: String
        get() {
            val locale = Locale("pt", "BR")
            return try {
                val date: Date = SimpleDateFormat("dd/MM/yyyy", locale).parse(date ?: "")
                SimpleDateFormat("MMM yyyy", locale).format(date)
                    .replaceFirst("F", "F", true)
            } catch (e: ParseException) {
                ""
            }
        }
}