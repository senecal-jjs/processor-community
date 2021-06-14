package org.snowshoe.community.data.repository

import org.snowshoe.community.data.model.Profile
import java.util.UUID

interface ProfileRepository {
    fun fetch(id: UUID): Profile?

    fun create(profile: Profile): Profile
}