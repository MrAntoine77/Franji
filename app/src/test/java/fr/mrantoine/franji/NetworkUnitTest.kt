package fr.mrantoine.franji

import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.MainPageStorage
import fr.mrantoine.franji.storage.VocabStorage
import fr.mrantoine.franji.storage.compressUserData
import fr.mrantoine.franji.storage.decompressUserData

import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*



class CategoryNetworkUnitTest {
    @Test
    fun getVocab() = runBlocking {
        val keys = CategoryStorage.getKeys()
        val user_data = decompressUserData("0|1|1775685600", keys)
        print(user_data)
        val compressed = compressUserData(user_data, keys)
        print(compressed)
    }
}
