package io.vallfg.valorantlfgmultiplatform.domain.usecase

import io.vallfg.valorantlfgmultiplatform.FilterString
import io.vallfg.valorantlfgmultiplatform.Filterable
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank

class ApplyPostFiltersUseCase {

    operator fun invoke(filterable: Filterable, appliedFilters: List<Filterable>): List<Filterable> {
        if (appliedFilters.contains(filterable)) {
            return appliedFilters.filter { it != filterable }
        }
        return when(filterable) {
            in Rank.values() ->   {
                buildList {
                    addAll(appliedFilters.filter { it !is Rank })
                    add(filterable)
                }
            }
            in GameMode.values() -> {
                buildList {
                    addAll(appliedFilters.filter { it !is GameMode })
                    add(filterable)
                }
            }
            is FilterString -> {
                buildList {
                    addAll(appliedFilters.filter { it !is FilterString })
                    add(filterable)
                }
            }
            else -> emptyList()
        }
    }
}