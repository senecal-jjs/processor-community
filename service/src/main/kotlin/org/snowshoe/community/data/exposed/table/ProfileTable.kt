package org.snowshoe.community.data.exposed.table

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.snowshoe.community.data.json.JsonBColumnType
import org.snowshoe.community.data.model.Profile
import java.util.UUID

object ProfileTable : UUIDTable(name = "profile", columnName = "id") {
    val profile = ProfileTable.registerColumn<Profile>("profile", object : JsonBColumnType<Profile>() {})
}

class ProfileRecord(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ProfileRecord>(ProfileTable)

    var profile by ProfileTable.profile
}