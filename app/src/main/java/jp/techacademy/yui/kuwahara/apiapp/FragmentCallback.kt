package jp.techacademy.yui.kuwahara.apiapp

interface FragmentCallback {

    // Itemを押したときの処理
    fun onClickItem(shop: Shop)//ViewHolder（クーポン画面にいくために、一つのセルをクリックした時）

    //fun onClickItem2(url: String)
    // お気に入り追加時の処理
    fun onAddFavorite(shop: Shop)
    // お気に入り削除時の処理
    fun onDeleteFavorite(id: String)
}