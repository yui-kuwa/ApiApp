package jp.techacademy.yui.kuwahara.apiapp
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ApiResponse(
    @SerializedName("results")
    val results: Results
)

data class Results(
    @SerializedName("shop")
    val shop: List<Shop>
)

data class Shop(
    @SerializedName("coupon_urls")
    var couponUrls: CouponUrls,
    @SerializedName("id")
    var id: String,
    @SerializedName("logo_image")
    var logoImage: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("address")
    var address: String
):Serializable//Serializableにすることでデータを丸ごとファイルに保存したり、別のActivityに渡すことができる

data class CouponUrls(
    @SerializedName("pc")
    val pc: String,
    @SerializedName("sp")
    var sp: String
):Serializable
