package com.thorn11166.unchainedtorbox.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.thorn11166.unchainedtorbox.data.model.HostRegex
import com.thorn11166.unchainedtorbox.data.model.KodiDevice
import com.thorn11166.unchainedtorbox.data.model.PluginVersion
import com.thorn11166.unchainedtorbox.data.model.Repository
import com.thorn11166.unchainedtorbox.data.model.RepositoryInfo
import com.thorn11166.unchainedtorbox.data.model.RepositoryPlugin

/** Annotates class to be a Room Database with a table (entity) of the Credentials class */
@Database(
    entities =
        [
            HostRegex::class,
            KodiDevice::class,
            Repository::class,
            RepositoryInfo::class,
            RepositoryPlugin::class,
            PluginVersion::class,
            RemoteDevice::class,
            RemoteService::class,
        ],
    version = 7,
    exportSchema = true,
    autoMigrations =
        [
            AutoMigration(from = 4, to = 5),
            AutoMigration(from = 5, to = 6),
            AutoMigration(from = 6, to = 7),
        ],
)
abstract class UnchaineDB : RoomDatabase() {
    abstract fun hostRegexDao(): HostRegexDao

    abstract fun kodiDeviceDao(): KodiDeviceDao

    abstract fun pluginRepositoryDao(): RepositoryDataDao

    abstract fun pluginRemoteDeviceDao(): RemoteDeviceDao
}
