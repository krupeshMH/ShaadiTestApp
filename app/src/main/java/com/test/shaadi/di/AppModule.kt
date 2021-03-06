package com.test.shaadi.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.shaadi.business.data.cache.abstraction.MembersCacheDataSource
import com.test.shaadi.business.data.cache.implementation.MembersCacheDataSourceImpl
import com.test.shaadi.business.data.network.abstraction.MembersNetworkDataSource
import com.test.shaadi.business.data.network.implementation.MembersNetworkDataSourceImpl
import com.test.shaadi.business.usecases.GetMembers
import com.test.shaadi.business.usecases.MemberListInteractors
import com.test.shaadi.framework.datasource.cache.abstraction.MembersDaoService
import com.test.shaadi.framework.datasource.cache.database.MemberDao
import com.test.shaadi.framework.datasource.cache.database.MemberDatabase
import com.test.shaadi.framework.datasource.cache.implementation.MembersDaoServiceImpl
import com.test.shaadi.framework.datasource.mappers.CacheMapper
import com.test.shaadi.framework.datasource.network.GetMembersAPI
import com.test.shaadi.framework.datasource.network.abstraction.MembersNetworkService
import com.test.shaadi.framework.datasource.network.implementation.MembersNetworkServiceImpl
import com.test.shaadi.framework.presentation.BaseApplication
import com.test.shaadi.framework.presentation.memberlist.MemberSyncManager
import com.test.shaadi.util.Constants
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Module
object AppModule {

    @Singleton
    @Provides
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(gson: Gson): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BASIC)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val original = chain.request()

                // Customize the request
                val request = original.newBuilder()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .removeHeader("Pragma")
                    .build()

                val response = chain.proceed(request)
                // Customize or return the response
                response
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }

    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit): GetMembersAPI {
        return retrofit.create<GetMembersAPI>(GetMembersAPI::class.java)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMembersNetworkService(
        api: GetMembersAPI
    ): MembersNetworkService {
        return MembersNetworkServiceImpl(api)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMemberNetworkDataSource(
        membersNetworkService: MembersNetworkService
    ): MembersNetworkDataSource {
        return MembersNetworkDataSourceImpl(membersNetworkService)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMemberDb(app: BaseApplication): MemberDatabase {
        return Room
            .databaseBuilder(app, MemberDatabase::class.java, MemberDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMemberDAO(memberDatabase: MemberDatabase): MemberDao {
        return memberDatabase.memberDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMemberCacheMapper(): CacheMapper {
        return CacheMapper()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMemberDaoService(
        dao: MemberDao,
        memberMapper: CacheMapper
    ): MembersDaoService {
        return MembersDaoServiceImpl(dao, memberMapper)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMemberCacheDataSource(
        membersDaoService: MembersDaoService
    ): MembersCacheDataSource {
        return MembersCacheDataSourceImpl(membersDaoService)
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideGetMembers(
        membersCacheDataSource: MembersCacheDataSource,
        membersNetworkDataSource: MembersNetworkDataSource
    ): GetMembers {
        return GetMembers(
            membersCacheDataSource,
            membersNetworkDataSource
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMemberSyncManager(
        getMembers: GetMembers
    ): MemberSyncManager {
        return MemberSyncManager(
            getMembers
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMemberListInteractors(getMembers: GetMembers): MemberListInteractors {
        return MemberListInteractors(getMembers)
    }
}