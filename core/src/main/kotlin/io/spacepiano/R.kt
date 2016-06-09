package io.spacepiano

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin

// TODO: make cleaner using reified type parameters and/or lazy
object R : AssetManager() {
    val SKIN = A("ui.json", Skin::class.java)
    val SHIP_BLUE = A("ship-mod-blue.atlas", TextureAtlas::class.java)
    val SHIP_GREEN = A("ship-mod-green.atlas", TextureAtlas::class.java)
    val SHIP_YELLOW = A("ship-mod-yellow.atlas", TextureAtlas::class.java)
    val SHIP_WHITE = A("ship-mod-white.atlas", TextureAtlas::class.java)


    init {
        loadNow(SKIN)
    }

    fun loadNow(asset: AssetDescriptor<*>) {
        load(asset)
        finishLoadingAsset(asset.fileName)
    }

    fun loadAll() = R::class.java.declaredMethods
            .filter { it.name.startsWith("get") and it.returnType.isAssignableFrom(AssetDescriptor::class.java) }
            .forEach { load(it(R) as AssetDescriptor<*>?) }

    private fun <T> A(fileName: String, type: Class<T>) = AssetDescriptor(fileName, type)
}