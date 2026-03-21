package de.ahlfeld.bitriseartifacts.feature.auth.testdata

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DataStoreFake(initialPreferences: Preferences = emptyPreferences()) : DataStore<Preferences> {
    private val _data = MutableStateFlow(initialPreferences)

    override val data: Flow<Preferences> = _data.asStateFlow()

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val currentData = _data.value
        val newData = transform(currentData)
        _data.value = newData
        return newData
    }
}
