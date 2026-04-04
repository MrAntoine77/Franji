package fr.mrantoine.franji

import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.MainPageStorage

import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*



class CategoryNetworkUnitTest {
    @Test
    fun getCategoryKanjiId_TEST() = runBlocking {
        val kanjis = MainPageStorage.getMainPage("Kanji")
        print(kanjis)
        //assertTrue(kanjis.size > 0)
    }


}
