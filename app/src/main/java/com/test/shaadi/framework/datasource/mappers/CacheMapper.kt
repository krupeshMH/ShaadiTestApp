package com.test.shaadi.framework.datasource.mappers

import com.test.shaadi.business.domain.EntityMapper
import com.test.shaadi.business.domain.model.*
import com.test.shaadi.framework.datasource.model.MemberCacheEntity
import java.net.IDN
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CacheMapper
@Inject
constructor(
) : EntityMapper<MemberCacheEntity, MemberEach> {

    fun entityListToMemberList(entities: List<MemberCacheEntity>): List<MemberEach> {
        val list: ArrayList<MemberEach> = ArrayList()
        for (entity in entities) {
            list.add(mapFromEntity(entity))
        }
        return list
    }

    fun memberListToEntityList(members: List<MemberEach>): List<MemberCacheEntity> {
        val entities: ArrayList<MemberCacheEntity> = ArrayList()
        for (member in members) {
            entities.add(mapToEntity(member))
        }
        return entities
    }

    override fun mapFromEntity(entity: MemberCacheEntity): MemberEach {
        val memberEach = MemberEach(
            cell = entity.cell,
            dob = Dob(entity.dob.toInt(), ""),
            email = entity.email,
            gender = entity.gender,
            id = Id("", entity.id),
            location = Location(
                entity.location,
                Coordinates("", ""),
                "",
                "",
                "",
                Street("", 0),
                Timezone("", "")
            ),
            login = Login("", "", "", "", "", "", entity.uuid),
            name = Name(entity.name, "", ""),
            nat = entity.nat,
            phone = entity.phone,
            picture = Picture(entity.picture, "", ""),
            registered = Registered(0, "")
        )
        return memberEach
    }

    override fun mapToEntity(domainModel: MemberEach): MemberCacheEntity {
        return MemberCacheEntity(
            cell = domainModel.cell ?: "",
            dob = domainModel.dob.age.toString() ?: "",
            email = domainModel.email ?: "",
            gender = domainModel.gender ?: "",
            id = domainModel.id.value ?: "",
            location = domainModel.location.city + " , " + domainModel.location.state + " , " + domainModel.location.country,
            uuid = domainModel.login.uuid,
            name = domainModel.name.first + " " + domainModel.name.last,
            nat = domainModel.nat ?: "",
            phone = domainModel.phone ?: "",
            picture = domainModel.picture.large ?: "",
            is_accepted = 0
        )
    }
}