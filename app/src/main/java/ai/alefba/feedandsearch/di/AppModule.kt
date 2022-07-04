package ai.alefba.feedandsearch.di

import ai.alefba.feedandsearch.data.remote.FeedApi
import ai.alefba.feedandsearch.data.repository.FeedRepository
import ai.alefba.feedandsearch.data.repository.FeedRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @ApplicationScope
    @Provides
    @Singleton
    //so I can provide a singleton reference of application in coroutine
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }

    @Provides
    @Singleton
    fun provideFeedRepository(api : FeedApi) : FeedRepository{
        return FeedRepositoryImpl(api)
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope