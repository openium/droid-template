package fr.openium.template.ext

import io.realm.Realm
import io.realm.RealmModel

/**
 * Created by t.coulange on 25/03/16.
 */
fun Realm.insertInTransaction(model: RealmModel) {
    executeTransaction {
        insertOrUpdate(model)
    }
}

fun Realm.insertInTransaction(models: Collection<RealmModel>) {
    executeTransaction {
        insertOrUpdate(models)
    }
}