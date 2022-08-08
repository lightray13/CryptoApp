package com.testing.cryptoapp.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.testing.cryptoapp.util.Constants
import com.testing.cryptoapp.util.get
import com.testing.cryptoapp.util.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private const val PREFERENCES_NAME = "COINS_PREFERENCES"

interface PreferenceStorage {
    var targetCurrency: String
    var coinsFilterBy: Int
    var currency: String

    val coinsFilterByFlow: Flow<Int>
}

@Singleton
class DataPreferenceStorage @Inject constructor(context: Context): PreferenceStorage {

    private val dataStore: DataStore<Preferences> =
        context.applicationContext.dataStore

    override var targetCurrency by Preference(
        dataStore = dataStore,
        key = stringPreferencesKey(name = "TARGET_CURRENCY"),
        defaultValue = "usd"
    )
    override var coinsFilterBy by Preference(
        dataStore = dataStore,
        key = intPreferencesKey(name = "COINS_FILTER_BY"),
        defaultValue = Constants.COINS_FILTER_BY_DEFAULT
    )

    override var currency by Preference(
        dataStore = dataStore,
        key = stringPreferencesKey(name = "CURRENCY"),
        defaultValue = "USD"
    )

    override val coinsFilterByFlow = dataStore.data
        .map { coinsFilterBy }
}

@Suppress("UNCHECKED_CAST")
class Preference<T>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
    private val defaultValue: T
): ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>) =
        dataStore.get(key = key, defaultValue = defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        dataStore.set(key = key, value = value)
    }
}

private val Context.dataStore by preferencesDataStore(
    name = PREFERENCES_NAME
)