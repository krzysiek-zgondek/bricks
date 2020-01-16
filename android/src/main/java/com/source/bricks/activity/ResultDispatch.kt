package com.source.bricks.activity

import android.content.Intent

/**
 * @project Bricks
 * @author SourceOne on 15.01.2020
 */

/**
 * Wrapper class for [android.app.Activity.onActivityResult] arguments
 * */
data class ResultDispatch(
    val requestCode: Int,
    val resultCode: Int,
    val data: Intent?
)

