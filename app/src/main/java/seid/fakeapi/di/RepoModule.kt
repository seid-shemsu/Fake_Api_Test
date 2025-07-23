package seid.fakeapi.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import seid.fakeapi.data.repo.auth.AuthRepository
import seid.fakeapi.data.repo.auth.AuthRepositoryImpl
import seid.fakeapi.data.repo.favorite.FavoriteRepository
import seid.fakeapi.data.repo.favorite.FavoriteRepositoryImpl
import seid.fakeapi.data.repo.product.ProductRepository
import seid.fakeapi.data.repo.product.ProductRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        impl: FavoriteRepositoryImpl
    ): FavoriteRepository
}
