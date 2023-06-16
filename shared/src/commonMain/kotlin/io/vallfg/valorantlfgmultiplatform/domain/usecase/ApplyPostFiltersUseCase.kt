package io.vallfg.valorantlfgmultiplatform.domain.usecase

import io.vallfg.valorantlfgmultiplatform.Needed
import io.vallfg.valorantlfgmultiplatform.Filterable
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.SortBy

/**
 * Use Case to combine a new filterable with the current filters.
 * If the list already contains the filterable it is removed.
 * Only one filterable per category is allowed.
 * categories are [Rank], [GameMode], [Needed] - needed
 */
class ApplyPostFiltersUseCase {

    operator fun invoke(filterable: Filterable, appliedFilters: List<Filterable>): List<Filterable> {
        if (appliedFilters.contains(filterable)) {
            return appliedFilters.filter { it != filterable }
        }
        return when(filterable) {
            is Rank ->   {
                buildList {
                    addAll(appliedFilters.filter { it !is Rank })
                    add(filterable)
                }
            }
            is GameMode -> {
                buildList {
                    addAll(appliedFilters.filter { it !is GameMode })
                    add(filterable)
                }
            }
            is Needed -> {
                buildList {
                    addAll(appliedFilters.filter { it !is Needed })
                    add(filterable)
                }
            }
            is SortBy -> {
                buildList {
                    addAll(appliedFilters.filter { it !is SortBy })
                    add(filterable)
                }
            }
        }
    }
}