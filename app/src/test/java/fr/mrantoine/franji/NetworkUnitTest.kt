package fr.mrantoine.franji

import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.MainPageStorage
import fr.mrantoine.franji.storage.VocabStorage

import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*



class CategoryNetworkUnitTest {
    @Test
    fun getVocab() = runBlocking {
        val vocab = VocabStorage.getVocabById("vocab2")
        print(vocab)
        //assertTrue(kanjis.size > 0)
    }
}
