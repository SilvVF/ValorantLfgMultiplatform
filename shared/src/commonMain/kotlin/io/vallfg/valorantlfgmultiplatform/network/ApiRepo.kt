package io.vallfg.valorantlfgmultiplatform.network

import com.apollographql.apollo3.ApolloClient
import io.vallfg.PlayerMutation
import io.vallfg.PlayerQuery
import io.vallfg.PostQuery
import io.vallfg.type.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiRepo(
    private val client: ApolloClient
) {

    suspend fun login(name: String, tag: String): ApiResponse<PlayerMutation.Data> =
        withContext(Dispatchers.Default) {
           client.mutation(
               PlayerMutation(name, tag)
           )
               .executeAsApiResponse()
        }

    suspend fun getPlayer(name: String, tag: String): ApiResponse<PlayerQuery.Data> =
        withContext(Dispatchers.Default){
            client.query(
                PlayerQuery(name, tag)
            )
                .executeAsApiResponse()
        }

    suspend fun getPost(amount: Int, offset: Int): ApiResponse<PostQuery.Data> =
        withContext(Dispatchers.Default) {
            client.query(
                PostQuery(amount, offset)
            )
                .executeAsApiResponse()
        }
}