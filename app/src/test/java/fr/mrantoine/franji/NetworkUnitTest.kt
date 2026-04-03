package fr.mrantoine.franji

import fr.mrantoine.franji.storage.getKanjiByChar
import fr.mrantoine.franji.storage.getKanjiById
import fr.mrantoine.franji.storage.getLottie
import fr.mrantoine.franji.storage.getMainMage
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*



class CategoryNetworkUnitTest {
    @Test
    fun getCategoryKanjiId_TEST() = runBlocking {
        val category = `CategoryStorage.kt`.getCategoriesKanjiId("Kanji/JLPT5/Tout")
        assertTrue(category.size > 0)
        assertTrue(category[0] == "kanji1")
    }

    @Test
    fun getCategoryKanjiChar_TEST() = runBlocking {
        val category = `CategoryStorage.kt`.getCategoriesKanjiChar("Kanji/JLPT5/Tout")
        assertTrue(category.size > 0)
        assertTrue(category[0] == "人")
    }

    @Test
    fun getCategories_TEST() = runBlocking {
        val categories = `CategoryStorage.kt`.getCategories()
        assertTrue(categories.size > 0)
        assertTrue(categories.containsKey("Kanji"))
    }
}

class KanjiNetworkUnitTest {
    @Test
    fun getKanjiById_TEST() = runBlocking {
        val kanji = getKanjiById("kanji25")
        print(kanji)
        assertTrue(kanji.kanji=="本")
        assertTrue(kanji.id=="kanji25")
        assertTrue(kanji.lectures[0].fr[0] == "livre")
        assertTrue(kanji.lectures[0].ON[0] == "HON")
        assertTrue(kanji.lectures[1].kun[0] == "moto")
    }

    @Test
    fun getKanjiByChar_TEST() = runBlocking {
        val kanji = getKanjiByChar("本")
        print("=====>$kanji \n")
        assertTrue(kanji.kanji=="本")
        assertTrue(kanji.id=="kanji25")
        assertTrue(kanji.lectures[0].fr[0] == "livre")
        assertTrue(kanji.lectures[0].ON[0] == "HON")
        assertTrue(kanji.lectures[1].kun[0] == "moto")
    }

}


class lottieNetworkUnitTest {
    @Test
    fun getLottie_TEST() = runBlocking {
        val lottie = getLottie(kanji = "kanji1")
        println(lottie)
        assertTrue(lottie.isNotEmpty())
        assertTrue(lottie != "{}")
    }
}

class mainPageUnitTest {
    @Test
    fun getMainPage_TEST() = runBlocking {
        val mainPage = getMainMage()
        assertTrue(mainPage.Kanji.height == 200)
        assertTrue(mainPage.Kanji.width == 200)
        assertTrue(mainPage.Kanji.paths[0] == "Kanji/JLPT5/Tout")
    }
}