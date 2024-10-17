package com.lichle.weather.domain

import com.lichle.weather.data.repository.CityRepository
import com.lichle.weather.domain.city.AddCityUseCase
import com.lichle.weather.domain.city.DeleteCityUseCase
import com.lichle.weather.domain.city.FetchCityUserCase
import com.lichle.weather.domain.city.GetCityListUseCase
import com.lichle.weather.domain.city.GetCityUseCase
import com.lichle.weather.domain.city.SearchCityUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @Named("GetCityListUseCase")
    fun provideGetCityListUseCase(
        cityRepository: CityRepository
    ): BaseUseCase<NoRequest, List<City>> {
        return GetCityListUseCase(cityRepository)
    }

    @Provides
    @Named("DeleteCityUseCase")
    fun provideDeleteCityUseCase(
        cityRepository: CityRepository
    ): BaseUseCase<DeleteCityUseCase.DeleteRequest, Unit> {
        return DeleteCityUseCase(cityRepository)
    }

    @Provides
    @Named("SearchCityUseCase")
    fun provideSearchCityUseCase(
        cityRepository: CityRepository
    ): BaseUseCase<SearchCityUseCase.SearchRequest, City?> {
        return SearchCityUseCase(cityRepository)
    }

    @Provides
    @Named("AddCityUseCase")
    fun provideAddCityUseCase(
        cityRepository: CityRepository
    ): BaseUseCase<AddCityUseCase.AddRequest, Unit> {
        return AddCityUseCase(cityRepository)
    }

    @Provides
    @Named("GetCityUseCase")
    fun provideGetCityUseCase(
        cityRepository: CityRepository
    ): BaseUseCase<GetCityUseCase.GetRequest, City> {
        return GetCityUseCase(cityRepository)
    }

    @Provides
    @Named("FetchCityUserCase")
    fun provideFetchCityUseCase(
        cityRepository: CityRepository
    ): BaseUseCase<FetchCityUserCase.FetchRequest, City> {
        return FetchCityUserCase(cityRepository)
    }

}