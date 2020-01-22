package com.source.bricks.bundle

import android.os.Bundle

/**
 * @project Bricks
 * @author SourceOne on 21.01.2020
 */

/*
* Default implementation of how to extract any [Type] of data from [Bundle]
* */
inline fun <reified Type> defaultBundleDecoder(bundle: Bundle, key: String): Type? {
    return bundle.get(key) as? Type?
}