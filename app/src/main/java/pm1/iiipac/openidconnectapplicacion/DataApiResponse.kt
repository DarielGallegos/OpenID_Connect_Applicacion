package pm1.iiipac.openidconnectapplicacion

data class DataApiResponse(
    val status: Int,
    val data: List<Any>,
    val message: String
)
