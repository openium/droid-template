package fr.openium.template.ext

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.os.AsyncTaskCompat
import android.support.v7.content.res.AppCompatResources
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery
import retrofit2.adapter.rxjava.Result
import rx.Observable
import timber.log.Timber
import java.util.*

/**
 * Created by t.coulange on 25/03/16.
 */


fun isVersionMoreOrEqual(versionRequired: Int): Boolean {
    return Build.VERSION.SDK_INT >= versionRequired
}

fun Long.toUnixTimestamp(): Long {
    return this / 1000
}

fun Long.toMillis(): Long {
    return this * 1000
}

fun Int.toUnixTimestamp(): Int {
    return this / 1000
}

fun Int.toMillis(): Int {
    return this * 1000
}

fun TimeZone.getApiOffset(): Int {
    return getOffset(System.currentTimeMillis()).toUnixTimestamp()
}

fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable = AppCompatResources.getDrawable(this, resId)!!


fun Context.getColorCompat(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)

fun RelativeLayout.LayoutParams.removeRuleCompat(key: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        removeRule(key)
    } else {
        addRule(key, 0)
    }
}

fun <E : RealmModel> RealmQuery<E>.inExclusive(fieldName: String, values: Array<Long>?): RealmQuery<E> {
    if (values == null || values.isEmpty()) {
        throw IllegalArgumentException("Non-empty 'values' must be provided.")
    }
    beginGroup().equalTo(fieldName, values[0])
    for (i in 1..values.size - 1) {
        equalTo(fieldName, values[i])
    }
    return endGroup()
}

fun <A, B, C> AsyncTask<A, B, C>.executeCompat(vararg args: A): AsyncTask<A, B, C> = AsyncTaskCompat.executeParallel(this, *args)

fun <T> Observable<Result<T>>.subscribeInsertToRealm(exec: T.(Realm) -> Unit) {
    subscribe({
        result ->
        if (result?.response()?.isSuccessful ?: false) {
            val realm = Realm.getDefaultInstance()
            realm.use {
                it.executeTransaction {
                    exec.invoke(result.response()!!.body()!!, it)
                }
            }
        } else {
            Timber.e(result?.error())
        }
    }, {
        Timber.e(it)
    })
}

fun EditText.onActionDone(callback: () -> Unit) {
    setOnEditorActionListener(object : TextView.OnEditorActionListener {
        override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                callback.invoke()
                return true
            }
            return false
        }
    })
}

fun Int.toDurationFormatted(): String {
    val hours = this / 60;
    val minutes = this % 60;
    return "${hours}h${minutes}"
}

fun Calendar.clearToDay() {
    set(Calendar.MILLISECOND, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.HOUR_OF_DAY, 0)
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun String.toUUID(): UUID = UUID.fromString(this)

fun Double.toRoundedString(): String = Math.round(this).toInt().toString()

//inline fun <reified T : RealmModel> Realm.findForUid(field: KMutableProperty1<ActivityItem, Long>, uid: Long): T? {
//    return where(T::class.java).equalTo(field.name, uid).findFirst()
//}