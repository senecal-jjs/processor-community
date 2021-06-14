package org.snowshoe.community.data.exposed.repository

import org.jetbrains.exposed.sql.transactions.transaction
import org.snowshoe.community.data.exposed.table.ProfileRecord
import org.snowshoe.community.data.model.Profile
import org.snowshoe.community.data.repository.ProfileRepository
import java.util.UUID

class ExposedProfileRepository : ProfileRepository {
    override fun fetch(id: UUID): Profile? {
        return transaction {
            ProfileRecord.findById(id)?.profile
        }
    }

    override fun create(profile: Profile): Profile {
        return transaction {
            val record = ProfileRecord.new(UUID.randomUUID()) {
                this.profile = profile
            }
            commit()
            record.profile
        }
    }
}